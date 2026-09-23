# Sistema de Matrículas Universitárias

Projeto desenvolvido para a disciplina de **Laboratório de Desenvolvimento de Software** do curso de Engenharia de Software da PUC Minas.

O sistema informatiza o processo de matrícula semestral de uma universidade, conectando alunos, professores, secretaria e o sistema de cobrança.

---
## 1. Requisitos do sistema

### 1.1 Requisitos funcionais (RF):
- **RF-001:** O usuário autentica no sistema
- **RF-002:** A secretaria mantém o histórico de disciplinas cursadas dos alunos
- **RF-003:** A secretaria gera currículo
- **RF-004:** A secretaria mantém as disciplinas
- **RF-005:** A secretaria mantém os professores
- **RF-006:** A secretaria mantém os alunos
- **RF-007:** O aluno ativo efetua matrícula
- **RF-008:** O aluno ativo cancela matrícula
- **RF-009:** O aluno ativo tranca a matrícula
- **RF-010:** O professor consulta os alunos cadastrados em cada disciplina
- **RF-011:** O sistema de matrículas deve enviar uma notificação ao sistema de cobranças informando as inscrições de um aluno

### 1.2 Requisitos não funcionais (RNF):
- **RFN-001:** O sistema deve ser desenvolvido utilizando a linguagem de programação Java.
- **RFN-002:** O software deve possuir um mecanismo de persistência de dados.

Veja o [documento com requisitos do sistema e as regras de negócio](docs/sistema-matricula-requisitos-v2.pdf).
---
## 2. Histórias de Usuário (User Stories)


### 2.1 Histórias de Usuário - Aluno

**USER STORY 01**

* **Como um** aluno
* **eu quero** efetuar minha matrícula no sistema
* **para que** eu possa garantir minha vaga e participar das disciplinas do semestre.

**USER STORY 02**

* **Como um** aluno
* **eu quero** cancelar minha matrícula
* **para que** eu possa desistir da disciplina dentro do prazo permitido sem gerar pendências.

---

### 2.2 Histórias de Usuário - Secretaria

**USER STORY 03**

* **Como um** usuário da secretaria
* **eu quero** gerenciar os cadastros de alunos (incluir, alterar, consultar e remover)
* **para que** os dados acadêmicos dos estudantes estejam sempre atualizados.

**USER STORY 04**

* **Como um** usuário da secretaria
* **eu quero** gerenciar os cadastros de professores (incluir, alterar, consultar e remover)
* **para que** a alocação docente fique devidamente registrada no sistema.

**USER STORY 05**

* **Como um** usuário da secretaria
* **eu quero** gerar o currículo acadêmico
* **para que** a grade curricular do curso seja disponibilizada e atualizada para os alunos.

---

### 2.3 Histórias de Usuário - Professor

**USER STORY 06**

* **Como um** professor
* **eu quero** consultar a lista de alunos cadastrados em minhas turmas
* **para que** eu possa realizar o acompanhamento de presença e notas.
---

### 2.4 Histórias de Usuário Transversais (Usuário Geral / Sistema)

**USER STORY 07**

* **Como um** usuário do sistema (aluno, professor ou secretaria)
* **eu quero** me autenticar no sistema com login e senha
* **para que** eu tenha acesso seguro às funcionalidades específicas do meu perfil.

**USER STORY 08**

* **Como um** usuário do sistema
* **eu quero** que o sistema envie uma notificação ao Sistema de Cobrança ao efetuar ou alterar matrículas
* **para que** a emissão de boletos e o controle financeiro ocorram de forma automática.
  
---
## 3. Modelagem e Diagramas UML

### 3.1 Diagrama de Casos de Uso
Veja o [diagrama de casos de uso](docs/diagrama-casos-de-uso-v2.pdf) referente ao sistema.

### 3.2 Diagrama de Classes
Clique no link para ver o [diagrama de classes](.docs/diagrama-de-classe.pdf) do sistema.

---

## 🛠️ Tecnologias e Modelagem

* **Linguagem:** Java
* **Interface:** Linha de Comando (CLI)
* **Persistência:** Manipulação e gravação em arquivos
* **Modelagem de Software:** Diagramas de Casos de Uso, Histórias de Usuário e Diagramas de Classes (UML)

---

## 📂 Estrutura do Repositório

```text
├── docs/                 # Modelos UML, diagramas e histórias de usuário
├── src/                  # Código-fonte da aplicação Java
└── README.md             # Documentação principal
```

---

## 👥 Autores

* Caio César Falinacio dos Santos
* Luiz Fernando Cunha Maia
* Pedro Henrique Nogueira
