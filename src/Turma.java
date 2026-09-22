public class Turma {
    private int limiteMaximo;
    private int limiteMinimo;
    private boolean isAtiva;
    private String codigo;

    public Turma(int limiteMaximo, int limiteMinimo, boolean isAtiva, String codigo) {
        this.limiteMaximo = limiteMaximo;
        this.limiteMinimo = limiteMinimo;
        this.isAtiva = isAtiva;
        this.codigo = codigo;
    }
    public int getLimiteMaximo() {
        return limiteMaximo;
    }
    public void setLimiteMaximo(int limiteMaximo) {
        this.limiteMaximo = limiteMaximo;
    }
    public int getLimiteMinimo() {
        return limiteMinimo;
    }
    public void setLimiteMinimo(int limiteMinimo) {
        this.limiteMinimo = limiteMinimo;
    }
    public boolean isAtiva() {
        return isAtiva;
    }
    public void setAtiva(boolean ativa) {
        isAtiva = ativa;
    }
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public boolean isTurmaAtiva() {
        return isAtiva;
    } 
}
