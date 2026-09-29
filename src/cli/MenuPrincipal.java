/**
 * Menu raiz da aplicação CLI.
 * Roteia a entrada do usuário para os sub-menus especializados.
 */
public class MenuPrincipal {

    private final Contexto ctx;
    private final MenuSecretaria menuSecretaria;
    private final MenuProfessor  menuProfessor;
    private final MenuAluno      menuAluno;

    public MenuPrincipal(Contexto ctx) {
        this.ctx            = ctx;
        this.menuSecretaria = new MenuSecretaria(ctx);
        this.menuProfessor  = new MenuProfessor(ctx);
        this.menuAluno      = new MenuAluno(ctx);
    }

    public void executar() {
        boolean continuar = true;
        while (continuar) {
            exibirMenu();
            int opcao = ctx.lerInt("  Opcao: ");
            System.out.println();
            switch (opcao) {
                case 1  -> menuSecretaria.menuCursos();
                case 2  -> menuSecretaria.menuDisciplinas();
                case 3  -> menuSecretaria.menuCurriculos();
                case 4  -> menuSecretaria.menuSecretaria();
                case 5  -> menuProfessor.executar();
                case 6  -> menuAluno.executar();
                case 7  -> menuSecretaria.menuTurmas();
                case 8  -> menuSecretaria.menuMatriculas();
                case 9  -> menuSecretaria.menuHistoricoCobranca();
                case 0  -> continuar = false;
                default -> System.out.println("  [!] Opcao invalida. Tente novamente.");
            }
        }
    }

    private void exibirMenu() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║              MENU PRINCIPAL                  ║");
        System.out.println("╠══════════════════════════════════════════════╣");
        System.out.println("║  [1] Gerenciar Cursos                        ║");
        System.out.println("║  [2] Gerenciar Disciplinas                   ║");
        System.out.println("║  [3] Gerenciar Curriculos / Periodo          ║");
        System.out.println("║  [4] Gerenciar Secretaria                    ║");
        System.out.println("║  [5] Gerenciar Professores                   ║");
        System.out.println("║  [6] Gerenciar Alunos                        ║");
        System.out.println("║  [7] Gerenciar Turmas                        ║");
        System.out.println("║  [8] Matriculas e Cancelamentos              ║");
        System.out.println("║  [9] Historico e Cobrancas                   ║");
        System.out.println("║  [0] Sair                                    ║");
        System.out.println("╚══════════════════════════════════════════════╝");
    }
}
