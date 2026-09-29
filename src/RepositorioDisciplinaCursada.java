import java.util.Map;

/** Persiste e recupera objetos DisciplinaCursada em data/disciplinas_cursadas.csv */
public final class RepositorioDisciplinaCursada extends RepositorioArquivo<DisciplinaCursada> {

    @Override
    protected String getCaminhoArquivo() { return "data/disciplinas_cursadas.csv"; }

    // formato: semestre;isAprovado;notaFinal;matriculaAluno;codigoDisciplina
    @Override
    protected String serializar(DisciplinaCursada d) {
        return String.join(";",
                esc(d.getSemestre()),
                String.valueOf(d.getIsAprovado()),
                String.valueOf(d.getNotaFinal()),
                esc(d.getAluno().getMatricula()),
                esc(d.getDisciplina().getCodigo()));
    }

    @Override
    protected DisciplinaCursada desserializar(String[] c, Map<String, Object> ctx) {
        if (c.length < 5) return null;
        Aluno aluno = (Aluno) ctx.get(c[3]);
        Disciplina disc = (Disciplina) ctx.get(c[4]);
        if (aluno == null || disc == null) return null;
        return new DisciplinaCursada(c[0], Boolean.parseBoolean(c[1]),
                Double.parseDouble(c[2]), aluno, disc);
    }
}
