package cli;
import domain.*;
import java.util.List;
import java.util.Scanner;

public class Contexto {

    public final Scanner scanner = new Scanner(System.in);
    public final GerenciadorDados dados;

    public final List<Curso>       cursos;
    public final List<Aluno>       alunos;
    public final List<Professor>   professores;
    public final List<Disciplina>  disciplinas;
    public final List<Curriculo>   curriculos;
    public final List<Turma>       turmas;
    public final ServicoCobranca   servicoCobranca;

    public Secretaria secretariaAtiva;
    public Professor professorAtivo;
    public Aluno alunoAtivo;

    public Contexto(GerenciadorDados dados) {
        this.dados = dados;
        this.cursos = dados.getCursos();
        this.alunos = dados.getAlunos();
        this.professores = dados.getProfessores();
        this.disciplinas = dados.getDisciplinas();
        this.curriculos = dados.getCurriculos();
        this.turmas = dados.getTurmas();
        this.servicoCobranca = dados.getServicoCobranca();
    }

    public String lerLinha(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public int lerInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String linha = scanner.nextLine().trim();
            try { return Integer.parseInt(linha); } 
            catch (NumberFormatException e) { System.out.println("  [!] Entrada invalida."); }
        }
    }

    public double lerDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String linha = scanner.nextLine().trim();
            try { return Double.parseDouble(linha.replace(",", ".")); } 
            catch (NumberFormatException e) { System.out.println("  [!] Entrada invalida."); }
        }
    }

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

    public Curso selecionarCurso() {
        if (cursos.isEmpty()) { System.out.println("  [!] Nenhum curso cadastrado."); return null; }
        for (int i = 0; i < cursos.size(); i++) {
            System.out.printf("    [%d] %s (%s)%n", i + 1, cursos.get(i).getNome(), cursos.get(i).getCodigo());
        }
        int idx = lerInt("  Escolha: ");
        if (idx < 1 || idx > cursos.size()) { System.out.println("  [!] Opcao invalida."); return null; }
        return cursos.get(idx - 1);
    }

    public Aluno selecionarAluno() {
        if (alunos.isEmpty()) { System.out.println("  [!] Nenhum aluno."); return null; }
        for (int i = 0; i < alunos.size(); i++) {
            System.out.printf("    [%d] %s %s - %s%n", i + 1, alunos.get(i).getNome(), alunos.get(i).getSobrenome(), alunos.get(i).getMatricula());
        }
        int idx = lerInt("  Escolha: ");
        if (idx < 1 || idx > alunos.size()) return null;
        return alunos.get(idx - 1);
    }

    public Professor selecionarProfessor() {
        if (professores.isEmpty()) { System.out.println("  [!] Nenhum professor."); return null; }
        for (int i = 0; i < professores.size(); i++) {
            System.out.printf("    [%d] %s %s - %s%n", i + 1, professores.get(i).getNome(), professores.get(i).getSobrenome(), professores.get(i).getCodigo());
        }
        int idx = lerInt("  Escolha: ");
        if (idx < 1 || idx > professores.size()) return null;
        return professores.get(idx - 1);
    }

    public Disciplina selecionarDisciplina() {
        if (disciplinas.isEmpty()) { System.out.println("  [!] Nenhuma disciplina."); return null; }
        for (int i = 0; i < disciplinas.size(); i++) {
            System.out.printf("    [%d] %s (%s)%n", i + 1, disciplinas.get(i).getNome(), disciplinas.get(i).getCodigo());
        }
        int idx = lerInt("  Escolha: ");
        if (idx < 1 || idx > disciplinas.size()) return null;
        return disciplinas.get(idx - 1);
    }

    public Curriculo selecionarCurriculo() {
        if (curriculos.isEmpty()) { System.out.println("  [!] Nenhum curriculo."); return null; }
        for (int i = 0; i < curriculos.size(); i++) {
            System.out.printf("    [%d] Semestre: %s%n", i + 1, curriculos.get(i).getSemestre());
        }
        int idx = lerInt("  Escolha: ");
        if (idx < 1 || idx > curriculos.size()) return null;
        return curriculos.get(idx - 1);
    }

    public Turma selecionarTurma() {
        if (turmas.isEmpty()) { System.out.println("  [!] Nenhuma turma."); return null; }
        for (int i = 0; i < turmas.size(); i++) {
            System.out.printf("    [%d] %s - %s%n", i + 1, turmas.get(i).getCodigo(), turmas.get(i).getDisciplina().getNome());
        }
        int idx = lerInt("  Escolha: ");
        if (idx < 1 || idx > turmas.size()) return null;
        return turmas.get(idx - 1);
    }

    public Inscricao selecionarInscricaoAtiva(Aluno aluno) {
        List<Inscricao> ativas = aluno.getInscricoesAtivas();
        if (ativas.isEmpty()) { System.out.println("  [!] Nenhuma inscricao ativa."); return null; }
        for (int i = 0; i < ativas.size(); i++) {
            System.out.printf("    [%d] %s - %s%n", i + 1, ativas.get(i).getTurma().getCodigo(), ativas.get(i).getTurma().getDisciplina().getNome());
        }
        int idx = lerInt("  Escolha: ");
        if (idx < 1 || idx > ativas.size()) return null;
        return ativas.get(idx - 1);
    }

    public void pausar() {
        System.out.print("\n  Pressione ENTER para continuar...");
        scanner.nextLine();
    }

    public void imprimirSeparador(String titulo) {
        System.out.println("\n------------------------------------------------");
        System.out.printf ("  %s%n", titulo);
        System.out.println("------------------------------------------------");
    }
}
