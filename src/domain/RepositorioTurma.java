package domain;
import java.util.Map;

/** Persiste e recupera objetos Turma em data/turmas.csv */
public final class RepositorioTurma extends RepositorioArquivo<Turma> {

    @Override
    protected String getCaminhoArquivo() { return "data/turmas.csv"; }

    // formato: codigo;limiteMaximo;limiteMinimo;isAtiva;codigoDisciplina;idProfessor;semestre
    @Override
    protected String serializar(Turma t) {
        return String.join(";",
                esc(t.getCodigo()),
                String.valueOf(t.getLimiteMaximo()),
                String.valueOf(t.getLimiteMinimo()),
                String.valueOf(t.isAtiva()),
                esc(t.getDisciplina().getCodigo()),
                esc(t.getProfessor().getIdUsuario().toString()),
                esc(t.getCurriculo().getSemestre()));
    }

    @Override
    protected Turma desserializar(String[] c, Map<String, Object> ctx) {
        if (c.length < 7) return null;
        Disciplina disc = (Disciplina) ctx.get(c[4]);
        Professor prof = (Professor) ctx.get(c[5]);
        Curriculo curr = (Curriculo) ctx.get(c[6]);
        if (disc == null || prof == null || curr == null) return null;
        // Usa construtor de reconstrucao (nao valida periodo aberto)
        Turma t = new Turma(
                Integer.parseInt(c[1]), Integer.parseInt(c[2]),
                Boolean.parseBoolean(c[3]), c[0],
                disc, prof, curr, true);
        ctx.put(c[0], t); // indexado pelo codigo da turma
        return t;
    }
}

