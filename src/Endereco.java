import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public final class Endereco {
    private Pessoa pessoa;

    public Pessoa getPessoa() { return pessoa; }

    void vincularPessoa(Pessoa novaPessoa) {
        if (pessoa != null && novaPessoa != null && pessoa != novaPessoa) {
            throw new IllegalStateException("Endereco ja pertence a outra pessoa");
        }
        pessoa = novaPessoa;
    }

    /** Consulta um cadastro local; nao acessa servicos externos de CEP. */
    public static Optional<Endereco> buscarEnderecoPeloCEP(String cep, Collection<Endereco> cadastro) {
        String normalizado = Objects.requireNonNull(cep).replace("-", "");
        return Objects.requireNonNull(cadastro).stream()
                .filter(e -> e.getCEP().replace("-", "").equals(normalizado)).findFirst();
    }

    public void cadastrarEndereco(Pessoa pessoa) {
        Objects.requireNonNull(pessoa).setEndereco(this);
    }

    public void editarEndereco(String rua, String numero, String cidade, String bairro,
                               String cep, String complemento) {
        Objects.requireNonNull(rua);
        Objects.requireNonNull(numero);
        Objects.requireNonNull(cidade);
        Objects.requireNonNull(bairro);
        Objects.requireNonNull(cep);
        this.rua = rua;
        this.numero = numero;
        this.cidade = cidade;
        this.bairro = bairro;
        this.CEP = cep;
        this.complemento = complemento;
    }

    @Override
    public String toString() {
        return rua + ", " + numero + " - " + bairro + ", " + cidade + " - CEP " + CEP
                + (complemento == null || complemento.isBlank() ? "" : " (" + complemento + ")");
    }

    private String rua;
    private String numero;
    private String cidade;
    private String bairro;
    private String CEP;
    private String complemento;

    public Endereco(String rua, String numero, String cidade, String bairro, String CEP, String complemento) {
        this.rua = rua;
        this.numero = numero;
        this.cidade = cidade;
        this.bairro = bairro;
        this.CEP = Objects.requireNonNull(CEP, "CEP obrigatorio");
        this.complemento = complemento;
    }
    public String getRua() {
        return rua;
    }
    public void setRua(String rua) {
        this.rua = rua;
    }
    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
    public String getCidade() {
        return cidade;
    }
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
    public String getBairro() {
        return bairro;
    }
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }
    public String getCEP() {
        return CEP;
    }
    public void setCEP(String CEP) {
        this.CEP = Objects.requireNonNull(CEP, "CEP obrigatorio");
    }
    public String getComplemento() {
        return complemento;
    }
    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }
    


    
}
