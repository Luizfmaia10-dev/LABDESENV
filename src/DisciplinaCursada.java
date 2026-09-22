public class DisciplinaCursada {
    private String semestre;
    private boolean isAprovado;
    private double notaFinal;
    
    public DisciplinaCursada(String semestre, boolean isAprovado, double notaFinal) {
        this.semestre = semestre;
        this.isAprovado = isAprovado;
        this.notaFinal = notaFinal;
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
        this.notaFinal = notaFinal;
    }

}
