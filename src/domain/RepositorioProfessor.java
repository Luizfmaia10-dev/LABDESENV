package domain;
import java.util.Map;

/** Persiste e recupera objetos Professor em data/professores.csv */
public final class RepositorioProfessor extends RepositorioArquivo<Professor> {

    @Override
    protected String getCaminhoArquivo() { return "data/professores.csv"; }

    // formato: idUsuario;codigo;idSecretaria;emailCorporativo;senhaHash;rua;numero;cidade;bairro;cep;complemento
    @Override
    protected String serializar(Professor p) {
        Endereco e = p.getEndereco();
        return String.join(";",
                p.getIdUsuario().toString(),
                esc(p.getCodigo()),
                esc(p.getSecretaria().getIdUsuario().toString()),
                esc(p.getEmailCorporativo()),
                esc(p.getSenhaHash()),
                esc(e.getRua()), esc(e.getNumero()), esc(e.getCidade()),
                esc(e.getBairro()), esc(e.getCEP()), esc(e.getComplemento()));
    }

    @Override
    protected Professor desserializar(String[] c, Map<String, Object> ctx) {
        if (c.length < 11) return null;
        Secretaria sec = (Secretaria) ctx.get(c[2]);
        if (sec == null) return null;
        Endereco end = new Endereco(c[5], c[6], c[7], c[8], c[9], c[10]);
        Professor p = new Professor(c[1], sec, c[3], c[4], end, java.util.UUID.fromString(c[0]));
        ctx.put(c[0], p);
        return p;
    }
}

