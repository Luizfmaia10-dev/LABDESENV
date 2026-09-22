import java.time.LocalDate;
import java.util.Objects;

/** Classe associativa entre um aluno e uma turma. */
public final class Inscricao {
    private final TipoInscricao tipoInscricao;
    private final LocalDate dataHora;
    private boolean isAtiva;
    private final Aluno aluno;
    private final Turma turma;
    private final ServicoCobranca servicoCobranca;

    public Inscricao(TipoInscricao tipoInscricao, LocalDate dataHora, Aluno aluno,
                     Turma turma, ServicoCobranca servicoCobranca) {
        this.tipoInscricao = Objects.requireNonNull(tipoInscricao);
        this.dataHora = Objects.requireNonNull(dataHora);
        this.aluno = Objects.requireNonNull(aluno, "Aluno obrigatorio");
        this.turma = Objects.requireNonNull(turma, "Turma obrigatoria");
        this.servicoCobranca = Objects.requireNonNull(servicoCobranca, "Servico de cobranca obrigatorio");
        aluno.validarMatricula(turma, tipoInscricao);
        isAtiva = true;
        aluno.registrarInscricao(this);
        turma.registrarInscricao(this);
        enviarNotificaoAoSistemaCobranca();
    }

    public TipoInscricao getTipoInscricao() { return tipoInscricao; }
    public LocalDate getDataHora() { return dataHora; }
    public boolean isAtiva() { return isAtiva; }
    public Aluno getAluno() { return aluno; }
    public Turma getTurma() { return turma; }

    public void setAtiva(boolean ativa) {
        if (ativa == isAtiva) return;
        if (!ativa) {
            aluno.cancelarMatricula(this);
        } else {
            aluno.validarMatricula(turma, tipoInscricao);
            isAtiva = true;
            enviarNotificaoAoSistemaCobranca();
        }
    }

    void desativar() {
        if (!isAtiva) return;
        isAtiva = false;
        enviarNotificaoAoSistemaCobranca();
    }

    /** Nome preservado conforme o diagrama fornecido. */
    public void enviarNotificaoAoSistemaCobranca() {
        servicoCobranca.notificarInscricoes(this);
    }
}
