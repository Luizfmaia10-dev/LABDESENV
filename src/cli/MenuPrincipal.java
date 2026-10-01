package cli;
import domain.*;
/**
 * Menu raiz da aplicaÃ§Ã£o CLI.
 * Roteia a entrada do usuÃ¡rio para os sub-menus especializados.
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
                case 10 -> new MenuConta(ctx).executar(ctx.secretariaAtiva);
                case 0  -> continuar = false;
                default -> System.out.println("  [!] Opcao invalida. Tente novamente.");
            }
        }
    }

    private void exibirMenu() {
        System.out.println();
        System.out.println("â•”â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•—");
        System.out.println("â•‘              MENU PRINCIPAL                  â•‘");
        System.out.println("â• â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•£");
        System.out.println("â•‘  [1] Gerenciar Cursos                        â•‘");
        System.out.println("â•‘  [2] Gerenciar Disciplinas                   â•‘");
        System.out.println("â•‘  [3] Gerenciar Curriculos / Periodo          â•‘");
        System.out.println("â•‘  [4] Gerenciar Secretaria                    â•‘");
        System.out.println("â•‘  [5] Gerenciar Professores                   â•‘");
        System.out.println("â•‘  [6] Gerenciar Alunos                        â•‘");
        System.out.println("â•‘  [7] Gerenciar Turmas                        â•‘");
        System.out.println("â•‘  [8] Matriculas e Cancelamentos              â•‘");
        System.out.println("â•‘  [9] Historico e Cobrancas                   â•‘");
        System.out.println("â•‘  [10] Minha conta                            â•‘");
        System.out.println("â•‘  [0] Sair                                    â•‘");
        System.out.println("â•šâ•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•");
    }
}

