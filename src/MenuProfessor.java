import java.util.List;
import java.util.Scanner;

/**
 * Menu do Professor — consulta de turmas e lista de alunos matriculados.
 */
public final class MenuProfessor {

    private final Professor professor;
    private final GerenciadorDados dados;
    private final Scanner sc;

    public MenuProfessor(Professor professor, GerenciadorDados dados, Scanner sc) {
        this.professor = professor;
        this.dados = dados;
        this.sc = sc;
    }

    public void exibir() {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n=== Menu do Professor — " + professor.getCodigo() + " ===");
            System.out.println("1. Listar minhas turmas");
            System.out.println("2. Consultar alunos matriculados em uma turma");
            System.out.println("0. Sair");
            System.out.print("> ");
            switch (sc.nextLine().trim()) {
                case "1" -> listarTurmas();
                case "2" -> consultarAlunosPorTurma();
                case "0" -> continuar = false;
                default -> System.out.println("Opcao invalida.");
            }
        }
    }

    private void listarTurmas() {
        List<Turma> turmas = professor.getTurmas();
        if (turmas.isEmpty()) { System.out.println("Nenhuma turma alocada."); return; }
        System.out.println("\nMinhas turmas:");
        turmas.forEach(t -> System.out.printf(
                "  • %s — %s | Semestre: %s | Alunos: %d/%d | Ativa: %s%n",
                t.getCodigo(),
                t.getDisciplina().getNome(),
                t.getCurriculo().getSemestre(),
                t.getAlunos().size(),
                t.getLimiteMaximo(),
                t.isAtiva() ? "Sim" : "Nao"));
    }

    private void consultarAlunosPorTurma() {
        List<Turma> turmas = professor.getTurmas();
        if (turmas.isEmpty()) { System.out.println("Nenhuma turma alocada."); return; }

        System.out.println("\nSelecione a turma:");
        for (int i = 0; i < turmas.size(); i++) {
            System.out.printf("  %d. %s — %s%n", i + 1,
                    turmas.get(i).getCodigo(), turmas.get(i).getDisciplina().getNome());
        }
        System.out.print("Numero (0 para voltar): ");
        int idx;
        try { idx = Integer.parseInt(sc.nextLine().trim()) - 1; }
        catch (NumberFormatException e) { idx = -1; }
        if (idx < 0 || idx >= turmas.size()) { System.out.println("Cancelado."); return; }

        Turma turma = turmas.get(idx);
        List<Aluno> alunos = professor.listarAlunosPorTurma(turma);
        if (alunos.isEmpty()) { System.out.println("Nenhum aluno matriculado."); return; }

        System.out.printf("%nAlunos em %s — %s:%n", turma.getCodigo(), turma.getDisciplina().getNome());
        alunos.forEach(a -> System.out.printf("  • %s | %s%n",
                a.getMatricula(), a.getEmailCorporativo()));
    }
}
