public class Curriculo {
    private String semestre;
    private boolean  isPeriodoMatriculaAberto;

    public Curriculo(String semestre, boolean isPeriodoMatriculaAberto) {
        this.semestre = semestre;
        this.isPeriodoMatriculaAberto = isPeriodoMatriculaAberto;
    }

    public String getSemestre() {
        return semestre;
    }
    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }
    public boolean isPeriodoMatriculaAberto() {
        return isPeriodoMatriculaAberto;
    }
    public void setPeriodoMatriculaAberto(boolean periodoMatriculaAberto) {
        isPeriodoMatriculaAberto = periodoMatriculaAberto;
    }
}
