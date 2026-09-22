import java.util.Date;
import java.util.UUID;

public abstract class Pessoa {
    private Endereco endereco;

    // Endereco apenas guarda a referencia; nao chama metodos da pessoa em construcao.
    @SuppressWarnings("this-escape")
    protected Pessoa(Endereco endereco) {
        this.idPessoa = UUID.randomUUID();
        setEndereco(endereco);
    }

    public Endereco getEndereco() { return endereco; }

    public final void setEndereco(Endereco novoEndereco) {
        java.util.Objects.requireNonNull(novoEndereco, "Endereco obrigatorio");
        novoEndereco.vincularPessoa(this);
        if (endereco != null && endereco != novoEndereco) endereco.vincularPessoa(null);
        endereco = novoEndereco;
    }

    public UUID getIdPessoa() { return idPessoa; }

    private UUID idPessoa;
    private String nome;
    private String sobrenome;
    private Date dataNascimento;
    private String telefone;
    private String email;

    protected Pessoa(UUID id, String nome, String sobrenome, Date dataNascimento, String telefone, String email, Endereco endereco) {
        this(validarId(id, endereco));
        this.idPessoa = java.util.Objects.requireNonNull(id);
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.dataNascimento = dataNascimento == null ? null : new Date(dataNascimento.getTime());
        this.telefone = telefone;
        this.email = email;
    }
    private static Endereco validarId(UUID id, Endereco endereco) {
        java.util.Objects.requireNonNull(id, "Id obrigatorio");
        return endereco;
    }

    public UUID getId() {
        return idPessoa;
    }
    public void setId(UUID id) {
        this.idPessoa = java.util.Objects.requireNonNull(id);
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getSobrenome() {
        return sobrenome;
    }
    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }
    public Date getDataNascimento() {
        return dataNascimento == null ? null : new Date(dataNascimento.getTime());
    }
    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento == null ? null : new Date(dataNascimento.getTime());
    }
    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    
}
