# Implementação do diagrama de classes

Referência: `class-diagram-v1.pdf`, fornecido pelo usuário em 22/09/2026.

## Adaptações aprovadas

- `Usuario` abstrato herda de `Pessoa` abstrata. Aluno, Professor e Secretaria herdam de Usuario.
- Um aluno possui vários registros de DisciplinaCursada; cada registro pertence a um aluno e uma disciplina.
- Métodos recebem os parâmetros necessários; `toString()` retorna String, conforme Java.

## Plano de execução

1. Testar os fluxos de autenticação, relações, histórico, matrícula, cancelamento e cobrança antes de implementar.
2. Completar Endereco, Pessoa e Usuario e seus atributos, operações e associação 1:1.
3. Completar StatusMatricula, Secretaria, Curso, Curriculo, Disciplina e Professor.
4. Completar Aluno, Turma, Inscricao, DisciplinaCursada e ServicoCobranca.
5. Compilar com javac e executar os testes de comportamento sem dependências externas.
6. Revisar o mapeamento do diagrama, registrar cada classe em um commit próprio e enviar os commits.

## Decisões de implementação

As referências obrigatórias são recebidas nos construtores. As coleções são expostas somente para leitura. O registro das associações ocorre na construção, mantendo os dois lados sincronizados. Curso e Professor permitem a montagem inicial de suas coleções; `validarEstrutura()` verifica o mínimo de uma disciplina e uma turma, respectivamente. A turma aceita inscrições durante a montagem; o mínimo de três alunos é verificado ao fechar a turma após o período de matrícula.

As regras complementares de matrícula vêm de `docs/sistema-matricula-requisitos-v2.pdf`: quatro obrigatórias, duas optativas, período aberto, aluno ativo, máximo de 60 e mínimo de três ao encerrar.

Os limites de inscrições são avaliados por semestre. Inscrições anteriores são mantidas para consulta e cobrança do respectivo período. Trancar matrícula exige aluno ativo; o documento restringe o período apenas para realizar/cancelar inscrições, sem impor essa restrição ao trancamento. Trancamento e alteração administrativa para status não ativo desativam as inscrições e notificam o serviço de cobrança.

Os construtores anteriores que não recebiam os vínculos obrigatórios foram substituídos. Referências de proprietário, tipo/data da inscrição e semestre do currículo não possuem setters, para evitar inconsistências posteriores. `TipoInscricao` já correspondia ao diagrama e foi preservado.

O diagrama não fornece integração de CEP, endpoint de cobrança nem valores monetários. A busca por CEP consulta uma coleção local; ServicoCobranca registra notificações em memória e gera a relação de inscrições cobráveis por semestre. Não realiza pagamentos nem envia mensagens externas. CLI e persistência são requisitos do sistema completo, fora desta implementação do modelo de classes.
