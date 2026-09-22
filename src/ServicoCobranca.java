import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** Implementacao local: registra eventos e discrimina as inscricoes cobraveis. */
public final class ServicoCobranca {
    private final List<String> notificacoes = new ArrayList<>();

    public void notificarInscricoes(Inscricao inscricao) {
        Objects.requireNonNull(inscricao);
        notificacoes.add(inscricao.getAluno().getMatricula() + " | "
                + inscricao.getTurma().getCodigo() + " | "
                + inscricao.getTurma().getCurriculo().getSemestre() + " | "
                + (inscricao.isAtiva() ? "ATIVA" : "CANCELADA"));
    }

    public List<String> getNotificacoes() { return Collections.unmodifiableList(notificacoes); }

    /** Nao calcula valores: o modelo nao define precos ou politicas financeiras. */
    public List<Inscricao> gerarCobranca(Aluno aluno, String semestre) {
        Objects.requireNonNull(aluno);
        Objects.requireNonNull(semestre);
        return aluno.getInscricoesAtivas().stream()
                .filter(i -> i.getTurma().getCurriculo().getSemestre().equals(semestre)).toList();
    }
}
