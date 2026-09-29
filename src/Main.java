import domain.*;
import cli.*;
import java.util.Scanner;

public final class Main {
    public static void main(String[] args) {
        GerenciadorDados dados = new GerenciadorDados();
        dados.carregar();

        Scanner sc = new Scanner(System.in);
        System.out.println("==========================================");
        System.out.println("  Sistema de Matriculas Universitarias  ");
        System.out.println("==========================================");

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

    private static void primeiraExecucao(GerenciadorDados dados, Scanner sc) {
        System.out.println("\n*** PRIMEIRA EXECUCAO ***");
        System.out.println("Nenhum dado encontrado. Vamos configurar a conta da Secretaria.");
        
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
                System.out.println("\nConta criada com sucesso!");
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage() + " — tente novamente.\n");
            }
        }
    }

    private static void autenticar(GerenciadorDados dados, Scanner sc) {
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        System.out.print("Senha: ");
        String senha = sc.nextLine().trim();

        Aluno aluno = dados.buscarAlunoPorEmail(email);
        if (aluno != null && aluno.autenticar(email, senha)) {
            Contexto ctx = new Contexto(dados);
            ctx.alunoAtivo = aluno;
            new PortalAluno(ctx).executar();
            return;
        }

        Professor prof = dados.buscarProfessorPorEmail(email);
        if (prof != null && prof.autenticar(email, senha)) {
            Contexto ctx = new Contexto(dados);
            ctx.professorAtivo = prof;
            new PortalProfessor(ctx).executar();
            return;
        }

        Secretaria sec = dados.buscarSecretariaPorEmail(email);
        if (sec != null && sec.autenticar(email, senha)) {
            Contexto ctx = new Contexto(dados);
            ctx.secretariaAtiva = sec;
            new MenuPrincipal(ctx).executar();
            return;
        }

        System.out.println("Email ou senha incorretos.");
    }
}
