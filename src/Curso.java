public class Curso {
    private String nome;
    private String codigo;
    private int creditoTotais;

    public Curso(String nome, String codigo, int creditoTotais) {
        this.nome = nome;
        this.codigo = codigo;
        this.creditoTotais = creditoTotais;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getCreditoTotais() {
        return creditoTotais;
    }

    public void setCreditoTotais(int creditoTotais) {
        this.creditoTotais = creditoTotais;
    }
}
