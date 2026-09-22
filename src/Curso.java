import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Curso {
    private final List<Aluno> alunos = new ArrayList<>();
    private final List<Disciplina> disciplinas = new ArrayList<>();

    public List<Aluno> getAlunos() { return Collections.unmodifiableList(alunos); }
    public List<Disciplina> getDisciplinas() { return Collections.unmodifiableList(disciplinas); }

    void registrarAluno(Aluno aluno) {
        if (aluno.getCurso() != this) throw new IllegalArgumentException("Curso diferente");
        if (!alunos.contains(aluno)) alunos.add(aluno);
    }
    void registrarDisciplina(Disciplina disciplina) {
        if (disciplina.getCurso() != this) throw new IllegalArgumentException("Curso diferente");
        if (!disciplinas.contains(disciplina)) disciplinas.add(disciplina);
    }
    public void validarEstrutura() {
        if (disciplinas.isEmpty()) throw new IllegalStateException("Curso exige ao menos uma disciplina");
    }

    private String nome;
    private String codigo;
    private int creditoTotais;

    public Curso(String nome, String codigo, int creditoTotais) {
        this.nome = nome;
        this.codigo = codigo;
        if (creditoTotais < 0) throw new IllegalArgumentException("Creditos negativos");
        this.creditoTotais = creditoTotais;
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

    public int getCreditoTotais() {
        return creditoTotais;
    }

    public void setCreditoTotais(int creditoTotais) {
        if (creditoTotais < 0) throw new IllegalArgumentException("Creditos negativos");
        this.creditoTotais = creditoTotais;
    }
}
