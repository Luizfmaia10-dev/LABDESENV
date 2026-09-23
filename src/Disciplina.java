import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
public final class Disciplina {
    private final Curso curso;
    private final Secretaria secretaria;
    private final List<Turma> turmas = new ArrayList<>();
    private final List<DisciplinaCursada> historicos = new ArrayList<>();

    public Curso getCurso() { return curso; }
    public Secretaria getSecretaria() { return secretaria; }
    public List<Turma> getTurmas() { return Collections.unmodifiableList(turmas); }
    public List<DisciplinaCursada> getHistoricos() { return Collections.unmodifiableList(historicos); }
    void registrarTurma(Turma turma) {
        if (turma.getDisciplina() != this) throw new IllegalArgumentException("Disciplina diferente");
        if (!turmas.contains(turma)) turmas.add(turma);
    }
    void registrarHistorico(DisciplinaCursada historico) {
        if (historico.getDisciplina() != this) throw new IllegalArgumentException("Disciplina diferente");
        if (!historicos.contains(historico)) historicos.add(historico);
    }

    private String nome;
    private String codigo;
    private int creditos;

    public Disciplina(String nome, String codigo, int creditos, Curso curso, Secretaria secretaria) {
        this.curso = Objects.requireNonNull(curso, "Curso obrigatorio");
        this.secretaria = Objects.requireNonNull(secretaria, "Secretaria obrigatoria");
        this.nome = nome;
        this.codigo = codigo;
        if (creditos < 0) throw new IllegalArgumentException("Creditos negativos");
        this.creditos = creditos;
        curso.registrarDisciplina(this);
        secretaria.registrarDisciplina(this);
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public int getCreditos() {
        return creditos;
    }
    public void setCreditos(int creditos) {
        if (creditos < 0) throw new IllegalArgumentException("Creditos negativos");
        this.creditos = creditos;
    }
}
