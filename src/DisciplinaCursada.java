import java.util.Objects;

public final class DisciplinaCursada {
    private final Aluno aluno;
    private final Disciplina disciplina;

    public Aluno getAluno() { return aluno; }
    public Disciplina getDisciplina() { return disciplina; }

    private String semestre;
    private boolean isAprovado;
    private double notaFinal;
    
    public DisciplinaCursada(String semestre, boolean isAprovado, double notaFinal,
                            Aluno aluno, Disciplina disciplina) {
        this.aluno = Objects.requireNonNull(aluno, "Aluno obrigatorio");
        this.disciplina = Objects.requireNonNull(disciplina, "Disciplina obrigatoria");
        this.semestre = semestre;
        this.isAprovado = isAprovado;
        if (!Double.isFinite(notaFinal)) throw new IllegalArgumentException("Nota invalida");
        this.notaFinal = notaFinal;
        aluno.registrarHistorico(this);
        disciplina.registrarHistorico(this);
    }
    public String getSemestre() {
        return semestre;
    }
    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }
    public boolean getIsAprovado() {
        return isAprovado;
    }
    public void setAprovado(boolean aprovado) {
        isAprovado = aprovado;
    }
    public double getNotaFinal() {
        return notaFinal;
    }
    public void setNotaFinal(double notaFinal) {
        if (!Double.isFinite(notaFinal)) throw new IllegalArgumentException("Nota invalida");
        this.notaFinal = notaFinal;
    }

}
