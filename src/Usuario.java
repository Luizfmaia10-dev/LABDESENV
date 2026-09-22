import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.UUID;

public abstract class Usuario extends Pessoa {
    private final UUID idUsuario = UUID.randomUUID();
    private String emailCorporativo;
    // O atributo guarda um resumo com salt, nunca a senha original.
    private String senha;

    protected Usuario(String emailCorporativo, String senha, Endereco endereco) {
        super(validarCadastro(emailCorporativo, senha, endereco));
        this.emailCorporativo = emailCorporativo;
        this.senha = resumo(senha);
    }

    private static Endereco validarCadastro(String email, String senha, Endereco endereco) {
        if (email == null || email.isBlank() || senha == null || senha.isBlank()) {
            throw new IllegalArgumentException("Email e senha obrigatorios");
        }
        return Objects.requireNonNull(endereco, "Endereco obrigatorio");
    }

    private String resumo(String valor) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest((idUsuario + ":" + valor).getBytes(StandardCharsets.UTF_8));
            return java.util.HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 indisponivel", e);
        }
    }

    public UUID getIdUsuario() { return idUsuario; }
    public String getEmailCorporativo() { return emailCorporativo; }
    public void setEmailCorporativo(String emailCorporativo) {
        if (emailCorporativo == null || emailCorporativo.isBlank()) {
            throw new IllegalArgumentException("Email corporativo obrigatorio");
        }
        this.emailCorporativo = emailCorporativo;
    }

    public boolean autenticar(String emailCorporativo, String senha) {
        return this.emailCorporativo.equals(emailCorporativo) && senha != null
                && MessageDigest.isEqual(this.senha.getBytes(StandardCharsets.UTF_8),
                        resumo(senha).getBytes(StandardCharsets.UTF_8));
    }

    public void alterarSenha(String senhaAtual, String novaSenha) {
        if (!autenticar(emailCorporativo, senhaAtual)) {
            throw new IllegalArgumentException("Senha atual incorreta");
        }
        if (novaSenha == null || novaSenha.isBlank()) {
            throw new IllegalArgumentException("Nova senha obrigatoria");
        }
        senha = resumo(novaSenha);
    }
}
