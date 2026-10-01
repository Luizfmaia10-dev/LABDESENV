package cli;

import domain.Endereco;
import domain.Usuario;

public final class MenuConta {
    private final Contexto ctx;

    public MenuConta(Contexto ctx) {
        this.ctx = ctx;
    }

    public void executar(Usuario usuario) {
        boolean continuar = true;
        while (continuar) {
            ctx.imprimirSeparador("MINHA CONTA");
            System.out.println("  Email: " + usuario.getEmailCorporativo());
            System.out.println("  [1] Alterar email");
            System.out.println("  [2] Alterar senha");
            System.out.println("  [3] Atualizar endereco");
            System.out.println("  [4] Atualizar dados pessoais");
            System.out.println("  [0] Voltar");
            int opcao = ctx.lerInt("  Opcao: ");
            System.out.println();
            switch (opcao) {
                case 1 -> alterarEmail(usuario);
                case 2 -> alterarSenha(usuario);
                case 3 -> atualizarEndereco(usuario);
                case 4 -> atualizarDadosPessoais(usuario);
                case 0 -> continuar = false;
                default -> System.out.println("  [!] Opcao invalida.");
            }
            if (continuar) ctx.pausar();
        }
    }

    private void alterarEmail(Usuario usuario) {
        String email = ctx.lerLinha("  Novo email: ");
        try {
            if (!ctx.dados.emailDisponivel(email, usuario)) {
                throw new IllegalArgumentException("Email ja cadastrado");
            }
            usuario.setEmailCorporativo(email);
            System.out.println("  [OK] Email atualizado.");
        } catch (Exception e) {
            System.out.println("  [ERRO] " + e.getMessage());
        }
    }

    private void alterarSenha(Usuario usuario) {
        String atual = ctx.lerLinha("  Senha atual: ");
        String nova = ctx.lerLinha("  Nova senha: ");
        try {
            usuario.alterarSenha(atual, nova);
            System.out.println("  [OK] Senha atualizada.");
        } catch (Exception e) {
            System.out.println("  [ERRO] " + e.getMessage());
        }
    }

    private void atualizarEndereco(Usuario usuario) {
        Endereco endereco = ctx.lerEndereco();
        try {
            usuario.setEndereco(endereco);
            System.out.println("  [OK] Endereco atualizado.");
        } catch (Exception e) {
            System.out.println("  [ERRO] " + e.getMessage());
        }
    }

    private void atualizarDadosPessoais(Usuario usuario) {
        String nome = ctx.lerLinha("  Nome (em branco para manter): ");
        String sobrenome = ctx.lerLinha("  Sobrenome (em branco para manter): ");
        String telefone = ctx.lerLinha("  Telefone (em branco para manter): ");
        if (!nome.isBlank()) usuario.setNome(nome);
        if (!sobrenome.isBlank()) usuario.setSobrenome(sobrenome);
        if (!telefone.isBlank()) usuario.setTelefone(telefone);
        System.out.println("  [OK] Dados pessoais atualizados.");
    }
}