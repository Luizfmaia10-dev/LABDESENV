package cli;
import domain.*;
import java.util.List;

public class PortalProfessor {
    private final Contexto ctx;

    public PortalProfessor(Contexto ctx) {
        this.ctx = ctx;
    }

    public void executar() {
        boolean continuar = true;
        while (continuar) {
            ctx.imprimirSeparador("PORTAL DO PROFESSOR - " + ctx.professorAtivo.getCodigo());
            System.out.println("  [1] Listar minhas turmas");
            System.out.println("  [2] Consultar alunos matriculados em uma turma");
            System.out.println("  [0] Sair");
            int opcao = ctx.lerInt("  Opcao: ");
            System.out.println();
            switch (opcao) {
                case 1 -> listarTurmas();
                case 2 -> consultarAlunosPorTurma();
                case 0 -> continuar = false;
                default -> System.out.println("  [!] Opcao invalida.");
            }
            if (continuar) ctx.pausar();
        }
    }

    private void listarTurmas() {
        ctx.imprimirSeparador("MINHAS TURMAS");
        List<Turma> turmas = ctx.professorAtivo.getTurmas();
        if (turmas.isEmpty()) { System.out.println("  Nenhuma turma alocada."); return; }
        for (Turma t : turmas) {
            System.out.printf("  - %s - %s | Semestre: %s | Alunos: %d/%d | Ativa: %s%n",
                t.getCodigo(), t.getDisciplina().getNome(), t.getCurriculo().getSemestre(),
                t.getAlunos().size(), t.getLimiteMaximo(), t.isAtiva() ? "Sim" : "Nao");
        }
    }

    private void consultarAlunosPorTurma() {
        ctx.imprimirSeparador("ALUNOS POR TURMA");
        List<Turma> turmas = ctx.professorAtivo.getTurmas();
        if (turmas.isEmpty()) { System.out.println("  Nenhuma turma alocada."); return; }
        for (int i = 0; i < turmas.size(); i++) {
            System.out.printf("  [%d] %s - %s%n", i + 1, turmas.get(i).getCodigo(), turmas.get(i).getDisciplina().getNome());
        }
        int idx = ctx.lerInt("  Escolha a turma (numero): ");
        if (idx < 1 || idx > turmas.size()) { System.out.println("  [!] Opcao invalida."); return; }
        Turma turma = turmas.get(idx - 1);
        List<Aluno> alunos = ctx.professorAtivo.listarAlunosPorTurma(turma);
        if (alunos.isEmpty()) { System.out.println("  Nenhum aluno matriculado."); return; }
        System.out.printf("  Alunos em %s - %s:%n", turma.getCodigo(), turma.getDisciplina().getNome());
        for (Aluno a : alunos) {
            System.out.printf("  - %s | %s%n", a.getMatricula(), a.getEmailCorporativo());
        }
    }
}
