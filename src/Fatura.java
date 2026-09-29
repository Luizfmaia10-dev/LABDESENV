import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Agrega os itens de cobranca de um aluno para um determinado semestre.
 * O valor total e calculado dinamicamente com base nos itens ativos.
 * Criada e gerenciada exclusivamente pelo ServicoCobranca.
 */
public final class Fatura {

    private final String id;
    private final Aluno aluno;
    private final String semestre;
    private final LocalDate dataEmissao;
    private final LocalDate dataVencimento;
    private final List<ItemFatura> itens = new ArrayList<>();
    private StatusPagamento status;

    Fatura(Aluno aluno, String semestre) {
        this.id = UUID.randomUUID().toString();
        this.aluno = Objects.requireNonNull(aluno, "Aluno obrigatorio");
        this.semestre = Objects.requireNonNull(semestre, "Semestre obrigatorio");
        this.dataEmissao = LocalDate.now();
        this.dataVencimento = dataEmissao.plusDays(30);
        this.status = StatusPagamento.PENDENTE;
    }

    /** Soma os valores de todos os itens ativos desta fatura. */
    public double getValorTotal() {
        return itens.stream()
                .filter(ItemFatura::isAtivo)
                .mapToDouble(ItemFatura::getValor)
                .sum();
    }

    void adicionarItem(ItemFatura item) {
        Objects.requireNonNull(item, "Item obrigatorio");
        itens.add(item);
    }

    /**
     * Localiza e cancela o item correspondente a uma inscricao cancelada.
     * Apos cancelar, verifica se a fatura ficou totalmente vazia para auto-cancelar.
     */
    void removerItemPorInscricao(Inscricao inscricao) {
        Objects.requireNonNull(inscricao, "Inscricao obrigatoria");
        itens.stream()
                .filter(i -> i.getInscricao() == inscricao && i.isAtivo())
                .findFirst()
                .ifPresent(ItemFatura::cancelar);
        recalcular();
    }

    /**
     * Reavalia o status da fatura: se nao ha mais itens ativos, cancela a fatura.
     */
    void recalcular() {
        boolean todosInativos = itens.stream().noneMatch(ItemFatura::isAtivo);
        if (todosInativos && status == StatusPagamento.PENDENTE) {
            status = StatusPagamento.CANCELADO;
        }
    }

    /** Registra o pagamento desta fatura. Somente faturas PENDENTES podem ser pagas. */
    public void registrarPagamento() {
        if (status != StatusPagamento.PENDENTE) {
            throw new IllegalStateException(
                    "Somente faturas PENDENTES podem ser pagas. Status atual: " + status);
        }
        this.status = StatusPagamento.PAGO;
    }

    /** Cancela manualmente a fatura, independente de ter itens ativos. */
    public void cancelar() {
        if (status == StatusPagamento.PAGO) {
            throw new IllegalStateException("Fatura ja paga nao pode ser cancelada");
        }
        itens.forEach(ItemFatura::cancelar);
        this.status = StatusPagamento.CANCELADO;
    }

    public String getId()                       { return id; }
    public Aluno getAluno()                     { return aluno; }
    public String getSemestre()                 { return semestre; }
    public LocalDate getDataEmissao()           { return dataEmissao; }
    public LocalDate getDataVencimento()        { return dataVencimento; }
    public StatusPagamento getStatus()          { return status; }
    public List<ItemFatura> getItens()          { return Collections.unmodifiableList(itens); }
}
