import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Estado compartilhado da sessão CLI.
 * Centraliza os dados em memória e os utilitários de leitura de entrada.
 * Uma Secretaria padrão é inicializada automaticamente.
 */
public class Contexto {

    public final Scanner scanner = new Scanner(System.in);

    // Coleções em memória da sessão
    public final List<Curso>       cursos       = new ArrayList<>();
    public final List<Aluno>       alunos       = new ArrayList<>();
    public final List<Professor>   professores  = new ArrayList<>();
    public final List<Disciplina>  disciplinas  = new ArrayList<>();
    public final List<Curriculo>   curriculos   = new ArrayList<>();
    public final List<Turma>       turmas       = new ArrayList<>();
    public final ServicoCobranca   servicoCobranca = new ServicoCobranca();

    /** Secretaria padrão inicializada ao abrir a aplicação. */
    public final Secretaria secretariaAtiva;

    public Contexto() {
        Endereco end = new Endereco(
                "Avenida Principal", "1",
                "Belo Horizonte", "Centro",
                "30000-000", "Bloco Administrativo");
        secretariaAtiva = new Secretaria(
                "Secretaria Geral",
                "secretaria@universidade.br",
                "admin123",
                end);
    }

    // ─── Helpers de leitura ───────────────────────────────────────────────────

