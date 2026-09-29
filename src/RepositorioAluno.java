import java.util.Map;

/** Persiste e recupera objetos Aluno em data/alunos.csv */
public final class RepositorioAluno extends RepositorioArquivo<Aluno> {

    @Override
    protected String getCaminhoArquivo() { return "data/alunos.csv"; }

    // formato: idUsuario;matricula;status;codigoCurso;idSecretaria;email;senhaHash;rua;numero;cidade;bairro;cep;complemento
    @Override
    protected String serializar(Aluno a) {
        Endereco e = a.getEndereco();
        return String.join(";",
                a.getIdUsuario().toString(),
                esc(a.getMatricula()),
                a.getStatus().name(),
                esc(a.getCurso().getCodigo()),
                esc(a.getSecretaria().getIdUsuario().toString()),
                esc(a.getEmailCorporativo()),
                esc(a.getSenhaHash()),
                esc(e.getRua()), esc(e.getNumero()), esc(e.getCidade()),
                esc(e.getBairro()), esc(e.getCEP()), esc(e.getComplemento()));
    }

    @Override
    protected Aluno desserializar(String[] c, Map<String, Object> ctx) {
        if (c.length < 13) return null;
        Curso curso = (Curso) ctx.get(c[3]);
        Secretaria sec = (Secretaria) ctx.get(c[4]);
        if (curso == null || sec == null) return null;
        Endereco end = new Endereco(c[7], c[8], c[9], c[10], c[11], c[12]);
        StatusMatricula status = StatusMatricula.valueOf(c[2]);
        Aluno a = new Aluno(c[1], status, curso, sec, c[5], c[6], end, true);
        ctx.put(c[0], a);
        ctx.put(c[1], a); // indexado tambem pela matricula
        return a;
    }
}
