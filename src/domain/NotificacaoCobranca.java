package domain;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Registro estruturado de um evento financeiro disparado pela Inscricao.
 * Substitui o registro em String puro do modelo anterior.
 * O metodo toString() preserva o formato legivel original para compatibilidade.
 */
public final class NotificacaoCobranca {

    private final TipoNotificacao tipo;
    private final LocalDateTime dataHora;
    private final String matriculaAluno;
    private final String codigoTurma;
    private final String semestre;
    private final String detalhes;

    public NotificacaoCobranca(TipoNotificacao tipo, String matriculaAluno,
                               String codigoTurma, String semestre, String detalhes) {
        this.tipo = Objects.requireNonNull(tipo, "Tipo obrigatorio");
        this.matriculaAluno = Objects.requireNonNull(matriculaAluno, "Matricula obrigatoria");
        this.codigoTurma = Objects.requireNonNull(codigoTurma, "Codigo da turma obrigatorio");
        this.semestre = Objects.requireNonNull(semestre, "Semestre obrigatorio");
        this.detalhes = detalhes != null ? detalhes : "";
        this.dataHora = LocalDateTime.now();
    }

    public TipoNotificacao getTipo()        { return tipo; }
    public LocalDateTime getDataHora()      { return dataHora; }
    public String getMatriculaAluno()       { return matriculaAluno; }
    public String getCodigoTurma()          { return codigoTurma; }
    public String getSemestre()             { return semestre; }
    public String getDetalhes()             { return detalhes; }

    /**
     * Formato compativel com o modelo anterior:
     * "matricula | turma | semestre | TIPO"
     */
    @Override
    public String toString() {
        return matriculaAluno + " | " + codigoTurma + " | " + semestre + " | " + tipo.name();
    }
}

