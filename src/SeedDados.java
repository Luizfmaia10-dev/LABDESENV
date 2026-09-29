/**
 * Popula o sistema com dados iniciais de teste e salva nos arquivos CSV em data/.
 *
 * Execute uma vez antes de rodar a aplicacao pela primeira vez:
 *   java -cp build SeedDados
 *
 * Credenciais criadas:
 *   Secretaria : secretaria@uni.br  / senha123
 *   Professor  : professor@uni.br   / senha123
 *   Aluno 1    : ana@uni.br         / senha123
 *   Aluno 2    : bruno@uni.br       / senha123
 */
public final class SeedDados {

    public static void main(String[] args) {
        GerenciadorDados dados = new GerenciadorDados();
        // Começa do zero — ignora qualquer arquivo existente
        dados.carregar();

        // ------------------------------------------------------------------
        // Secretaria
        // ------------------------------------------------------------------
        Endereco endSec = new Endereco("Av. Principal", "1", "Belo Horizonte", "Centro", "30100-000", "");
        Secretaria secretaria = new Secretaria("Atendimento Geral", "secretaria@uni.br", "senha123", endSec);
        dados.getSecretarias().add(secretaria);

        // ------------------------------------------------------------------
        // Curso
        // ------------------------------------------------------------------
        Curso curso = new Curso("Engenharia de Software", "ESW", 240);
        dados.getCursos().add(curso);

        // ------------------------------------------------------------------
        // Curriculo — periodo de matricula ABERTO
        // ------------------------------------------------------------------
        Curriculo curriculo = secretaria.gerarCurriculo("2026/2");
        dados.getCurriculos().add(curriculo);

        // ------------------------------------------------------------------
        // Professor
        // ------------------------------------------------------------------
        Endereco endProf = new Endereco("Rua das Flores", "42", "Belo Horizonte", "Savassi", "30110-000", "");
        Professor professor = new Professor("PROF-001", secretaria, "professor@uni.br", "senha123", endProf);
        dados.getProfessores().add(professor);

        // ------------------------------------------------------------------
        // Disciplinas
        // ------------------------------------------------------------------
        Disciplina alg  = new Disciplina("Algoritmos",              "ALG",  4, curso, secretaria);
        Disciplina bd   = new Disciplina("Banco de Dados",          "BD",   4, curso, secretaria);
        Disciplina eng  = new Disciplina("Engenharia de Requisitos","REQ",  4, curso, secretaria);
        Disciplina opt1 = new Disciplina("Inteligencia Artificial", "IA",   2, curso, secretaria);
        dados.getDisciplinas().add(alg);
        dados.getDisciplinas().add(bd);
        dados.getDisciplinas().add(eng);
        dados.getDisciplinas().add(opt1);

        // ------------------------------------------------------------------
        // Turmas (periodo aberto, entao construtor publico funciona)
        // ------------------------------------------------------------------
        Turma tAlg  = new Turma(40, 3, true, "ALG-01",  alg,  professor, curriculo);
        Turma tBd   = new Turma(40, 3, true, "BD-01",   bd,   professor, curriculo);
        Turma tReq  = new Turma(40, 3, true, "REQ-01",  eng,  professor, curriculo);
        Turma tIa   = new Turma(40, 3, true, "IA-01",   opt1, professor, curriculo);
        dados.getTurmas().add(tAlg);
        dados.getTurmas().add(tBd);
        dados.getTurmas().add(tReq);
        dados.getTurmas().add(tIa);

        // ------------------------------------------------------------------
        // Alunos
        // ------------------------------------------------------------------
        Endereco endAna = new Endereco("Rua A", "10", "Belo Horizonte", "Pampulha", "31270-000", "");
        Aluno ana = new Aluno("ANA-2026", StatusMatricula.ATIVA, curso, secretaria,
                "ana@uni.br", "senha123", endAna);
        dados.getAlunos().add(ana);

        Endereco endBruno = new Endereco("Rua B", "20", "Belo Horizonte", "Buritis", "30575-000", "");
        Aluno bruno = new Aluno("BRUNO-2026", StatusMatricula.ATIVA, curso, secretaria,
                "bruno@uni.br", "senha123", endBruno);
        dados.getAlunos().add(bruno);

        // ------------------------------------------------------------------
        // Tabela de precos
        // ------------------------------------------------------------------
        dados.getTabelaPrecos().setValorCreditoObrigatoria(120.0);
        dados.getTabelaPrecos().setValorCreditoOptativa(100.0);

        // ------------------------------------------------------------------
        // Salva tudo em data/
        // ------------------------------------------------------------------
        dados.salvar();

        System.out.println("=== Seed concluido! Dados salvos em data/ ===");
        System.out.println();
        System.out.println("Credenciais para login:");
        System.out.println("  Secretaria : secretaria@uni.br  / senha123");
        System.out.println("  Professor  : professor@uni.br   / senha123");
        System.out.println("  Aluno 1    : ana@uni.br         / senha123");
        System.out.println("  Aluno 2    : bruno@uni.br       / senha123");
        System.out.println();
        System.out.println("Turmas disponiveis (periodo aberto):");
        System.out.println("  ALG-01  — Algoritmos              (4 creditos, OBRIGATORIA)");
        System.out.println("  BD-01   — Banco de Dados          (4 creditos, OBRIGATORIA)");
        System.out.println("  REQ-01  — Engenharia de Requisitos(4 creditos, OBRIGATORIA)");
        System.out.println("  IA-01   — Inteligencia Artificial  (2 creditos, OPTATIVA)");
    }
}