    public String lerLinha(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public int lerInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String linha = scanner.nextLine().trim();
            try {
                return Integer.parseInt(linha);
            } catch (NumberFormatException e) {
                System.out.println("  [!] Entrada invalida. Digite um numero inteiro.");
            }
        }
    }

    public double lerDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String linha = scanner.nextLine().trim();
            try {
                return Double.parseDouble(linha.replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.println("  [!] Entrada invalida. Digite um numero decimal.");
            }
        }
    }

    /** Lê os 6 campos de um Endereco interativamente. */
    public Endereco lerEndereco() {
        System.out.println("  --- Endereco ---");
        String logradouro   = lerLinha("    Logradouro : ");
        String numero       = lerLinha("    Numero     : ");
        String cidade       = lerLinha("    Cidade     : ");
        String bairro       = lerLinha("    Bairro     : ");
        String cep          = lerLinha("    CEP        : ");
        String complemento  = lerLinha("    Complemento: ");
        return new Endereco(logradouro, numero, cidade, bairro, cep, complemento);
    }

    // ─── Helpers de seleção de entidades ─────────────────────────────────────

    public Curso selecionarCurso() {
        if (cursos.isEmpty()) { System.out.println("  [!] Nenhum curso cadastrado."); return null; }
        System.out.println("  Cursos disponíveis:");
        for (int i = 0; i < cursos.size(); i++) {
            Curso c = cursos.get(i);
            System.out.printf("    [%d] %s (%s)%n", i + 1, c.getNome(), c.getCodigo());
        }
        int idx = lerInt("  Escolha (numero): ");
        if (idx < 1 || idx > cursos.size()) { System.out.println("  [!] Opcao invalida."); return null; }
        return cursos.get(idx - 1);
    }

    public Aluno selecionarAluno() {
        if (alunos.isEmpty()) { System.out.println("  [!] Nenhum aluno cadastrado."); return null; }
        System.out.println("  Alunos disponíveis:");
        for (int i = 0; i < alunos.size(); i++) {
            Aluno a = alunos.get(i);
            System.out.printf("    [%d] %s %s — Matricula: %s — Status: %s%n",
                    i + 1, a.getNome(), a.getSobrenome(), a.getMatricula(), a.getStatus());
        }
        int idx = lerInt("  Escolha (numero): ");
        if (idx < 1 || idx > alunos.size()) { System.out.println("  [!] Opcao invalida."); return null; }
        return alunos.get(idx - 1);
    }

    public Professor selecionarProfessor() {
        if (professores.isEmpty()) { System.out.println("  [!] Nenhum professor cadastrado."); return null; }
        System.out.println("  Professores disponíveis:");
        for (int i = 0; i < professores.size(); i++) {
            Professor p = professores.get(i);
            System.out.printf("    [%d] %s %s — Codigo: %s%n",
                    i + 1, p.getNome(), p.getSobrenome(), p.getCodigo());
        }
        int idx = lerInt("  Escolha (numero): ");
        if (idx < 1 || idx > professores.size()) { System.out.println("  [!] Opcao invalida."); return null; }
        return professores.get(idx - 1);
    }

    public Disciplina selecionarDisciplina() {
        if (disciplinas.isEmpty()) { System.out.println("  [!] Nenhuma disciplina cadastrada."); return null; }
        System.out.println("  Disciplinas disponíveis:");
        for (int i = 0; i < disciplinas.size(); i++) {
            Disciplina d = disciplinas.get(i);
            System.out.printf("    [%d] %s (%s) — Curso: %s%n",
                    i + 1, d.getNome(), d.getCodigo(), d.getCurso().getNome());
        }
        int idx = lerInt("  Escolha (numero): ");
        if (idx < 1 || idx > disciplinas.size()) { System.out.println("  [!] Opcao invalida."); return null; }
        return disciplinas.get(idx - 1);
    }

    public Curriculo selecionarCurriculo() {
        List<Curriculo> lista = secretariaAtiva.getCurriculos();
        if (lista.isEmpty()) { System.out.println("  [!] Nenhum curriculo cadastrado."); return null; }
        System.out.println("  Curriculos disponíveis:");
        for (int i = 0; i < lista.size(); i++) {
            Curriculo c = lista.get(i);
            System.out.printf("    [%d] %s — Periodo: %s%n",
                    i + 1, c.getSemestre(), c.isPeriodoMatriculaAberto() ? "ABERTO" : "FECHADO");
        }
        int idx = lerInt("  Escolha (numero): ");
        if (idx < 1 || idx > lista.size()) { System.out.println("  [!] Opcao invalida."); return null; }
        return lista.get(idx - 1);
    }

    public Turma selecionarTurma() {
        if (turmas.isEmpty()) { System.out.println("  [!] Nenhuma turma cadastrada."); return null; }
        System.out.println("  Turmas disponíveis:");
        for (int i = 0; i < turmas.size(); i++) {
            Turma t = turmas.get(i);
            System.out.printf("    [%d] %s — %s — Semestre: %s — %s%n",
                    i + 1, t.getCodigo(), t.getDisciplina().getNome(),
                    t.getCurriculo().getSemestre(),
                    t.isAtiva() ? "ATIVA" : "INATIVA");
        }
        int idx = lerInt("  Escolha (numero): ");
        if (idx < 1 || idx > turmas.size()) { System.out.println("  [!] Opcao invalida."); return null; }
        return turmas.get(idx - 1);
    }

    public Inscricao selecionarInscricaoAtiva(Aluno aluno) {
        List<Inscricao> ativas = aluno.getInscricoesAtivas();
        if (ativas.isEmpty()) { System.out.println("  [!] Aluno nao possui inscricoes ativas."); return null; }
        System.out.println("  Inscricoes ativas:");
        for (int i = 0; i < ativas.size(); i++) {
            Inscricao ins = ativas.get(i);
            System.out.printf("    [%d] Turma: %s — Disciplina: %s — Tipo: %s%n",
                    i + 1, ins.getTurma().getCodigo(),
                    ins.getTurma().getDisciplina().getNome(),
                    ins.getTipoInscricao());
        }
        int idx = lerInt("  Escolha (numero): ");
        if (idx < 1 || idx > ativas.size()) { System.out.println("  [!] Opcao invalida."); return null; }
        return ativas.get(idx - 1);
    }

    // ─── Utilitários de exibição ──────────────────────────────────────────────

    public void pausar() {
        System.out.print("\n  Pressione ENTER para continuar...");
        scanner.nextLine();
    }

    public void imprimirSeparador(String titulo) {
        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.printf ("║  %-44s ║%n", titulo);
        System.out.println("╚══════════════════════════════════════════════╝");
    }
}
