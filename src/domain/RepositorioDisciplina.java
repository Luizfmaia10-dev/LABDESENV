package domain;
import java.util.Map;

/** Persiste e recupera objetos Disciplina em data/disciplinas.csv */
public final class RepositorioDisciplina extends RepositorioArquivo<Disciplina> {

    @Override
    protected String getCaminhoArquivo() { return "data/disciplinas.csv"; }

    // formato: nome;codigo;creditos;codigoCurso;idSecretaria
    @Override
    protected String serializar(Disciplina d) {
        return String.join(";",
                esc(d.getNome()),
                esc(d.getCodigo()),
                String.valueOf(d.getCreditos()),
                esc(d.getCurso().getCodigo()),
                esc(d.getSecretaria().getIdUsuario().toString()));
    }

    @Override
    protected Disciplina desserializar(String[] c, Map<String, Object> ctx) {
        if (c.length < 5) return null;
        Curso curso = (Curso) ctx.get(c[3]);
        Secretaria sec = (Secretaria) ctx.get(c[4]);
        if (curso == null || sec == null) return null;
        Disciplina d = new Disciplina(c[0], c[1], Integer.parseInt(c[2]), curso, sec);
        ctx.put(c[1], d); // indexado pelo codigo da disciplina
        return d;
    }
}

