package domain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Coordena o ciclo de vida completo dos dados da aplicacao.
 *
 * Responsabilidades:
 *  - Carregar todos os dados dos arquivos CSV na ordem correta (respeita dependencias)
 *  - Fornecer acesso tipado as colecoes em memoria
 *  - Salvar todas as entidades ao encerrar a aplicacao
 *
 * Uso:
 *  GerenciadorDados gd = new GerenciadorDados();
 *  gd.carregar();          // inicio da aplicacao
 *  // ... operacoes de negocio ...
 *  gd.salvar();            // encerramento da aplicacao
 */
public final class GerenciadorDados {

    // Repositorios
    private final RepositorioSecretaria repoSecretaria       = new RepositorioSecretaria();
    private final RepositorioCurso repoCurso                 = new RepositorioCurso();
    private final RepositorioCurriculo repoCurriculo         = new RepositorioCurriculo();
    private final RepositorioProfessor repoProfessor         = new RepositorioProfessor();
    private final RepositorioDisciplina repoDisciplina       = new RepositorioDisciplina();
    private final RepositorioAluno repoAluno                 = new RepositorioAluno();
    private final RepositorioTurma repoTurma                 = new RepositorioTurma();
    private final RepositorioInscricao repoInscricao         = new RepositorioInscricao();
    private final RepositorioDisciplinaCursada repoDC        = new RepositorioDisciplinaCursada();

    // Colecoes em memoria
    private final List<Secretaria>        secretarias        = new ArrayList<>();
    private final List<Curso>             cursos             = new ArrayList<>();
    private final List<Curriculo>         curriculos         = new ArrayList<>();
    private final List<Professor>         professores        = new ArrayList<>();
    private final List<Disciplina>        disciplinas        = new ArrayList<>();
    private final List<Aluno>             alunos             = new ArrayList<>();
    private final List<Turma>             turmas             = new ArrayList<>();
    private final List<Inscricao>         inscricoes         = new ArrayList<>();
    private final List<DisciplinaCursada> disciplinasCursadas = new ArrayList<>();

    // ServicoCobranca e TabelaPrecos â€” unicas instancias da aplicacao
    private TabelaPrecos tabelaPrecos;
    private ServicoCobranca servicoCobranca;

    // -------------------------------------------------------------------------
    // Carregamento
    // -------------------------------------------------------------------------

    /**
     * Carrega todos os dados dos arquivos CSV na ordem correta de dependencias.
     * O mapa de contexto relaciona IDs/codigos aos objetos ja instanciados,
     * permitindo que repositorios mais tardios resolvam referencias.
     */
    public void carregar() {
        Map<String, Object> ctx = new HashMap<>();

        secretarias.clear();
        secretarias.addAll(repoSecretaria.carregarTodos(ctx));

        cursos.clear();
        cursos.addAll(repoCurso.carregarTodos(ctx));

        curriculos.clear();
        curriculos.addAll(repoCurriculo.carregarTodos(ctx));

        professores.clear();
        professores.addAll(repoProfessor.carregarTodos(ctx));

        disciplinas.clear();
        disciplinas.addAll(repoDisciplina.carregarTodos(ctx));

        alunos.clear();
        alunos.addAll(repoAluno.carregarTodos(ctx));

        turmas.clear();
        turmas.addAll(repoTurma.carregarTodos(ctx));

        inscricoes.clear();
        inscricoes.addAll(repoInscricao.carregarTodos(ctx));

        disciplinasCursadas.clear();
        disciplinasCursadas.addAll(repoDC.carregarTodos(ctx));

        // TabelaPrecos e ServicoCobranca â€” reconstruidos com valores zerados se nao houver dados
        this.tabelaPrecos = new TabelaPrecos(0.0, 0.0);
        this.servicoCobranca = new ServicoCobranca(tabelaPrecos);
        inscricoes.forEach(inscricao -> inscricao.vincularServicoCobranca(servicoCobranca));
    }

    // -------------------------------------------------------------------------
    // Salvamento
    // -------------------------------------------------------------------------

    /**
     * Persiste todas as entidades em memoria nos arquivos CSV.
     * Chamado ao encerrar a aplicacao ou sob demanda.
     */
    public void salvar() {
        repoSecretaria.salvarTodos(secretarias);
        repoCurso.salvarTodos(cursos);
        repoCurriculo.salvarTodos(curriculos);
        repoProfessor.salvarTodos(professores);
        repoDisciplina.salvarTodos(disciplinas);
        repoAluno.salvarTodos(alunos);
        repoTurma.salvarTodos(turmas);
        repoInscricao.salvarTodos(inscricoes);
        repoDC.salvarTodos(disciplinasCursadas);
    }

    // -------------------------------------------------------------------------
    // Acesso as colecoes (usadas pela CLI)
    // -------------------------------------------------------------------------

    public List<Secretaria>        getSecretarias()         { return secretarias; }
    public List<Curso>             getCursos()              { return cursos; }
    public List<Curriculo>         getCurriculos()          { return curriculos; }
    public List<Professor>         getProfessores()         { return professores; }
    public List<Disciplina>        getDisciplinas()         { return disciplinas; }
    public List<Aluno>             getAlunos()              { return alunos; }
    public List<Turma>             getTurmas()              { return turmas; }
    public List<Inscricao>         getInscricoes()          { return inscricoes; }
    public List<DisciplinaCursada> getDisciplinasCursadas() { return disciplinasCursadas; }
    public TabelaPrecos            getTabelaPrecos()        { return tabelaPrecos; }
    public ServicoCobranca         getServicoCobranca()     { return servicoCobranca; }

    // -------------------------------------------------------------------------
    // Busca rapida
    // -------------------------------------------------------------------------

    public Aluno buscarAlunoPorEmail(String email) {
        if (email == null) return null;
        return alunos.stream()
                .filter(a -> a.getEmailCorporativo().equalsIgnoreCase(email.trim()))
                .findFirst().orElse(null);
    }

    public Professor buscarProfessorPorEmail(String email) {
        if (email == null) return null;
        return professores.stream()
                .filter(p -> p.getEmailCorporativo().equalsIgnoreCase(email.trim()))
                .findFirst().orElse(null);
    }

    public Secretaria buscarSecretariaPorEmail(String email) {
        if (email == null) return null;
        return secretarias.stream()
                .filter(s -> s.getEmailCorporativo().equalsIgnoreCase(email.trim()))
                .findFirst().orElse(null);
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        if (email == null || email.isBlank()) return null;
        List<Usuario> correspondencias = new ArrayList<>();
        secretarias.stream().filter(u -> u.getEmailCorporativo().equalsIgnoreCase(email.trim()))
                .forEach(correspondencias::add);
        professores.stream().filter(u -> u.getEmailCorporativo().equalsIgnoreCase(email.trim()))
                .forEach(correspondencias::add);
        alunos.stream().filter(u -> u.getEmailCorporativo().equalsIgnoreCase(email.trim()))
                .forEach(correspondencias::add);
        return correspondencias.size() == 1 ? correspondencias.get(0) : null;
    }

    public boolean emailDisponivel(String email, Usuario ignorar) {
        if (email == null || email.isBlank()) return false;
        return java.util.stream.Stream.of(secretarias, professores, alunos)
                .flatMap(List::stream)
                .noneMatch(usuario -> usuario != ignorar
                        && usuario.getEmailCorporativo().equalsIgnoreCase(email.trim()));
    }

    public List<Turma> getTurmasPorCurriculo(Curriculo curriculo) {
        return turmas.stream()
                .filter(t -> t.getCurriculo() == curriculo)
                .toList();
    }
}

