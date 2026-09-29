import java.util.Objects;

/**
 * Define os valores monetarios por credito para cada tipo de inscricao.
 * Configurada pela Secretaria antes de gerar faturas.
 * A politica de precos e totalmente parametrizavel — nenhum valor esta fixo no dominio.
 */
public final class TabelaPrecos {

    private double valorCreditoObrigatoria;
    private double valorCreditoOptativa;

    /**
     * @param valorCreditoObrigatoria  preco por credito para disciplinas obrigatorias (ex: 120.0)
     * @param valorCreditoOptativa     preco por credito para disciplinas optativas (ex: 100.0)
     */
    public TabelaPrecos(double valorCreditoObrigatoria, double valorCreditoOptativa) {
        setValorCreditoObrigatoria(valorCreditoObrigatoria);
        setValorCreditoOptativa(valorCreditoOptativa);
    }

    /**
     * Calcula o valor monetario de uma inscricao com base no tipo e nos creditos da disciplina.
     * Formula: creditos_da_disciplina x preco_por_credito_do_tipo
     */
    public double calcularValorInscricao(Inscricao inscricao) {
        Objects.requireNonNull(inscricao, "Inscricao obrigatoria");
        int creditos = inscricao.getTurma().getDisciplina().getCreditos();
        double precoPorCredito = inscricao.getTipoInscricao() == TipoInscricao.OBRIGATORIA
                ? valorCreditoObrigatoria
                : valorCreditoOptativa;
        return creditos * precoPorCredito;
    }

    public double getValorCreditoObrigatoria() { return valorCreditoObrigatoria; }
    public double getValorCreditoOptativa()    { return valorCreditoOptativa; }

    public void setValorCreditoObrigatoria(double valor) {
        if (valor < 0) throw new IllegalArgumentException("Valor por credito nao pode ser negativo");
        this.valorCreditoObrigatoria = valor;
    }

    public void setValorCreditoOptativa(double valor) {
        if (valor < 0) throw new IllegalArgumentException("Valor por credito nao pode ser negativo");
        this.valorCreditoOptativa = valor;
    }
}
