package domain;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public abstract class Usuario extends Pessoa {
    private static final int ITERACOES_HASH = 120_000;
    private static final int TAMANHO_SALT = 16;
    private static final SecureRandom RANDOM = new SecureRandom();
    private final UUID idUsuario;
    private String emailCorporativo;
    private String senha;

    protected Usuario(String emailCorporativo, String senha, Endereco endereco) {
        super(validarCadastro(emailCorporativo, senha, endereco));
        this.idUsuario = UUID.randomUUID();
        this.emailCorporativo = normalizarEmail(emailCorporativo);
        this.senha = gerarHash(senha);
    }

    protected Usuario(String emailCorporativo, String senhaHashPronta, Endereco endereco, UUID idUsuario) {
        super(Objects.requireNonNull(idUsuario), validarCadastro(emailCorporativo, senhaHashPronta, endereco));
        this.idUsuario = idUsuario;
        this.emailCorporativo = normalizarEmail(emailCorporativo);
        this.senha = Objects.requireNonNull(senhaHashPronta);
    }

    /** Acesso ao hash da senha para serializacao. Uso exclusivo da camada de persistencia. */
    String getSenhaHash() { return senha; }

    private static Endereco validarCadastro(String email, String senha, Endereco endereco) {
        if (email == null || email.isBlank() || senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("Email e senha obrigatorios");
        }
        return Objects.requireNonNull(endereco, "Endereco obrigatorio");
    }

    private static String normalizarEmail(String email) {
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email obrigatorio");
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private static String gerarHash(String valor) {
        byte[] salt = new byte[TAMANHO_SALT];
        RANDOM.nextBytes(salt);
        return "pbkdf2$" + ITERACOES_HASH + "$" + HexFormat.of().formatHex(salt)
                + "$" + HexFormat.of().formatHex(calcularHash(valor, salt, ITERACOES_HASH));
    }

    private static byte[] calcularHash(String valor, byte[] salt, int iteracoes) {
        try {
            PBEKeySpec spec = new PBEKeySpec(valor.toCharArray(), salt, iteracoes, 256);
            try {
                return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                        .generateSecret(spec).getEncoded();
            } finally {
                spec.clearPassword();
            }
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("PBKDF2 indisponivel", e);
        }
    }

    private boolean validarHashAtual(String valor) {
        if (!senha.startsWith("pbkdf2$")) {
            byte[] legado = MessageDigestHolder.sha256((idUsuario + ":" + valor).getBytes(StandardCharsets.UTF_8));
            return MessageDigest.isEqual(senha.getBytes(StandardCharsets.UTF_8),
                    HexFormat.of().formatHex(legado).getBytes(StandardCharsets.UTF_8));
        }
        try {
            String[] partes = senha.split("\\$", -1);
            if (partes.length != 4) return false;
            int iteracoes = Integer.parseInt(partes[1]);
            byte[] salt = HexFormat.of().parseHex(partes[2]);
            byte[] hashSalvo = HexFormat.of().parseHex(partes[3]);
            return MessageDigest.isEqual(hashSalvo, calcularHash(valor, salt, iteracoes));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public UUID getIdUsuario() { return idUsuario; }
    public String getEmailCorporativo() { return emailCorporativo; }
    public void setEmailCorporativo(String emailCorporativo) {
        this.emailCorporativo = normalizarEmail(emailCorporativo);
    }

    public boolean autenticar(String emailCorporativo, String senha) {
        if (emailCorporativo == null || senha == null
                || !this.emailCorporativo.equals(emailCorporativo.trim().toLowerCase(Locale.ROOT))) return false;
        if (!validarHashAtual(senha)) return false;
        if (!this.senha.startsWith("pbkdf2$")) this.senha = gerarHash(senha);
        return true;
    }

    public void alterarSenha(String senhaAtual, String novaSenha) {
        if (!autenticar(emailCorporativo, senhaAtual)) {
            throw new IllegalArgumentException("Senha atual incorreta");
        }
        if (novaSenha == null || novaSenha.isBlank()) {
            throw new IllegalArgumentException("Nova senha obrigatoria");
        }
        senha = gerarHash(novaSenha);
    }

    private static final class MessageDigestHolder {
        private static byte[] sha256(byte[] valor) {
            try {
                return MessageDigest.getInstance("SHA-256").digest(valor);
            } catch (java.security.NoSuchAlgorithmException e) {
                throw new IllegalStateException("SHA-256 indisponivel", e);
            }
        }
    }
}

