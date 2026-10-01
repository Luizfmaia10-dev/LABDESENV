import domain.*;
public class SeedDados {
    public static void main(String[] args) {
        GerenciadorDados dados = new GerenciadorDados();
        dados.carregar();
        if (dados.getSecretarias().isEmpty()) {
            // Cada pessoa precisa do seu proprio objeto Endereco
            Secretaria sec = new Secretaria("Admin", "secretaria@uni.br", "senha123",
                    new Endereco("Rua A", "1", "Cidade", "Bairro", "000", ""));
            dados.getSecretarias().add(sec);
            Professor p = new Professor("P01", sec, "professor@uni.br", "senha123",
                    new Endereco("Rua B", "2", "Cidade", "Bairro", "001", ""));
            p.setNome("Carlos"); p.setSobrenome("Silva");
            dados.getProfessores().add(p);
            Curso c = new Curso("Engenharia", "ENG01", 200);
            dados.getCursos().add(c);
            Aluno a1 = new Aluno("A01", StatusMatricula.ATIVA, c, sec, "ana@uni.br", "senha123",
                    new Endereco("Rua C", "3", "Cidade", "Bairro", "002", ""));
            a1.setNome("Ana"); a1.setSobrenome("Costa");
            Aluno a2 = new Aluno("A02", StatusMatricula.ATIVA, c, sec, "bruno@uni.br", "senha123",
                    new Endereco("Rua D", "4", "Cidade", "Bairro", "003", ""));
            a2.setNome("Bruno"); a2.setSobrenome("Dias");
            dados.getAlunos().add(a1); dados.getAlunos().add(a2);
            // Curriculo e disciplinas do semestre 2026/2
            Curriculo curriculo = sec.gerarCurriculo("2026/2");
            dados.getCurriculos().add(curriculo);
            Disciplina alg = new Disciplina("Algoritmos",               "ALG", 4, c, sec);
            Disciplina bd  = new Disciplina("Banco de Dados",           "BD",  4, c, sec);
            Disciplina req = new Disciplina("Engenharia de Requisitos", "REQ", 4, c, sec);
            Disciplina ia  = new Disciplina("Inteligencia Artificial",  "IA",  2, c, sec);
            dados.getDisciplinas().add(alg);
            dados.getDisciplinas().add(bd);
            dados.getDisciplinas().add(req);
            dados.getDisciplinas().add(ia);
            // Turmas ativas (periodo de matricula esta aberto)
            dados.getTurmas().add(new Turma(40, 3, true, "ALG-01", alg, p, curriculo));
            dados.getTurmas().add(new Turma(40, 3, true, "BD-01",  bd,  p, curriculo));
            dados.getTurmas().add(new Turma(40, 3, true, "REQ-01", req, p, curriculo));
            dados.getTurmas().add(new Turma(40, 3, true, "IA-01",  ia,  p, curriculo));
            dados.salvar();
            System.out.println("Seed criado com sucesso!");
            System.out.println("Secretaria : secretaria@uni.br / senha123");
            System.out.println("Professor  : professor@uni.br  / senha123");
            System.out.println("Aluno 1    : ana@uni.br        / senha123");
            System.out.println("Aluno 2    : bruno@uni.br      / senha123");
        } else {
            System.out.println("Dados ja existem — seed ignorado.");
        }
    }
}
