import domain.*;
public class SeedDados {
    public static void main(String[] args) {
        GerenciadorDados dados = new GerenciadorDados();
        dados.carregar();
        if (dados.getSecretarias().isEmpty()) {
            Endereco end = new Endereco("Rua A", "1", "Cidade", "Bairro", "000", "");
            Secretaria sec = new Secretaria("Admin", "secretaria@uni.br", "senha123", end);
            dados.getSecretarias().add(sec);
            Professor p = new Professor("P01", sec, "professor@uni.br", "senha123", end);
            p.setNome("Carlos"); p.setSobrenome("Silva");
            dados.getProfessores().add(p);
            Curso c = new Curso("Engenharia", "ENG01", 200);
            dados.getCursos().add(c);
            Aluno a1 = new Aluno("A01", StatusMatricula.ATIVA, c, sec, "ana@uni.br", "senha123", end);
            a1.setNome("Ana"); a1.setSobrenome("Costa");
            Aluno a2 = new Aluno("A02", StatusMatricula.ATIVA, c, sec, "bruno@uni.br", "senha123", end);
            a2.setNome("Bruno"); a2.setSobrenome("Dias");
            dados.getAlunos().add(a1); dados.getAlunos().add(a2);
            dados.salvar();
            System.out.println("Seed criado!");
        }
    }
}
