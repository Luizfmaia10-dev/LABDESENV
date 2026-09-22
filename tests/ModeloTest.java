import java.time.LocalDate;
import java.util.List;

/** Testes de comportamento, executados sem bibliotecas externas. */
public class ModeloTest {
    private static int verificacoes;

    private static void verificar(boolean condicao, String mensagem) {
        verificacoes++;
        if (!condicao) throw new AssertionError(mensagem);
    }

    private static void rejeitar(Runnable acao) {
        verificacoes++;
        try { acao.run(); }
        catch (IllegalArgumentException | IllegalStateException | UnsupportedOperationException | NullPointerException e) { return; }
        throw new AssertionError("A operacao deveria ser rejeitada");
    }

    private static Endereco endereco() {
        return new Endereco("Rua A", "10", "Belo Horizonte", "Centro", "30100-000", "Casa");
    }

    private static class Cenario {
        final Secretaria secretaria = new Secretaria("Atendimento", "sec@universidade.br", "senha", endereco());
        final Curso curso = new Curso("Engenharia", "ENG", 200);
        final Curriculo curriculo = secretaria.gerarCurriculo("2026/2");
        final Professor professor = new Professor("P1", secretaria, "prof@universidade.br", "senha", endereco());
        final ServicoCobranca cobranca = new ServicoCobranca();
        Aluno aluno(String numero) {
            return new Aluno(numero, StatusMatricula.ATIVA, curso, secretaria,
                    numero + "@universidade.br", "senha", endereco());
        }
        Turma turma(String codigo) {
            Disciplina disciplina = new Disciplina(codigo, codigo, 4, curso, secretaria);
            return new Turma(60, 3, true, codigo, disciplina, professor, curriculo);
        }
        Inscricao inscrever(Aluno aluno, Turma turma, TipoInscricao tipo) {
            return aluno.matricular(turma, tipo, cobranca);
        }
    }

    public static void main(String[] args) {
        credenciaisEEndereco();
        relacionamentosEHistorico();
        limitesDoAluno();
        limitesDaTurma();
        periodoStatusECancelamento();
        cobranca();
        construcaoInvalidaNaoReservaEndereco();
        caminhosDiretosRespeitamRegras();
        limitePorSemestreECurriculoUnico();
        System.out.println("OK: " + verificacoes + " verificacoes");
    }

    private static void credenciaisEEndereco() {
        Cenario c = new Cenario();
        Aluno a = c.aluno("A1");
        verificar(a.autenticar("A1@universidade.br", "senha"), "Autenticacao valida");
        verificar(!a.autenticar("A1@universidade.br", "errada"), "Senha incorreta");
        rejeitar(() -> a.alterarSenha("errada", "nova"));
        a.alterarSenha("senha", "nova");
        verificar(a.autenticar("A1@universidade.br", "nova"), "Senha alterada");
        verificar(!a.autenticar("A1@universidade.br", "senha"), "Senha anterior revogada");
        Endereco antigo = a.getEndereco();
        rejeitar(() -> new Professor("P2", c.secretaria, "p2@u.br", "senha", antigo));
        Endereco novo = endereco();
        a.setEndereco(novo);
        verificar(novo.getPessoa() == a && antigo.getPessoa() == null, "Relacao 1:1 consistente");
        verificar(novo.toString().contains("30100-000"), "Endereco legivel");
        verificar(Endereco.buscarEnderecoPeloCEP("30100000", List.of(novo)).orElseThrow() == novo,
                "Busca local normaliza CEP");
    }

    private static void relacionamentosEHistorico() {
        Cenario c = new Cenario();
        Aluno a = c.aluno("A1");
        Turma t = c.turma("T1");
        Inscricao i = c.inscrever(a, t, TipoInscricao.OBRIGATORIA);
        verificar(c.curso.getAlunos().contains(a), "Curso conhece aluno");
        verificar(c.secretaria.getAlunos().contains(a), "Secretaria conhece aluno");
        verificar(c.secretaria.getProfessores().contains(c.professor), "Secretaria conhece professor");
        verificar(c.secretaria.getDisciplinas().contains(t.getDisciplina()), "Secretaria conhece disciplina");
        verificar(c.secretaria.getCurriculos().contains(c.curriculo), "Secretaria conhece curriculo");
        verificar(c.curso.getDisciplinas().contains(t.getDisciplina()), "Curso agrega disciplinas");
        verificar(t.getDisciplina().getTurmas().contains(t), "Disciplina conhece turmas");
        verificar(c.professor.listarAlunosPorTurma(t).equals(List.of(a)), "Consulta do professor");
        verificar(a.getInscricoes().contains(i) && t.getInscricoes().contains(i), "Classe associativa");
        rejeitar(() -> a.getInscricoes().clear());
        rejeitar(() -> c.curso.getDisciplinas().clear());
        DisciplinaCursada h1 = new DisciplinaCursada("2025/2", true, 90, a, t.getDisciplina());
        DisciplinaCursada h2 = new DisciplinaCursada("2026/1", false, 40, a, t.getDisciplina());
        verificar(a.getHistorico().equals(List.of(h1, h2)), "Historico 1:N");
        verificar(h1.getAluno() == a && h1.getDisciplina() == t.getDisciplina(), "Historico tem donos");
        c.curso.validarEstrutura();
        c.professor.validarEstrutura();
        rejeitar(() -> new Curso("Vazio", "V", 10).validarEstrutura());
        Professor outro = new Professor("P2", c.secretaria, "p2@u.br", "senha", endereco());
        rejeitar(outro::validarEstrutura);
        rejeitar(() -> outro.listarAlunosPorTurma(t));
    }

