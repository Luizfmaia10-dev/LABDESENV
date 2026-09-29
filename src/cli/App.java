/**
 * Ponto de entrada da interface de linha de comando (CLI).
 * Instancia o Contexto (com a Secretaria padrão) e inicia o menu principal.
 */
public class App {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║       SISTEMA DE MATRICULA — CLI v1.0        ║");
        System.out.println("╚══════════════════════════════════════════════╝");
        System.out.println("  Secretaria padrao inicializada:");
        System.out.println("  > secretaria@universidade.br  |  Cargo: Secretaria Geral");
        System.out.println();

        Contexto ctx = new Contexto();
        new MenuPrincipal(ctx).executar();

        ctx.scanner.close();
        System.out.println("\nSistema encerrado. Ate logo!");
    }
}
