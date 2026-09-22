public class Aluno extends Usuario{
    private String matricula;
    private StatusMatricula status;

    public Aluno(String matricula, StatusMatricula status) {
        this.matricula = matricula;
        this.status = status;
    }
    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public StatusMatricula getStatus() {
        return status;
    }

    public void setStatus(StatusMatricula status) {
        this.status = status;
    }
}