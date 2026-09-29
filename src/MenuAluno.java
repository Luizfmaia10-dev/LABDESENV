import java.util.List;
import java.util.Scanner;

/**
 * Menu do Aluno — permite matricular, cancelar, trancar e consultar inscricoes e faturas.
 */
public final class MenuAluno {

    private final Aluno aluno;
    private final GerenciadorDados dados;
    private final Scanner sc;

    public MenuAluno(Aluno aluno, GerenciadorDados dados, Scanner sc) {
        this.aluno = aluno;
        this.dados = dados;
        this.sc = sc;
    }

    public void exibir() {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n=== Menu do Aluno — " + aluno.getMatricula() + " ===");
            System.out.println("1. Efetuar matricula em disciplina");
            System.out.println("2. Cancelar matricula em disciplina");
            System.out.println("3. Trancar matricula");
            System.out.println("4. Consultar inscricoes ativas");
            System.out.println("5. Consultar historico academico");
            System.out.println("6. Consultar faturas");
            System.out.println("0. Sair");
            System.out.print("> ");
            String opcao = sc.nextLine().trim();
            switch (opcao) {
                case "1" -> efetuarMatricula();
                case "2" -> cancelarMatricula();
                case "3" -> trancarMatricula();
                case "4" -> consultarInscricoes();
                case "5" -> consultarHistorico();
                case "6" -> consultarFaturas();
                case "0" -> continuar = false;
                default -> System.out.println("Opcao invalida.");
            }
        }
    }

    private void efetuarMatricula() {
        List<Turma> disponiveis = dados.getTurmas().stream()
                .filter(t -> t.isAtiva()
                        && t.getCurriculo().isPeriodoMatriculaAberto()
                        && t.getDisciplina().getCurso() == aluno.getCurso())
                .toList();

        if (disponiveis.isEmpty()) {
            System.out.println("Nenhuma turma disponivel para matricula.");
            return;
        }
        System.out.println("\nTurmas disponíveis:");
        for (int i = 0; i < disponiveis.size(); i++) {
            Turma t = disponiveis.get(i);
            System.out.printf("  %d. %s — %s (%d credito(s)) | Prof: %s | Vagas: %d/%d%n",
                    i + 1,
                    t.getCodigo(),
                    t.getDisciplina().getNome(),
                    t.getDisciplina().getCreditos(),
                    t.getProfessor().getCodigo(),
                    t.getAlunos().size(),
                    t.getLimiteMaximo());
        }
        System.out.print("Numero da turma (0 para cancelar): ");
        int idx = lerInt() - 1;
        if (idx < 0 || idx >= disponiveis.size()) { System.out.println("Cancelado."); return; }

        System.out.print("Tipo de inscricao (1=OBRIGATORIA / 2=OPTATIVA): ");
        String t = sc.nextLine().trim();
        TipoInscricao tipo = t.equals("1") ? TipoInscricao.OBRIGATORIA : TipoInscricao.OPTATIVA;

        try {
            Inscricao inscricao = aluno.matricular(disponiveis.get(idx), tipo, dados.getServicoCobranca());
            dados.getInscricoes().add(inscricao);
            System.out.println("Matricula realizada com sucesso!");
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void cancelarMatricula() {
        List<Inscricao> ativas = aluno.getInscricoesAtivas();
        if (ativas.isEmpty()) { System.out.println("Nenhuma inscricao ativa."); return; }

        System.out.println("\nInscricoes ativas:");
        for (int i = 0; i < ativas.size(); i++) {
            Inscricao ins = ativas.get(i);
            System.out.printf("  %d. %s — %s%n", i + 1,
                    ins.getTurma().getCodigo(), ins.getTurma().getDisciplina().getNome());
        }
        System.out.print("Numero da inscricao a cancelar (0 para voltar): ");
        int idx = lerInt() - 1;
        if (idx < 0 || idx >= ativas.size()) { System.out.println("Cancelado."); return; }

        try {
            aluno.cancelarMatricula(ativas.get(idx));
            System.out.println("Matricula cancelada com sucesso!");
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void trancarMatricula() {
        System.out.print("Confirma trancamento da matricula? Todas as inscricoes ativas serao canceladas. (s/n): ");
        if (!sc.nextLine().trim().equalsIgnoreCase("s")) { System.out.println("Operacao cancelada."); return; }
        try {
            aluno.trancarMatricula();
            System.out.println("Matricula trancada com sucesso.");
        } catch (IllegalStateException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void consultarInscricoes() {
        List<Inscricao> ativas = aluno.getInscricoesAtivas();
        if (ativas.isEmpty()) { System.out.println("Nenhuma inscricao ativa."); return; }
        System.out.println("\nInscricoes ativas:");
        ativas.forEach(i -> System.out.printf("  • %s — %s | %s | %s%n",
                i.getTurma().getCodigo(),
                i.getTurma().getDisciplina().getNome(),
                i.getTipoInscricao(),
                i.getTurma().getCurriculo().getSemestre()));
    }

    private void consultarHistorico() {
        List<DisciplinaCursada> hist = aluno.getHistorico();
        if (hist.isEmpty()) { System.out.println("Historico vazio."); return; }
        System.out.println("\nHistorico academico:");
        hist.forEach(h -> System.out.printf("  • %s | %s | Nota: %.1f | %s%n",
                h.getSemestre(),
                h.getDisciplina().getNome(),
                h.getNotaFinal(),
                h.getIsAprovado() ? "APROVADO" : "REPROVADO"));
    }

    private void consultarFaturas() {
        List<Fatura> faturas = dados.getServicoCobranca().consultarFaturas(aluno);
        if (faturas.isEmpty()) { System.out.println("Nenhuma fatura gerada."); return; }
        System.out.println("\nFaturas:");
        faturas.forEach(f -> System.out.printf(
                "  • Semestre: %s | Vencimento: %s | Total: R$ %.2f | Status: %s%n",
                f.getSemestre(), f.getDataVencimento(), f.getValorTotal(), f.getStatus()));
    }

    private int lerInt() {
        try { return Integer.parseInt(sc.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }
}
