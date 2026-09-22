import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Professor extends Usuario {
    private String codigo;
    private final Secretaria secretaria;
    private final List<Turma> turmas = new ArrayList<>();

    public Professor(String codigo, Secretaria secretaria, String emailCorporativo,
                     String senha, Endereco endereco) {
        super(emailCorporativo, senha, validarDados(codigo, secretaria, endereco));
        this.codigo = Objects.requireNonNull(codigo);
        this.secretaria = Objects.requireNonNull(secretaria, "Secretaria obrigatoria");
        secretaria.registrarProfessor(this);
    }

    private static Endereco validarDados(String codigo, Secretaria secretaria, Endereco endereco) {
        Objects.requireNonNull(codigo, "codigo obrigatorio");
        Objects.requireNonNull(secretaria, "secretaria obrigatorio");
        return endereco;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = Objects.requireNonNull(codigo); }
    public Secretaria getSecretaria() { return secretaria; }
    public List<Turma> getTurmas() { return Collections.unmodifiableList(turmas); }
    void registrarTurma(Turma turma) {
        if (turma.getProfessor() != this) throw new IllegalArgumentException("Professor diferente");
        if (!turmas.contains(turma)) turmas.add(turma);
    }

    public List<Aluno> listarAlunosPorTurma(Turma turma) {
        if (Objects.requireNonNull(turma).getProfessor() != this) {
            throw new IllegalArgumentException("Turma nao pertence a este professor");
        }
        return turma.getAlunos();
    }

    public void validarEstrutura() {
        if (turmas.isEmpty()) throw new IllegalStateException("Professor exige ao menos uma turma");
    }
}
