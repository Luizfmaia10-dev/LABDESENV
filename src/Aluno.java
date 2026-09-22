import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Aluno extends Usuario {
    private String matricula;
    private StatusMatricula status;
    private final Curso curso;
    private final Secretaria secretaria;
    private final List<Inscricao> inscricoes = new ArrayList<>();
    private final List<DisciplinaCursada> historico = new ArrayList<>();

    public Aluno(String matricula, StatusMatricula status, Curso curso, Secretaria secretaria,
                 String emailCorporativo, String senha, Endereco endereco) {
        super(emailCorporativo, senha, validarDados(matricula, status, curso, secretaria, endereco));
        this.matricula = Objects.requireNonNull(matricula);
        this.status = Objects.requireNonNull(status);
        this.curso = Objects.requireNonNull(curso, "Curso obrigatorio");
        this.secretaria = Objects.requireNonNull(secretaria, "Secretaria obrigatoria");
        curso.registrarAluno(this);
        secretaria.registrarAluno(this);
    }

    private static Endereco validarDados(String matricula, StatusMatricula status, Curso curso, Secretaria secretaria, Endereco endereco) {
        Objects.requireNonNull(matricula, "matricula obrigatorio");
        Objects.requireNonNull(status, "status obrigatorio");
        Objects.requireNonNull(curso, "curso obrigatorio");
        Objects.requireNonNull(secretaria, "secretaria obrigatorio");
        return endereco;
    }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = Objects.requireNonNull(matricula); }
    public StatusMatricula getStatus() { return status; }
    /** Alteracao administrativa; estados nao ativos liberam as vagas ocupadas. */
    public void setStatus(StatusMatricula status) {
        Objects.requireNonNull(status);
        if (status != StatusMatricula.ATIVA) {
            for (Inscricao inscricao : getInscricoesAtivas()) inscricao.desativar();
        }
        this.status = status;
    }
    public Curso getCurso() { return curso; }
    public Secretaria getSecretaria() { return secretaria; }
    public List<Inscricao> getInscricoes() { return Collections.unmodifiableList(inscricoes); }
    public List<Inscricao> getInscricoesAtivas() {
        return inscricoes.stream().filter(Inscricao::isAtiva).toList();
    }
    public List<DisciplinaCursada> getHistorico() { return Collections.unmodifiableList(historico); }

    void registrarInscricao(Inscricao inscricao) {
        if (inscricao.getAluno() != this) throw new IllegalArgumentException("Aluno diferente");
        if (!inscricoes.contains(inscricao)) inscricoes.add(inscricao);
    }
    void registrarHistorico(DisciplinaCursada registro) {
        if (registro.getAluno() != this) throw new IllegalArgumentException("Aluno diferente");
        if (!historico.contains(registro)) historico.add(registro);
    }

    void validarMatricula(Turma turma, TipoInscricao tipo) {
        Objects.requireNonNull(turma);
        Objects.requireNonNull(tipo);
        exigirAtivo();
        if (!turma.getCurriculo().isPeriodoMatriculaAberto()) {
            throw new IllegalStateException("Periodo de matricula encerrado");
        }
        if (turma.getDisciplina().getCurso() != curso) {
            throw new IllegalArgumentException("Disciplina de outro curso");
        }
        // A cardinalidade de matriculas e avaliada no semestre da oferta.
        List<Inscricao> ativas = getInscricoesAtivas().stream()
                .filter(i -> i.getTurma().getCurriculo().getSemestre()
                        .equals(turma.getCurriculo().getSemestre())).toList();
        if (ativas.stream().anyMatch(i -> i.getTurma().getDisciplina() == turma.getDisciplina()
                && i.getTurma().getCurriculo().getSemestre().equals(turma.getCurriculo().getSemestre()))) {
            throw new IllegalStateException("Aluno ja inscrito nesta disciplina no semestre");
        }
        long quantidadeTipo = ativas.stream().filter(i -> i.getTipoInscricao() == tipo).count();
        int limite = tipo == TipoInscricao.OBRIGATORIA ? 4 : 2;
        if (ativas.size() >= 6 || quantidadeTipo >= limite) {
            throw new IllegalStateException("Limite de inscricoes do aluno atingido");
        }
        turma.validarVaga();
    }

    public Inscricao matricular(Turma turma, TipoInscricao tipo, ServicoCobranca servicoCobranca) {
        return new Inscricao(tipo, LocalDate.now(), this, turma, servicoCobranca);
    }

    public void cancelarMatricula(Inscricao inscricao) {
        exigirAtivo();
        if (Objects.requireNonNull(inscricao).getAluno() != this) {
            throw new IllegalArgumentException("Inscricao de outro aluno");
        }
        if (!inscricao.getTurma().getCurriculo().isPeriodoMatriculaAberto()) {
            throw new IllegalStateException("Periodo de matricula encerrado");
        }
        inscricao.desativar();
    }

    public void trancarMatricula() {
        exigirAtivo();
        setStatus(StatusMatricula.TRANCADA);
    }

    private void exigirAtivo() {
        if (status != StatusMatricula.ATIVA) throw new IllegalStateException("Aluno nao esta ativo");
    }
}
