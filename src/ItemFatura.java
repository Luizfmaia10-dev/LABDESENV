import java.util.Objects;

/**
 * Representa um item individual de uma fatura — uma inscricao cobrada.
 * Pode ser desativado (cancelado) quando a inscricao correspondente e cancelada.
 */
public final class ItemFatura {

    private final Inscricao inscricao;
    private final String descricao;
    private final double valor;
    private boolean ativo;

    ItemFatura(Inscricao inscricao, double valor) {
        this.inscricao = Objects.requireNonNull(inscricao, "Inscricao obrigatoria");
        if (valor < 0) throw new IllegalArgumentException("Valor do item nao pode ser negativo");
        this.valor = valor;
        this.descricao = inscricao.getTurma().getDisciplina().getNome()
                + " — " + inscricao.getTurma().getCodigo()
                + " [" + inscricao.getTipoInscricao().name() + "]";
        this.ativo = true;
    }

    /** Cancela este item, removendo-o do calculo do valor total da fatura. */
    public void cancelar() { this.ativo = false; }

    public Inscricao getInscricao() { return inscricao; }
    public String getDescricao()    { return descricao; }
    public double getValor()        { return valor; }
    public boolean isAtivo()        { return ativo; }
}
