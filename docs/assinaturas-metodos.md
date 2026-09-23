# Assinaturas dos metodos

## `Endereco`

```java
public Endereco(String rua, String numero, String cidade, String bairro, String CEP, String complemento)
public Pessoa getPessoa()
void vincularPessoa(Pessoa novaPessoa)
public static Optional<Endereco> buscarEnderecoPeloCEP(String cep, Collection<Endereco> cadastro)
public void cadastrarEndereco(Pessoa pessoa)
public void editarEndereco(String rua, String numero, String cidade, String bairro, String cep, String complemento)
public String toString()
public String getRua()
public void setRua(String rua)
public String getNumero()
public void setNumero(String numero)
public String getCidade()
public void setCidade(String cidade)
public void setBairro(String bairro)
public String getBairro()
public String getCEP()
public void setCEP(String CEP)
public String getComplemento()
public void setComplemento(String complemento)
```

## `Pessoa`

```java
protected Pessoa(Endereco endereco)
protected Pessoa(UUID id, String nome, String sobrenome, Date dataNascimento, String telefone, String email, Endereco endereco)
private static Endereco validarId(UUID id, Endereco endereco)
public Endereco getEndereco()
public final void setEndereco(Endereco novoEndereco)
public UUID getIdPessoa()
public UUID getId()
public void setId(UUID id)
public String getNome()
public void setNome(String nome)
public String getSobrenome()
public void setSobrenome(String sobrenome)
public Date getDataNascimento()
public void setDataNascimento(Date dataNascimento)
public String getTelefone()
public void setTelefone(String telefone)
public String getEmail()
public void setEmail(String email)
```

## `Usuario`

```java
protected Usuario(String emailCorporativo, String senha, Endereco endereco)
private static Endereco validarCadastro(String email, String senha, Endereco endereco)
private String resumo(String valor)
public UUID getIdUsuario()
public String getEmailCorporativo()
public void setEmailCorporativo(String emailCorporativo)
public boolean autenticar(String emailCorporativo, String senha)
public void alterarSenha(String senhaAtual, String novaSenha)
```

## `Aluno`

```java
public Aluno(String matricula, StatusMatricula status, Curso curso, Secretaria secretaria, String emailCorporativo, String senha, Endereco endereco)
private static Endereco validarDados(String matricula, StatusMatricula status, Curso curso, Secretaria secretaria, Endereco endereco)
public String getMatricula()
public void setMatricula(String matricula)
public StatusMatricula getStatus()
public void setStatus(StatusMatricula status)
public Curso getCurso()
public Secretaria getSecretaria()
public List<Inscricao> getInscricoes()
public List<Inscricao> getInscricoesAtivas()
public List<DisciplinaCursada> getHistorico()
void registrarInscricao(Inscricao inscricao)
void registrarHistorico(DisciplinaCursada registro)
void validarMatricula(Turma turma, TipoInscricao tipo)
public Inscricao matricular(Turma turma, TipoInscricao tipo, ServicoCobranca servicoCobranca)
public void cancelarMatricula(Inscricao inscricao)
public void trancarMatricula()
private void exigirAtivo()
```

## `Professor`

```java
public Professor(String codigo, Secretaria secretaria, String emailCorporativo, String senha, Endereco endereco)
private static Endereco validarDados(String codigo, Secretaria secretaria, Endereco endereco)
public String getCodigo()
public void setCodigo(String codigo)
public Secretaria getSecretaria()
public List<Turma> getTurmas()
void registrarTurma(Turma turma)
public List<Aluno> listarAlunosPorTurma(Turma turma)
public void validarEstrutura()
```

## `Secretaria`

