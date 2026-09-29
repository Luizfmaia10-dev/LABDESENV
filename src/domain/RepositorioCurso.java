package domain;
import java.util.Map;

/** Persiste e recupera objetos Curso em data/cursos.csv */
public final class RepositorioCurso extends RepositorioArquivo<Curso> {

    @Override
    protected String getCaminhoArquivo() { return "data/cursos.csv"; }

    // formato: nome;codigo;creditosTotais
    @Override
    protected String serializar(Curso c) {
        return String.join(";",
                esc(c.getNome()),
                esc(c.getCodigo()),
                String.valueOf(c.getCreditoTotais()));
    }

    @Override
    protected Curso desserializar(String[] c, Map<String, Object> ctx) {
        if (c.length < 3) return null;
        Curso curso = new Curso(c[0], c[1], Integer.parseInt(c[2]));
        ctx.put(c[1], curso); // indexado pelo codigo do curso
        return curso;
    }
}

