import java.util.Scanner;

/**
 * Ponto de entrada da aplicacao.
 *
 * Fluxo:
 *  1. Carrega todos os dados do disco (GerenciadorDados)
 *  2. Se nenhuma secretaria estiver cadastrada (primeira execucao), abre o
 *     assistente de configuracao inicial para criar a primeira conta.
 *  3. Exibe tela de login e autentica o usuario
 *  4. Detecta o perfil (Aluno / Professor / Secretaria) e abre o menu correspondente
 *  5. Ao sair, persiste todos os dados de volta no disco
 */
public final class Main {

    public static void main(String[] args) {
        GerenciadorDados dados = new GerenciadorDados();
        dados.carregar();

        Scanner sc = new Scanner(System.in);
        System.out.println("==========================================");
        System.out.println("  Sistema de Matriculas Universitarias  ");
        System.out.println("==========================================");

        // Primeira execucao: nenhuma secretaria cadastrada
        if (dados.getSecretarias().isEmpty()) {
            primeiraExecucao(dados, sc);
        }

        boolean executando = true;
        while (executando) {
            System.out.println("\n1. Login");
            System.out.println("0. Sair");
            System.out.print("> ");
            String opcao = sc.nextLine().trim();

            switch (opcao) {
                case "1" -> autenticar(dados, sc);
                case "0" -> executando = false;
                default  -> System.out.println("Opcao invalida.");
            }
        }

        dados.salvar();
        System.out.println("\nDados salvos. Ate logo!");
        sc.close();
    }

    // -------------------------------------------------------------------------
    // Assistente de primeira execucao
    // -------------------------------------------------------------------------

    private static void primeiraExecucao(GerenciadorDados dados, Scanner sc) {
        System.out.println("\n*** PRIMEIRA EXECUCAO ***");
        System.out.println("Nenhum dado encontrado. Vamos configurar a conta da Secretaria.");
        System.out.println("A Secretaria podera cadastrar professores, alunos e disciplinas depois.\n");

        Secretaria sec = null;
        while (sec == null) {
            try {
                System.out.print("Cargo da secretaria (ex: Atendimento Geral): ");
                String cargo = sc.nextLine().trim();

                System.out.print("Email corporativo: ");
                String email = sc.nextLine().trim();

                System.out.print("Senha: ");
                String senha = sc.nextLine().trim();

                System.out.println("Endereco:");
                System.out.print("  Rua: ");          String rua  = sc.nextLine().trim();
                System.out.print("  Numero: ");        String num  = sc.nextLine().trim();
                System.out.print("  Cidade: ");        String cid  = sc.nextLine().trim();
                System.out.print("  Bairro: ");        String bai  = sc.nextLine().trim();
                System.out.print("  CEP: ");           String cep  = sc.nextLine().trim();
                System.out.print("  Complemento: ");   String comp = sc.nextLine().trim();

                Endereco end = new Endereco(rua, num, cid, bai, cep, comp);
                sec = new Secretaria(cargo, email, senha, end);
                dados.getSecretarias().add(sec);
                dados.salvar();

                System.out.println("\nConta da Secretaria criada com sucesso!");
                System.out.println("Use o email '" + email + "' para fazer login.");
                System.out.println("------------------------------------------");

            } catch (IllegalArgumentException | NullPointerException e) {
                System.out.println("Erro: " + e.getMessage() + " — tente novamente.\n");
            }
        }
    }

    // -------------------------------------------------------------------------
    // Autenticacao
    // -------------------------------------------------------------------------

    private static void autenticar(GerenciadorDados dados, Scanner sc) {
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        System.out.print("Senha: ");
        String senha = sc.nextLine().trim();

        Aluno aluno = dados.buscarAlunoPorEmail(email);
        if (aluno != null && aluno.autenticar(email, senha)) {
            new MenuAluno(aluno, dados, sc).exibir();
            return;
        }

        Professor prof = dados.buscarProfessorPorEmail(email);
        if (prof != null && prof.autenticar(email, senha)) {
            new MenuProfessor(prof, dados, sc).exibir();
            return;
        }

        Secretaria sec = dados.buscarSecretariaPorEmail(email);
        if (sec != null && sec.autenticar(email, senha)) {
            new MenuSecretaria(sec, dados, sc).exibir();
            return;
        }

        System.out.println("Email ou senha incorretos.");
    }
}
