package cli;
import domain.*;
/**
 * Sub-menu de operaÃ§Ãµes relacionadas a Professores.
 */
public class MenuProfessor {

    private final Contexto ctx;

    public MenuProfessor(Contexto ctx) {
        this.ctx = ctx;
    }

    public void executar() {
        boolean continuar = true;
        while (continuar) {
            ctx.imprimirSeparador("PROFESSORES");
            System.out.println("  [1] Cadastrar professor");
            System.out.println("  [2] Listar professores");
            System.out.println("  [3] Editar codigo do professor");
            System.out.println("  [4] Validar estrutura do professor");
            System.out.println("  [5] Listar alunos por turma");
            System.out.println("  [0] Voltar");
            int opcao = ctx.lerInt("  Opcao: ");
            System.out.println();
            switch (opcao) {
                case 1  -> cadastrarProfessor();
                case 2  -> listarProfessores();
                case 3  -> editarCodigoProfessor();
                case 4  -> validarEstruturaProfessor();
                case 5  -> listarAlunosPorTurma();
                case 0  -> continuar = false;
                default -> System.out.println("  [!] Opcao invalida.");
            }
            if (continuar) ctx.pausar();
        }
    }

    // â”€â”€â”€ AÃ§Ãµes â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private void cadastrarProfessor() {
        ctx.imprimirSeparador("CADASTRAR PROFESSOR");
        String codigo = ctx.lerLinha("  Codigo do professor: ");
        String email  = ctx.lerLinha("  Email corporativo  : ");
        String senha  = ctx.lerLinha("  Senha              : ");
        String nome   = ctx.lerLinha("  Nome               : ");
        String sobrenome = ctx.lerLinha("  Sobrenome          : ");
        Endereco end  = ctx.lerEndereco();
        try {
            Professor professor = new Professor(codigo, ctx.secretariaAtiva, email, senha, end);
            professor.setNome(nome);
            professor.setSobrenome(sobrenome);
            ctx.professores.add(professor);
            System.out.println("  [OK] Professor cadastrado com sucesso.");
        } catch (Exception e) {
            System.out.println("  [ERRO] " + e.getMessage());
        }
    }

    private void listarProfessores() {
        ctx.imprimirSeparador("LISTA DE PROFESSORES");
        if (ctx.professores.isEmpty()) {
            System.out.println("  Nenhum professor cadastrado.");
            return;
        }
        for (Professor p : ctx.professores) {
            System.out.printf("  Codigo: %-10s | Nome: %s %s | Email: %s | Turmas: %d%n",
                    p.getCodigo(), p.getNome(), p.getSobrenome(),
                    p.getEmailCorporativo(), p.getTurmas().size());
        }
    }

    private void editarCodigoProfessor() {
        ctx.imprimirSeparador("EDITAR PROFESSOR");
        Professor professor = ctx.selecionarProfessor();
        if (professor == null) return;
        String novoCodigo = ctx.lerLinha("  Novo codigo: ");
        try {
            professor.setCodigo(novoCodigo);
            System.out.println("  [OK] Codigo atualizado.");
        } catch (Exception e) {
            System.out.println("  [ERRO] " + e.getMessage());
        }
    }

    private void validarEstruturaProfessor() {
        ctx.imprimirSeparador("VALIDAR PROFESSOR");
        Professor professor = ctx.selecionarProfessor();
        if (professor == null) return;
        try {
            professor.validarEstrutura();
            System.out.println("  [OK] Estrutura valida. Professor possui ao menos uma turma.");
        } catch (Exception e) {
            System.out.println("  [ERRO] " + e.getMessage());
        }
    }

    private void listarAlunosPorTurma() {
        ctx.imprimirSeparador("ALUNOS POR TURMA");
        Professor professor = ctx.selecionarProfessor();
        if (professor == null) return;

        if (professor.getTurmas().isEmpty()) {
            System.out.println("  Professor nao possui turmas.");
            return;
        }
        System.out.println("  Turmas do professor:");
        java.util.List<Turma> turmasDoProfessor = professor.getTurmas();
        for (int i = 0; i < turmasDoProfessor.size(); i++) {
            Turma t = turmasDoProfessor.get(i);
            System.out.printf("    [%d] %s â€” %s%n", i + 1, t.getCodigo(), t.getDisciplina().getNome());
        }
        int idx = ctx.lerInt("  Escolha a turma (numero): ");
        if (idx < 1 || idx > turmasDoProfessor.size()) {
            System.out.println("  [!] Opcao invalida.");
            return;
        }
        Turma turma = turmasDoProfessor.get(idx - 1);
        try {
            java.util.List<Aluno> alunosDaTurma = professor.listarAlunosPorTurma(turma);
            if (alunosDaTurma.isEmpty()) {
                System.out.println("  Nenhum aluno matriculado nesta turma.");
            } else {
                System.out.printf("  Alunos da turma %s (%d):%n", turma.getCodigo(), alunosDaTurma.size());
                for (Aluno a : alunosDaTurma) {
                    System.out.printf("    - %s %s | Matricula: %s%n",
                            a.getNome(), a.getSobrenome(), a.getMatricula());
                }
            }
        } catch (Exception e) {
            System.out.println("  [ERRO] " + e.getMessage());
        }
    }
}

