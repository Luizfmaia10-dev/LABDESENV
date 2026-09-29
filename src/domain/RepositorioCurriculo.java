package domain;
import java.util.Map;

/** Persiste e recupera objetos Curriculo em data/curriculos.csv */
public final class RepositorioCurriculo extends RepositorioArquivo<Curriculo> {

    @Override
    protected String getCaminhoArquivo() { return "data/curriculos.csv"; }

    // formato: semestre;periodoAberto;idSecretaria
    @Override
    protected String serializar(Curriculo c) {
        return String.join(";",
                esc(c.getSemestre()),
                String.valueOf(c.isPeriodoMatriculaAberto()),
                esc(c.getSecretaria().getIdUsuario().toString()));
    }

    @Override
    protected Curriculo desserializar(String[] c, Map<String, Object> ctx) {
        if (c.length < 3) return null;
        Secretaria sec = (Secretaria) ctx.get(c[2]);
        if (sec == null) return null;
        Curriculo curriculo = new Curriculo(c[0], Boolean.parseBoolean(c[1]), sec);
        ctx.put(c[0], curriculo); // indexado pelo semestre
        return curriculo;
    }
}

