package domain;
import java.util.Map;

/** Persiste e recupera objetos Secretaria em data/secretarias.csv */
public final class RepositorioSecretaria extends RepositorioArquivo<Secretaria> {

    @Override
    protected String getCaminhoArquivo() { return "data/secretarias.csv"; }

    // formato: idUsuario;cargo;emailCorporativo;senhaHash;rua;numero;cidade;bairro;cep;complemento
    @Override
    protected String serializar(Secretaria s) {
        Endereco e = s.getEndereco();
        return String.join(";",
                s.getIdUsuario().toString(),
                esc(s.getCargo()),
                esc(s.getEmailCorporativo()),
                esc(s.getSenhaHash()),
                esc(e.getRua()), esc(e.getNumero()), esc(e.getCidade()),
                esc(e.getBairro()), esc(e.getCEP()), esc(e.getComplemento()));
    }

    @Override
    protected Secretaria desserializar(String[] c, Map<String, Object> ctx) {
        if (c.length < 10) return null;
        Endereco end = new Endereco(c[4], c[5], c[6], c[7], c[8], c[9]);
        // Usa construtor de reconstrucao com hash ja pronto
        Secretaria s = new Secretaria(c[1], c[2], c[3], end, true);
        ctx.put(c[0], s);
        return s;
    }
}