```java
public Secretaria(String cargo, String emailCorporativo, String senha, Endereco endereco)
private static Endereco validarDados(String cargo, Endereco endereco)
public UUID getIdSecretaria()
public String getCargo()
public void setCargo(String cargo)
public List<Aluno> getAlunos()
public List<Professor> getProfessores()
public List<Disciplina> getDisciplinas()
public List<Curriculo> getCurriculos()
void registrarAluno(Aluno aluno)
void registrarProfessor(Professor professor)
void registrarDisciplina(Disciplina disciplina)
void registrarCurriculo(Curriculo curriculo)
public Curriculo gerarCurriculo(String semestre)
```

## `Curso`

```java
public Curso(String nome, String codigo, int creditoTotais)
public List<Aluno> getAlunos()
public List<Disciplina> getDisciplinas()
void registrarAluno(Aluno aluno)
void registrarDisciplina(Disciplina disciplina)
public void validarEstrutura()
public String getNome()
public void setNome(String nome)
public String getCodigo()
public void setCodigo(String codigo)
public int getCreditoTotais()
public void setCreditoTotais(int creditoTotais)
```

## `Curriculo`

```java
public Curriculo(String semestre, boolean isPeriodoMatriculaAberto, Secretaria secretaria)
public Secretaria getSecretaria()
public String getSemestre()
public boolean isPeriodoMatriculaAberto()
public void setPeriodoMatriculaAberto(boolean periodoMatriculaAberto)
```

## `Disciplina`

```java
public Disciplina(String nome, String codigo, int creditos, Curso curso, Secretaria secretaria)
public Curso getCurso()
public Secretaria getSecretaria()
public List<Turma> getTurmas()
public List<DisciplinaCursada> getHistoricos()
void registrarTurma(Turma turma)
void registrarHistorico(DisciplinaCursada historico)
public String getNome()
public void setNome(String nome)
public String getCodigo()
public void setCodigo(String codigo)
public int getCreditos()
public void setCreditos(int creditos)
```

## `DisciplinaCursada`

```java
public DisciplinaCursada(String semestre, boolean isAprovado, double notaFinal, Aluno aluno, Disciplina disciplina)
public Aluno getAluno()
public Disciplina getDisciplina()
public String getSemestre()
public void setSemestre(String semestre)
public boolean getIsAprovado()
public void setAprovado(boolean aprovado)
public double getNotaFinal()
public void setNotaFinal(double notaFinal)
```

## `Turma`

```java
public Turma(int limiteMaximo, int limiteMinimo, boolean isAtiva, String codigo, Disciplina disciplina, Professor professor, Curriculo curriculo)
private static void validarLimites(int minimo, int maximo)
public int getLimiteMaximo()
public void setLimiteMaximo(int limiteMaximo)
public int getLimiteMinimo()
public void setLimiteMinimo(int limiteMinimo)
public boolean isAtiva()
public boolean isTurmaAtiva()
public void setAtiva(boolean ativa)
public String getCodigo()
public void setCodigo(String codigo)
public Disciplina getDisciplina()
public Professor getProfessor()
public Curriculo getCurriculo()
public List<Inscricao> getInscricoes()
public List<Aluno> getAlunos()
void registrarInscricao(Inscricao inscricao)
void validarVaga()
public void fecharTurma()
```

## `Inscricao`

```java
public Inscricao(TipoInscricao tipoInscricao, LocalDate dataHora, Aluno aluno, Turma turma, ServicoCobranca servicoCobranca)
public TipoInscricao getTipoInscricao()
public LocalDate getDataHora()
public boolean isAtiva()
public Aluno getAluno()
public Turma getTurma()
public void setAtiva(boolean ativa)
void desativar()
public void enviarNotificaoAoSistemaCobranca()
```

## `ServicoCobranca`

```java
public ServicoCobranca()
public void notificarInscricoes(Inscricao inscricao)
public List<String> getNotificacoes()
public List<Inscricao> gerarCobranca(Aluno aluno, String semestre)
```

## `StatusMatricula`

```java
enum StatusMatricula
```

## `TipoInscricao`

```java
enum TipoInscricao
```
