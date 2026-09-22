import java.util.Objects;

public final class Curriculo {
    private final Secretaria secretaria;

    public Secretaria getSecretaria() { return secretaria; }

    private final String semestre;
    private boolean  isPeriodoMatriculaAberto;

    public Curriculo(String semestre, boolean isPeriodoMatriculaAberto, Secretaria secretaria) {
        this.secretaria = Objects.requireNonNull(secretaria, "Secretaria obrigatoria");
        if (semestre == null || semestre.isBlank()) throw new IllegalArgumentException("Semestre obrigatorio");
        this.semestre = semestre;
        this.isPeriodoMatriculaAberto = isPeriodoMatriculaAberto;
        secretaria.registrarCurriculo(this);
    }

    public String getSemestre() {
        return semestre;
    }
    public boolean isPeriodoMatriculaAberto() {
        return isPeriodoMatriculaAberto;
    }
    public void setPeriodoMatriculaAberto(boolean periodoMatriculaAberto) {
        isPeriodoMatriculaAberto = periodoMatriculaAberto;
    }
}
