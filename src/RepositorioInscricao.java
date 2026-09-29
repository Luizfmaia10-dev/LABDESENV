import java.time.LocalDate;
import java.util.Map;

/** Persiste e recupera objetos Inscricao em data/inscricoes.csv */
public final class RepositorioInscricao extends RepositorioArquivo<Inscricao> {

    @Override
    protected String getCaminhoArquivo() { return "data/inscricoes.csv"; }

    // formato: tipoInscricao;dataHora;isAtiva;matriculaAluno;codigoTurma
    @Override
    protected String serializar(Inscricao i) {
        return String.join(";",
                i.getTipoInscricao().name(),
                i.getDataHora().toString(),
                String.valueOf(i.isAtiva()),
                esc(i.getAluno().getMatricula()),
                esc(i.getTurma().getCodigo()));
    }

    @Override
    protected Inscricao desserializar(String[] c, Map<String, Object> ctx) {
        if (c.length < 5) return null;
        Aluno aluno = (Aluno) ctx.get(c[3]);
        Turma turma = (Turma) ctx.get(c[4]);
        if (aluno == null || turma == null) return null;
        // Usa construtor de reconstrucao (nao valida nem notifica cobranca)
        return new Inscricao(
                TipoInscricao.valueOf(c[0]),
                LocalDate.parse(c[1]),
                Boolean.parseBoolean(c[2]),
                aluno, turma);
    }
}
