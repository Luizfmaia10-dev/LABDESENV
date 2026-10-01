package cli;
import domain.*;
import java.util.List;

public class PortalAluno {
    private final Contexto ctx;

    public PortalAluno(Contexto ctx) {
        this.ctx = ctx;
    }

    public void executar() {
        boolean continuar = true;
        while (continuar) {
            Aluno aluno = ctx.alunoAtivo;
            ctx.imprimirSeparador("PORTAL DO ALUNO - " + aluno.getMatricula());
            System.out.println("  Bem-vindo, " + (aluno.getNome() == null ? aluno.getMatricula() : aluno.getNome()) + "!");
            System.out.println("  [1] Consultar ofertas de turma");
            System.out.println("  [2] Matricular-se em turma");
            System.out.println("  [3] Minhas inscricoes");
            System.out.println("  [4] Cancelar inscricao");
            System.out.println("  [5] Trancar matricula");
            System.out.println("  [6] Minha conta");
            System.out.println("  [0] Sair");
            int opcao = ctx.lerInt("  Opcao: ");
            System.out.println();
            switch (opcao) {
                case 1 -> listarOfertas();
                case 2 -> matricular();
                case 3 -> listarInscricoes();
                case 4 -> cancelarInscricao();
                case 5 -> trancarMatricula();
                case 6 -> new MenuConta(ctx).executar(aluno);
                case 0 -> continuar = false;
                default -> System.out.println("  [!] Opcao invalida.");
            }
            if (continuar) ctx.pausar();
        }
    }

    private List<Turma> ofertasDisponiveis() {
        return ctx.turmas.stream()
                .filter(Turma::isAtiva)
                .filter(t -> t.getCurriculo().isPeriodoMatriculaAberto())
                .filter(t -> t.getDisciplina().getCurso() == ctx.alunoAtivo.getCurso())
                .filter(t -> t.getAlunos().size() < t.getLimiteMaximo())
                .toList();
    }

    private void listarOfertas() {
        ctx.imprimirSeparador("OFERTAS DISPONIVEIS");
        List<Turma> ofertas = ofertasDisponiveis();
        if (ofertas.isEmpty()) {
            System.out.println("  Nenhuma turma disponivel para seu curso.");
            return;
        }
        for (Turma turma : ofertas) {
            System.out.printf("  %s | %s | Semestre: %s | Vagas: %d/%d%n",
                    turma.getCodigo(), turma.getDisciplina().getNome(),
                    turma.getCurriculo().getSemestre(), turma.getAlunos().size(), turma.getLimiteMaximo());
        }
    }

    private void matricular() {
        List<Turma> ofertas = ofertasDisponiveis();
        if (ofertas.isEmpty()) {
            System.out.println("  Nenhuma turma disponivel para matricula.");
            return;
        }
        for (int i = 0; i < ofertas.size(); i++) {
            Turma turma = ofertas.get(i);
            System.out.printf("  [%d] %s | %s | %s%n", i + 1, turma.getCodigo(),
                    turma.getDisciplina().getNome(), turma.getCurriculo().getSemestre());
        }
        int indice = ctx.lerInt("  Turma: ");
        if (indice < 1 || indice > ofertas.size()) {
            System.out.println("  [!] Opcao invalida.");
            return;
        }
        System.out.println("  [1] Obrigatoria (limite 4 por semestre)");
        System.out.println("  [2] Optativa (limite 2 por semestre)");
        int tipoOpcao = ctx.lerInt("  Tipo: ");
        if (tipoOpcao != 1 && tipoOpcao != 2) {
            System.out.println("  [!] Tipo invalido.");
            return;
        }
        try {
            TipoInscricao tipo = tipoOpcao == 1 ? TipoInscricao.OBRIGATORIA : TipoInscricao.OPTATIVA;
            Inscricao inscricao = ctx.matricular(ctx.alunoAtivo, ofertas.get(indice - 1), tipo);
            System.out.println("  [OK] Matricula realizada: " + inscricao.getTurma().getCodigo());
        } catch (Exception e) {
            System.out.println("  [ERRO] " + e.getMessage());
        }
    }

    private void listarInscricoes() {
        ctx.imprimirSeparador("MINHAS INSCRICOES ATIVAS");
        List<Inscricao> inscricoes = ctx.alunoAtivo.getInscricoesAtivas();
        if (inscricoes.isEmpty()) {
            System.out.println("  Nenhuma inscricao ativa.");
            return;
        }
        for (Inscricao inscricao : inscricoes) {
            Turma turma = inscricao.getTurma();
            System.out.printf("  %s | %s | %s | %s%n", turma.getCodigo(),
                    turma.getDisciplina().getNome(), turma.getCurriculo().getSemestre(),
                    inscricao.getTipoInscricao());
        }
    }

    private void cancelarInscricao() {
        Inscricao inscricao = ctx.selecionarInscricaoAtiva(ctx.alunoAtivo);
        if (inscricao == null) return;
        try {
            ctx.alunoAtivo.cancelarMatricula(inscricao);
            System.out.println("  [OK] Inscricao cancelada.");
        } catch (Exception e) {
            System.out.println("  [ERRO] " + e.getMessage());
        }
    }

    private void trancarMatricula() {
        try {
            ctx.alunoAtivo.trancarMatricula();
            System.out.println("  [OK] Matricula trancada e inscricoes ativas canceladas.");
        } catch (Exception e) {
            System.out.println("  [ERRO] " + e.getMessage());
        }
    }
}