    private static void limitesDoAluno() {
        Cenario c = new Cenario();
        Aluno a = c.aluno("A1");
        Turma primeira = c.turma("T0");
        c.inscrever(a, primeira, TipoInscricao.OBRIGATORIA);
        rejeitar(() -> c.inscrever(a, primeira, TipoInscricao.OBRIGATORIA));
        for (int n = 1; n < 4; n++) c.inscrever(a, c.turma("T" + n), TipoInscricao.OBRIGATORIA);
        rejeitar(() -> c.inscrever(a, c.turma("ExtraObrigatoria"), TipoInscricao.OBRIGATORIA));
        c.inscrever(a, c.turma("O1"), TipoInscricao.OPTATIVA);
        c.inscrever(a, c.turma("O2"), TipoInscricao.OPTATIVA);
        rejeitar(() -> c.inscrever(a, c.turma("O3"), TipoInscricao.OPTATIVA));
        verificar(a.getInscricoesAtivas().size() == 6, "Limite total seis");
        a.cancelarMatricula(a.getInscricoesAtivas().get(0));
        c.inscrever(a, primeira, TipoInscricao.OBRIGATORIA);
        verificar(a.getInscricoesAtivas().size() == 6, "Cancelamento libera vaga");
    }

    private static void limitesDaTurma() {
        Cenario c = new Cenario();
        Turma cheia = c.turma("Cheia");
        for (int n = 0; n < 60; n++) c.inscrever(c.aluno("A" + n), cheia, TipoInscricao.OBRIGATORIA);
        rejeitar(() -> c.inscrever(c.aluno("Extra"), cheia, TipoInscricao.OBRIGATORIA));
        verificar(cheia.getAlunos().size() == 60, "Maximo 60");
        rejeitar(() -> cheia.setLimiteMaximo(59));
        rejeitar(() -> cheia.setLimiteMaximo(61));
        rejeitar(() -> cheia.setLimiteMinimo(2));
        Turma pequena = c.turma("Pequena");
        c.inscrever(c.aluno("B1"), pequena, TipoInscricao.OPTATIVA);
        c.inscrever(c.aluno("B2"), pequena, TipoInscricao.OPTATIVA);
        Turma minima = c.turma("Minima");
        for (int n = 0; n < 3; n++) c.inscrever(c.aluno("C" + n), minima, TipoInscricao.OBRIGATORIA);
        rejeitar(pequena::fecharTurma);
        c.curriculo.setPeriodoMatriculaAberto(false);
        pequena.fecharTurma();
        minima.fecharTurma();
        verificar(!pequena.isAtiva() && pequena.getAlunos().isEmpty(), "Cancela turma com menos de tres");
        verificar(minima.isAtiva() && minima.getAlunos().size() == 3, "Tres alunos ativam turma");
        verificar(c.cobranca.getNotificacoes().size() == 67, "Cancelamento da turma notifica cobranca");
    }

    private static void periodoStatusECancelamento() {
        Cenario c = new Cenario();
        Aluno a = c.aluno("A1");
        Turma t = c.turma("T1");
        Inscricao i = c.inscrever(a, t, TipoInscricao.OBRIGATORIA);
        c.curriculo.setPeriodoMatriculaAberto(false);
        rejeitar(() -> a.cancelarMatricula(i));
        rejeitar(() -> c.inscrever(c.aluno("A2"), t, TipoInscricao.OBRIGATORIA));
        c.curriculo.setPeriodoMatriculaAberto(true);
        rejeitar(() -> c.aluno("Outro").cancelarMatricula(i));
        a.trancarMatricula();
        verificar(a.getStatus() == StatusMatricula.TRANCADA, "Status trancada");
        verificar(!i.isAtiva() && t.getAlunos().isEmpty(), "Trancamento libera vagas");
        rejeitar(() -> c.inscrever(a, t, TipoInscricao.OBRIGATORIA));
        a.setStatus(StatusMatricula.ALUNO_FORMADO);
        rejeitar(() -> c.inscrever(a, t, TipoInscricao.OBRIGATORIA));
        verificar(i.getDataHora().equals(LocalDate.now()), "Data de inscricao");
    }

