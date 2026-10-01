package cli;
import domain.*;
/**
 * Sub-menu de operaÃ§Ãµes relacionadas a Alunos.
 */
public class MenuAluno {

    private final Contexto ctx;

    public MenuAluno(Contexto ctx) {
        this.ctx = ctx;
    }

    public void executar() {
        boolean continuar = true;
        while (continuar) {
            ctx.imprimirSeparador("ALUNOS");
            System.out.println("  [1] Cadastrar aluno");
            System.out.println("  [2] Listar alunos");
            System.out.println("  [3] Alterar status do aluno");
            System.out.println("  [4] Trancar matricula");
            System.out.println("  [5] Exibir historico escolar");
            System.out.println("  [0] Voltar");
            int opcao = ctx.lerInt("  Opcao: ");
            System.out.println();
            switch (opcao) {
                case 1  -> cadastrarAluno();
                case 2  -> listarAlunos();
                case 3  -> alterarStatusAluno();
                case 4  -> trancarMatricula();
                case 5  -> exibirHistorico();
                case 0  -> continuar = false;
                default -> System.out.println("  [!] Opcao invalida.");
            }
            if (continuar) ctx.pausar();
        }
    }

    // â”€â”€â”€ AÃ§Ãµes â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private void cadastrarAluno() {
        ctx.imprimirSeparador("CADASTRAR ALUNO");
        String matricula = ctx.lerLinha("  Numero de matricula: ");
        System.out.println("  Status inicial:");
        System.out.println("    [1] ATIVA");
        System.out.println("    [2] TRANCADA");
        System.out.println("    [3] ALUNO_FORMADO");
        int statusOpcao = ctx.lerInt("  Escolha: ");
        StatusMatricula status = switch (statusOpcao) {
            case 2  -> StatusMatricula.TRANCADA;
            case 3  -> StatusMatricula.ALUNO_FORMADO;
            default -> StatusMatricula.ATIVA;
        };
        System.out.println("  Selecione o curso do aluno:");
        Curso curso = ctx.selecionarCurso();
        if (curso == null) return;
        String email    = ctx.lerLinha("  Email corporativo: ");
        String senha    = ctx.lerLinha("  Senha            : ");
        String nome     = ctx.lerLinha("  Nome             : ");
        String sobrenome = ctx.lerLinha("  Sobrenome        : ");
        Endereco end    = ctx.lerEndereco();
        try {
            if (!ctx.dados.emailDisponivel(email, null)) throw new IllegalArgumentException("Email ja cadastrado");
            Aluno aluno = new Aluno(matricula, status, curso, ctx.secretariaAtiva, email, senha, end);
            aluno.setNome(nome);
            aluno.setSobrenome(sobrenome);
            ctx.alunos.add(aluno);
            System.out.println("  [OK] Aluno cadastrado com sucesso.");
        } catch (Exception e) {
            System.out.println("  [ERRO] " + e.getMessage());
        }
    }

    private void listarAlunos() {
        ctx.imprimirSeparador("LISTA DE ALUNOS");
        if (ctx.alunos.isEmpty()) {
            System.out.println("  Nenhum aluno cadastrado.");
            return;
        }
        for (Aluno a : ctx.alunos) {
            System.out.printf("  Matricula: %-8s | Nome: %-20s | Curso: %-15s | Status: %s%n",
                    a.getMatricula(),
                    (a.getNome() == null ? "" : a.getNome()) + " " + (a.getSobrenome() == null ? "" : a.getSobrenome()),
                    a.getCurso().getNome(),
                    a.getStatus());
        }
    }

    private void alterarStatusAluno() {
        ctx.imprimirSeparador("ALTERAR STATUS DO ALUNO");
        Aluno aluno = ctx.selecionarAluno();
        if (aluno == null) return;
        System.out.println("  Novo status:");
        System.out.println("    [1] ATIVA");
        System.out.println("    [2] TRANCADA");
        System.out.println("    [3] ALUNO_FORMADO");
        int opcao = ctx.lerInt("  Escolha: ");
        StatusMatricula novoStatus = switch (opcao) {
            case 2  -> StatusMatricula.TRANCADA;
            case 3  -> StatusMatricula.ALUNO_FORMADO;
            default -> StatusMatricula.ATIVA;
        };
        try {
            aluno.setStatus(novoStatus);
            System.out.println("  [OK] Status atualizado para: " + novoStatus);
        } catch (Exception e) {
            System.out.println("  [ERRO] " + e.getMessage());
        }
    }

    private void trancarMatricula() {
        ctx.imprimirSeparador("TRANCAR MATRICULA");
        Aluno aluno = ctx.selecionarAluno();
        if (aluno == null) return;
        try {
            aluno.trancarMatricula();
            System.out.println("  [OK] Matricula do aluno trancada. Inscricoes ativas canceladas.");
        } catch (Exception e) {
            System.out.println("  [ERRO] " + e.getMessage());
        }
    }

    private void exibirHistorico() {
        ctx.imprimirSeparador("HISTORICO ESCOLAR");
        Aluno aluno = ctx.selecionarAluno();
        if (aluno == null) return;
        System.out.printf("  Aluno: %s %s | Matricula: %s%n",
                aluno.getNome(), aluno.getSobrenome(), aluno.getMatricula());
        if (aluno.getHistorico().isEmpty()) {
            System.out.println("  Nenhuma disciplina cursada registrada.");
            return;
        }
        System.out.println();
        System.out.printf("  %-20s %-10s %-10s %s%n", "Disciplina", "Semestre", "Nota", "Situacao");
        System.out.println("  " + "-".repeat(55));
        for (DisciplinaCursada dc : aluno.getHistorico()) {
            System.out.printf("  %-20s %-10s %-10.1f %s%n",
                    dc.getDisciplina().getNome(),
                    dc.getSemestre(),
                    dc.getNotaFinal(),
                    dc.getIsAprovado() ? "APROVADO" : "REPROVADO");
        }
    }
}

