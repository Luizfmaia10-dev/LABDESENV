# Modelo de matrículas

Classes Java do diagrama, sem dependências externas. Requer JDK 17 ou superior.

## Compilar e testar

Na raiz do repositório, execute no PowerShell:

```powershell
./tests/run-tests.ps1
```

Ou manualmente:

```powershell
javac -encoding UTF-8 --release 17 -d build src/*.java tests/ModeloTest.java
java -cp build ModeloTest
```

## Exemplo de uso

```java
Endereco enderecoSecretaria = new Endereco("Rua A", "10", "Belo Horizonte", "Centro", "30100-000", "");
Secretaria secretaria = new Secretaria("Atendimento", "secretaria@universidade.br", "senha-exemplo", enderecoSecretaria);
Curso curso = new Curso("Engenharia", "ENG", 200);
Curriculo curriculo = secretaria.gerarCurriculo("2026/2");
Disciplina disciplina = new Disciplina("Programacao", "PROG", 4, curso, secretaria);
Professor professor = new Professor("P1", secretaria, "professor@universidade.br", "senha-exemplo",
        new Endereco("Rua B", "20", "Belo Horizonte", "Centro", "30100-001", ""));
Turma turma = new Turma(60, 3, true, "PROG-01", disciplina, professor, curriculo);
Aluno aluno = new Aluno("A1", StatusMatricula.ATIVA, curso, secretaria,
        "aluno@universidade.br", "senha-exemplo",
        new Endereco("Rua C", "30", "Belo Horizonte", "Centro", "30100-002", ""));
ServicoCobranca cobranca = new ServicoCobranca();
Inscricao inscricao = aluno.matricular(turma, TipoInscricao.OBRIGATORIA, cobranca);
System.out.println(professor.listarAlunosPorTurma(turma).size()); // 1
aluno.cancelarMatricula(inscricao);

curso.validarEstrutura();
professor.validarEstrutura();
curriculo.setPeriodoMatriculaAberto(false);
turma.fecharTurma(); // Cancela a oferta: menos de 3 alunos.
```

Cada pessoa precisa de seu próprio objeto Endereco. As coleções dos relacionamentos são somente leitura; os construtores registram os vínculos nos dois lados.

`fecharTurma()` deve ser chamado em cada turma ao encerrar o período. Curso e Professor podem estar em montagem antes de `validarEstrutura()`, que exige pelo menos uma disciplina e uma turma, respectivamente.

As operações de cobrança registram notificações e selecionam inscrições por semestre em memória. A busca por CEP consulta uma coleção local. Este modelo não inclui CLI interativa, persistência, cálculo de preços nem integrações externas.

As adaptações e decisões estão em [implementacao-diagrama.md](../docs/implementacao-diagrama.md).