    private static void cobranca() {
        Cenario c = new Cenario();
        Aluno a = c.aluno("A1");
        Inscricao i = c.inscrever(a, c.turma("T1"), TipoInscricao.OBRIGATORIA);
        verificar(c.cobranca.getNotificacoes().size() == 1, "Inscricao notifica");
        verificar(c.cobranca.gerarCobranca(a, "2026/2").size() == 1, "Cobranca inclui inscricoes ativas");
        verificar(c.cobranca.gerarCobranca(a, "2025/1").isEmpty(), "Cobranca filtra semestre");
        a.cancelarMatricula(i);
        verificar(c.cobranca.getNotificacoes().size() == 2, "Cancelamento notifica");
        verificar(c.cobranca.gerarCobranca(a, "2026/2").isEmpty(), "Cancelada nao e cobrada");
    }

    private static void construcaoInvalidaNaoReservaEndereco() {
        Cenario c = new Cenario();
        Endereco e = endereco();
        rejeitar(() -> new Aluno(null, StatusMatricula.ATIVA, c.curso, c.secretaria, "a@u.br", "senha", e));
        verificar(e.getPessoa() == null, "Aluno invalido nao reserva endereco");
        rejeitar(() -> new Professor("P", null, "p@u.br", "senha", e));
        verificar(e.getPessoa() == null, "Professor invalido nao reserva endereco");
        rejeitar(() -> new Secretaria(null, "s@u.br", "senha", e));
        verificar(e.getPessoa() == null, "Secretaria invalida nao reserva endereco");
    }

    private static void caminhosDiretosRespeitamRegras() {
        Cenario c = new Cenario();
        Aluno a = c.aluno("A1");
        Turma t = c.turma("T1");
        Inscricao i = new Inscricao(TipoInscricao.OBRIGATORIA, LocalDate.now(), a, t, c.cobranca);
        rejeitar(() -> new Inscricao(TipoInscricao.OBRIGATORIA, LocalDate.now(), a, t, c.cobranca));
        verificar(a.getInscricoes().size() == 1 && t.getInscricoes().size() == 1,
                "Falha de inscricao nao altera associacoes");
        Turma paralela = new Turma(60, 3, true, "T2", t.getDisciplina(), c.professor, c.curriculo);
        rejeitar(() -> c.inscrever(a, paralela, TipoInscricao.OPTATIVA));
        i.setAtiva(false);
        i.setAtiva(true);
        verificar(t.getAlunos().equals(List.of(a)), "Reativacao recupera uma vaga sem duplicar aluno");
        i.setAtiva(false);
        c.curriculo.setPeriodoMatriculaAberto(false);
        rejeitar(() -> i.setAtiva(true));
        c.curriculo.setPeriodoMatriculaAberto(true);
        i.setAtiva(true);
        a.setStatus(StatusMatricula.ALUNO_FORMADO);
        verificar(!i.isAtiva(), "Formatura libera inscricoes ativas");
        rejeitar(() -> i.setAtiva(true));
        Aluno externo = new Aluno("EX", StatusMatricula.ATIVA, new Curso("Outro", "O", 1),
                c.secretaria, "ex@u.br", "senha", endereco());
        rejeitar(() -> c.inscrever(externo, t, TipoInscricao.OBRIGATORIA));
        rejeitar(() -> new Turma(61, 3, true, "Invalida", t.getDisciplina(), c.professor, c.curriculo));
        t.setAtiva(false);
        rejeitar(() -> c.inscrever(c.aluno("Novo"), t, TipoInscricao.OBRIGATORIA));
    }

    private static void limitePorSemestreECurriculoUnico() {
        Cenario c = new Cenario();
        Aluno a = c.aluno("A1");
        for (int n = 0; n < 4; n++) c.inscrever(a, c.turma("Antiga" + n), TipoInscricao.OBRIGATORIA);
        c.curriculo.setPeriodoMatriculaAberto(false);
        Curriculo seguinte = c.secretaria.gerarCurriculo("2027/1");
        Disciplina d = new Disciplina("Nova", "N", 4, c.curso, c.secretaria);
        Turma nova = new Turma(60, 3, true, "Nova", d, c.professor, seguinte);
        c.inscrever(a, nova, TipoInscricao.OBRIGATORIA);
        verificar(c.cobranca.gerarCobranca(a, "2027/1").size() == 1,
                "Inscricoes antigas nao consomem limite do novo semestre");
        rejeitar(() -> new Curriculo("2027/1", true, c.secretaria));
        verificar(c.secretaria.getCurriculos().size() == 2, "Curriculo duplicado nao e registrado");
    }
}
