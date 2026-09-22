import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class Secretaria extends Usuario {
    private final UUID idSecretaria = UUID.randomUUID();
    private String cargo;
    private final List<Aluno> alunos = new ArrayList<>();
    private final List<Professor> professores = new ArrayList<>();
    private final List<Disciplina> disciplinas = new ArrayList<>();
    private final List<Curriculo> curriculos = new ArrayList<>();

    public Secretaria(String cargo, String emailCorporativo, String senha, Endereco endereco) {
        super(emailCorporativo, senha, validarDados(cargo, endereco));
        this.cargo = Objects.requireNonNull(cargo);
    }

    private static Endereco validarDados(String cargo, Endereco endereco) {
        Objects.requireNonNull(cargo, "cargo obrigatorio");
        return endereco;
    }

    public UUID getIdSecretaria() { return idSecretaria; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = Objects.requireNonNull(cargo); }
    public List<Aluno> getAlunos() { return Collections.unmodifiableList(alunos); }
    public List<Professor> getProfessores() { return Collections.unmodifiableList(professores); }
    public List<Disciplina> getDisciplinas() { return Collections.unmodifiableList(disciplinas); }
    public List<Curriculo> getCurriculos() { return Collections.unmodifiableList(curriculos); }

    void registrarAluno(Aluno aluno) {
        if (aluno.getSecretaria() != this) throw new IllegalArgumentException("Secretaria diferente");
        if (!alunos.contains(aluno)) alunos.add(aluno);
    }
    void registrarProfessor(Professor professor) {
        if (professor.getSecretaria() != this) throw new IllegalArgumentException("Secretaria diferente");
        if (!professores.contains(professor)) professores.add(professor);
    }
    void registrarDisciplina(Disciplina disciplina) {
        if (disciplina.getSecretaria() != this) throw new IllegalArgumentException("Secretaria diferente");
        if (!disciplinas.contains(disciplina)) disciplinas.add(disciplina);
    }
    void registrarCurriculo(Curriculo curriculo) {
        if (curriculo.getSecretaria() != this) throw new IllegalArgumentException("Secretaria diferente");
        if (curriculos.stream().anyMatch(c -> c != curriculo && c.getSemestre().equals(curriculo.getSemestre()))) {
            throw new IllegalArgumentException("Curriculo ja cadastrado para o semestre");
        }
        if (!curriculos.contains(curriculo)) curriculos.add(curriculo);
    }

    public Curriculo gerarCurriculo(String semestre) {
        if (curriculos.stream().anyMatch(c -> c.getSemestre().equals(semestre))) {
            throw new IllegalArgumentException("Curriculo ja cadastrado para o semestre");
        }
        return new Curriculo(semestre, true, this);
    }
}
