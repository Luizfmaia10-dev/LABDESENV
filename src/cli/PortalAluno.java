package cli;
import domain.*;

public class PortalAluno {
    private final Contexto ctx;

    public PortalAluno(Contexto ctx) {
        this.ctx = ctx;
    }

    public void executar() {
        ctx.imprimirSeparador("PORTAL DO ALUNO - " + ctx.alunoAtivo.getMatricula());
        System.out.println("  Bem-vindo, " + ctx.alunoAtivo.getNome() + "!");
        System.out.println("  [Funcionalidade em desenvolvimento pelo grupo...]");
        ctx.pausar();
    }
}
