import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Subsistema de Cobranca e Integracao Financeira.
 *
 * Responsabilidades:
 *  - Receber notificacoes de eventos de matricula (criacao, cancelamento, reativacao)
 *  - Gerar e gerenciar faturas para alunos por semestre
 *  - Calcular valores monetarios usando a TabelaPrecos configurada pela Secretaria
 *
 * O construtor padrao (sem argumentos) usa precos zerados — a Secretaria deve
 * configurar a TabelaPrecos antes de gerar faturas.
 * O construtor parametrizado aceita uma TabelaPrecos pre-configurada.
 */
public final class ServicoCobranca {

    private final List<NotificacaoCobranca> notificacoes = new ArrayList<>();
    private final List<Fatura> faturas = new ArrayList<>();
    private final TabelaPrecos tabelaPrecos;

    /** Construtor padrao — precos zerados. Secretaria deve configurar via getTabelaPrecos(). */
    public ServicoCobranca() {
        this.tabelaPrecos = new TabelaPrecos(0.0, 0.0);
    }

    /** Construtor parametrizado — recebe TabelaPrecos pre-configurada. */
    public ServicoCobranca(TabelaPrecos tabelaPrecos) {
        this.tabelaPrecos = Objects.requireNonNull(tabelaPrecos, "TabelaPrecos obrigatoria");
    }

    // -------------------------------------------------------------------------
    // Notificacoes de eventos
    // -------------------------------------------------------------------------

    /**
     * Registra um evento financeiro originado pela Inscricao.
     * Determina automaticamente o tipo da notificacao com base no estado atual da inscricao
     * e no historico registrado (reativacao vs. criacao).
     */
    public void notificarInscricoes(Inscricao inscricao) {
        Objects.requireNonNull(inscricao, "Inscricao obrigatoria");

        TipoNotificacao tipo;
        if (!inscricao.isAtiva()) {
            tipo = TipoNotificacao.INSCRICAO_CANCELADA;
            // Propaga o cancelamento para faturas existentes
            faturas.stream()
                    .filter(f -> f.getAluno() == inscricao.getAluno()
                            && f.getSemestre().equals(inscricao.getTurma().getCurriculo().getSemestre()))
                    .forEach(f -> f.removerItemPorInscricao(inscricao));
        } else {
            // Se ja existe uma notificacao de criacao para esta inscricao, e uma reativacao
            boolean jaNotificada = notificacoes.stream()
                    .anyMatch(n -> n.getCodigoTurma().equals(inscricao.getTurma().getCodigo())
                            && n.getMatriculaAluno().equals(inscricao.getAluno().getMatricula())
                            && n.getTipo() == TipoNotificacao.INSCRICAO_CRIADA);
            tipo = jaNotificada ? TipoNotificacao.INSCRICAO_REATIVADA : TipoNotificacao.INSCRICAO_CRIADA;
        }

        notificacoes.add(new NotificacaoCobranca(
                tipo,
                inscricao.getAluno().getMatricula(),
                inscricao.getTurma().getCodigo(),
                inscricao.getTurma().getCurriculo().getSemestre(),
                inscricao.getTipoInscricao().name()
        ));
    }

    public List<NotificacaoCobranca> getNotificacoes() {
        return Collections.unmodifiableList(notificacoes);
    }

    // -------------------------------------------------------------------------
    // Cobranca / Faturamento
    // -------------------------------------------------------------------------

    /**
     * Retorna as inscricoes ativas de um aluno em um semestre.
     * Mantido para compatibilidade com o modelo anterior.
     */
    public List<Inscricao> gerarCobranca(Aluno aluno, String semestre) {
        Objects.requireNonNull(aluno, "Aluno obrigatorio");
        Objects.requireNonNull(semestre, "Semestre obrigatorio");
        return aluno.getInscricoesAtivas().stream()
                .filter(i -> i.getTurma().getCurriculo().getSemestre().equals(semestre))
                .toList();
    }

    /**
     * Cria e armazena uma nova Fatura para o aluno no semestre informado,
     * populada com um ItemFatura para cada inscricao ativa do semestre.
     * O valor de cada item e calculado pela TabelaPrecos configurada.
     */
    public Fatura gerarFatura(Aluno aluno, String semestre) {
        Objects.requireNonNull(aluno, "Aluno obrigatorio");
        Objects.requireNonNull(semestre, "Semestre obrigatorio");

        Fatura fatura = new Fatura(aluno, semestre);
        gerarCobranca(aluno, semestre).forEach(inscricao -> {
            double valor = tabelaPrecos.calcularValorInscricao(inscricao);
            fatura.adicionarItem(new ItemFatura(inscricao, valor));
        });
        faturas.add(fatura);
        return fatura;
    }

    /** Retorna todas as faturas de um aluno, independente do semestre. */
    public List<Fatura> consultarFaturas(Aluno aluno) {
        Objects.requireNonNull(aluno, "Aluno obrigatorio");
        return faturas.stream()
                .filter(f -> f.getAluno() == aluno)
                .toList();
    }

    /** Retorna as faturas de um aluno filtradas por semestre. */
    public List<Fatura> consultarFaturasPorSemestre(Aluno aluno, String semestre) {
        Objects.requireNonNull(aluno, "Aluno obrigatorio");
        Objects.requireNonNull(semestre, "Semestre obrigatorio");
        return faturas.stream()
                .filter(f -> f.getAluno() == aluno && f.getSemestre().equals(semestre))
                .toList();
    }

    /** Acesso a TabelaPrecos para que a Secretaria possa reconfigurar os precos. */
    public TabelaPrecos getTabelaPrecos() { return tabelaPrecos; }
}
