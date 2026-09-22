import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Turma {
    private int limiteMaximo;
    private int limiteMinimo;
    private boolean isAtiva;
    private String codigo;
    private final Disciplina disciplina;
    private final Professor professor;
    private final Curriculo curriculo;
    private final List<Inscricao> inscricoes = new ArrayList<>();

    public Turma(int limiteMaximo, int limiteMinimo, boolean isAtiva, String codigo,
                 Disciplina disciplina, Professor professor, Curriculo curriculo) {
        validarLimites(limiteMinimo, limiteMaximo);
        this.disciplina = Objects.requireNonNull(disciplina, "Disciplina obrigatoria");
        this.professor = Objects.requireNonNull(professor, "Professor obrigatorio");
        this.curriculo = Objects.requireNonNull(curriculo, "Curriculo obrigatorio");
        this.codigo = Objects.requireNonNull(codigo);
        if (isAtiva && !curriculo.isPeriodoMatriculaAberto()) {
            throw new IllegalStateException("Nova turma sem alunos exige periodo aberto");
        }
        this.limiteMaximo = limiteMaximo;
        this.limiteMinimo = limiteMinimo;
        this.isAtiva = isAtiva;
        disciplina.registrarTurma(this);
        professor.registrarTurma(this);
    }

    private static void validarLimites(int minimo, int maximo) {
        if (minimo < 3 || maximo > 60 || minimo > maximo) {
            throw new IllegalArgumentException("Limites devem respeitar 3 <= minimo <= maximo <= 60");
        }
    }
    public int getLimiteMaximo() { return limiteMaximo; }
    public void setLimiteMaximo(int limiteMaximo) {
        validarLimites(limiteMinimo, limiteMaximo);
        if (getAlunos().size() > limiteMaximo) throw new IllegalStateException("Existem mais alunos inscritos");
        this.limiteMaximo = limiteMaximo;
    }
    public int getLimiteMinimo() { return limiteMinimo; }
    public void setLimiteMinimo(int limiteMinimo) {
        validarLimites(limiteMinimo, limiteMaximo);
        if (isAtiva && !curriculo.isPeriodoMatriculaAberto() && getAlunos().size() < limiteMinimo) {
            throw new IllegalStateException("Turma ativa nao atende ao novo minimo");
        }
        this.limiteMinimo = limiteMinimo;
    }
    public boolean isAtiva() { return isAtiva; }
    public boolean isTurmaAtiva() { return isAtiva; }
    public void setAtiva(boolean ativa) {
        if (ativa && !curriculo.isPeriodoMatriculaAberto() && getAlunos().size() < limiteMinimo) {
            throw new IllegalStateException("Turma nao atende ao minimo");
        }
        isAtiva = ativa;
        if (!ativa) for (Inscricao inscricao : inscricoes) inscricao.desativar();
    }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = Objects.requireNonNull(codigo); }
    public Disciplina getDisciplina() { return disciplina; }
    public Professor getProfessor() { return professor; }
    public Curriculo getCurriculo() { return curriculo; }
    public List<Inscricao> getInscricoes() { return Collections.unmodifiableList(inscricoes); }
    public List<Aluno> getAlunos() {
        return inscricoes.stream().filter(Inscricao::isAtiva).map(Inscricao::getAluno).toList();
    }

    void registrarInscricao(Inscricao inscricao) {
        if (inscricao.getTurma() != this) throw new IllegalArgumentException("Turma diferente");
        if (!inscricoes.contains(inscricao)) inscricoes.add(inscricao);
    }
    void validarVaga() {
        if (!isAtiva) throw new IllegalStateException("Turma inativa");
        if (getAlunos().size() >= limiteMaximo) throw new IllegalStateException("Turma lotada");
    }

    /** Consolida a oferta apos o periodo oficial e cancela inscricoes de turmas inviaveis. */
    public void fecharTurma() {
        if (curriculo.isPeriodoMatriculaAberto()) {
            throw new IllegalStateException("Periodo de matricula ainda aberto");
        }
        setAtiva(getAlunos().size() >= limiteMinimo);
    }
}
