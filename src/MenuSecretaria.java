import java.util.List;
import java.util.Scanner;

/**
 * Menu da Secretaria — gerenciamento de todas as entidades academicas e financeiras.
 */
public final class MenuSecretaria {

    private final Secretaria secretaria;
    private final GerenciadorDados dados;
    private final Scanner sc;

    public MenuSecretaria(Secretaria secretaria, GerenciadorDados dados, Scanner sc) {
        this.secretaria = secretaria;
        this.dados = dados;
        this.sc = sc;
    }

    public void exibir() {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n=== Menu da Secretaria ===");
            System.out.println("--- Alunos ---");
            System.out.println(" 1. Cadastrar aluno");
            System.out.println(" 2. Consultar aluno");
            System.out.println(" 3. Alterar dados do aluno");
            System.out.println(" 4. Remover aluno");
            System.out.println("--- Professores ---");
            System.out.println(" 5. Cadastrar professor");
            System.out.println(" 6. Consultar professor");
            System.out.println(" 7. Alterar dados do professor");
            System.out.println(" 8. Remover professor");
            System.out.println("--- Disciplinas e Cursos ---");
            System.out.println(" 9. Cadastrar disciplina");
            System.out.println("10. Consultar disciplinas");
            System.out.println("11. Alterar disciplina");
            System.out.println("12. Remover disciplina");
            System.out.println("--- Curriculo ---");
            System.out.println("13. Gerar curriculo (semestre)");
            System.out.println("14. Abrir / fechar periodo de matricula");
            System.out.println("--- Historico Academico ---");
            System.out.println("15. Registrar disciplina cursada");
            System.out.println("16. Consultar historico de aluno");
            System.out.println("--- Cobranca ---");
            System.out.println("17. Configurar tabela de precos por credito");
            System.out.println("18. Gerar fatura de aluno");
            System.out.println("19. Consultar faturas de aluno");
            System.out.println("0. Sair");
            System.out.print("> ");
            switch (sc.nextLine().trim()) {
                case "1"  -> cadastrarAluno();
                case "2"  -> consultarAluno();
                case "3"  -> alterarAluno();
                case "4"  -> removerAluno();
                case "5"  -> cadastrarProfessor();
                case "6"  -> consultarProfessor();
                case "7"  -> alterarProfessor();
                case "8"  -> removerProfessor();
                case "9"  -> cadastrarDisciplina();
                case "10" -> consultarDisciplinas();
                case "11" -> alterarDisciplina();
                case "12" -> removerDisciplina();
                case "13" -> gerarCurriculo();
                case "14" -> alternarPeriodoMatricula();
                case "15" -> registrarDisciplinaCursada();
                case "16" -> consultarHistoricoAluno();
                case "17" -> configurarTabelaPrecos();
                case "18" -> gerarFatura();
                case "19" -> consultarFaturas();
                case "0"  -> continuar = false;
                default   -> System.out.println("Opcao invalida.");
            }
        }
    }

    // =========================================================================
    // ALUNOS
    // =========================================================================

    private void cadastrarAluno() {
        System.out.println("\n-- Cadastrar Aluno --");
        System.out.print("Matricula: ");          String matricula = sc.nextLine().trim();
        System.out.print("Email corporativo: ");  String email = sc.nextLine().trim();
        System.out.print("Senha: ");              String senha = sc.nextLine().trim();
        System.out.print("Codigo do curso: ");    String codCurso = sc.nextLine().trim();
        Curso curso = dados.getCursos().stream()
                .filter(c -> c.getCodigo().equals(codCurso)).findFirst().orElse(null);
        if (curso == null) { System.out.println("Curso nao encontrado."); return; }

        Endereco end = lerEndereco();
        try {
            Aluno a = new Aluno(matricula, StatusMatricula.ATIVA, curso, secretaria, email, senha, end);
            dados.getAlunos().add(a);
            System.out.println("Aluno cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void consultarAluno() {
        System.out.print("\nMatricula do aluno: ");
        String mat = sc.nextLine().trim();
        Aluno a = dados.getAlunos().stream()
                .filter(x -> x.getMatricula().equals(mat)).findFirst().orElse(null);
        if (a == null) { System.out.println("Aluno nao encontrado."); return; }
        System.out.printf("  Matricula: %s%n  Email: %s%n  Status: %s%n  Curso: %s%n",
                a.getMatricula(), a.getEmailCorporativo(), a.getStatus(), a.getCurso().getNome());
    }

    private void alterarAluno() {
        System.out.print("\nMatricula do aluno: ");
        String mat = sc.nextLine().trim();
        Aluno a = dados.getAlunos().stream()
                .filter(x -> x.getMatricula().equals(mat)).findFirst().orElse(null);
        if (a == null) { System.out.println("Aluno nao encontrado."); return; }
        System.out.print("Nova matricula (enter para manter): ");
        String novaMatricula = sc.nextLine().trim();
        if (!novaMatricula.isBlank()) a.setMatricula(novaMatricula);
        System.out.println("Dados atualizados.");
    }

    private void removerAluno() {
        System.out.print("\nMatricula do aluno a remover: ");
        String mat = sc.nextLine().trim();
        boolean removido = dados.getAlunos().removeIf(a -> a.getMatricula().equals(mat));
        System.out.println(removido ? "Aluno removido." : "Aluno nao encontrado.");
    }

    // =========================================================================
    // PROFESSORES
    // =========================================================================

    private void cadastrarProfessor() {
        System.out.println("\n-- Cadastrar Professor --");
        System.out.print("Codigo: ");             String codigo = sc.nextLine().trim();
        System.out.print("Email corporativo: ");  String email = sc.nextLine().trim();
        System.out.print("Senha: ");              String senha = sc.nextLine().trim();
        Endereco end = lerEndereco();
        try {
            Professor p = new Professor(codigo, secretaria, email, senha, end);
            dados.getProfessores().add(p);
            System.out.println("Professor cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void consultarProfessor() {
        System.out.print("\nCodigo do professor: ");
        String cod = sc.nextLine().trim();
        Professor p = dados.getProfessores().stream()
                .filter(x -> x.getCodigo().equals(cod)).findFirst().orElse(null);
        if (p == null) { System.out.println("Professor nao encontrado."); return; }
        System.out.printf("  Codigo: %s%n  Email: %s%n  Turmas: %d%n",
                p.getCodigo(), p.getEmailCorporativo(), p.getTurmas().size());
    }

    private void alterarProfessor() {
        System.out.print("\nCodigo do professor: ");
        String cod = sc.nextLine().trim();
        Professor p = dados.getProfessores().stream()
                .filter(x -> x.getCodigo().equals(cod)).findFirst().orElse(null);
        if (p == null) { System.out.println("Professor nao encontrado."); return; }
        System.out.print("Novo codigo (enter para manter): ");
        String novoCod = sc.nextLine().trim();
        if (!novoCod.isBlank()) p.setCodigo(novoCod);
        System.out.println("Dados atualizados.");
    }

    private void removerProfessor() {
        System.out.print("\nCodigo do professor a remover: ");
        String cod = sc.nextLine().trim();
        boolean removido = dados.getProfessores().removeIf(p -> p.getCodigo().equals(cod));
        System.out.println(removido ? "Professor removido." : "Professor nao encontrado.");
    }

    // =========================================================================
    // DISCIPLINAS E CURSOS
    // =========================================================================

    private void cadastrarDisciplina() {
        System.out.println("\n-- Cadastrar Disciplina --");
        System.out.print("Nome: ");               String nome = sc.nextLine().trim();
        System.out.print("Codigo: ");             String cod = sc.nextLine().trim();
        System.out.print("Creditos: ");           int cred = lerInt();
        System.out.print("Codigo do curso: ");    String codCurso = sc.nextLine().trim();
        Curso curso = dados.getCursos().stream()
                .filter(c -> c.getCodigo().equals(codCurso)).findFirst().orElse(null);
        if (curso == null) { System.out.println("Curso nao encontrado."); return; }
        try {
            Disciplina d = new Disciplina(nome, cod, cred, curso, secretaria);
            dados.getDisciplinas().add(d);
            System.out.println("Disciplina cadastrada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void consultarDisciplinas() {
        List<Disciplina> disc = dados.getDisciplinas();
        if (disc.isEmpty()) { System.out.println("Nenhuma disciplina cadastrada."); return; }
        System.out.println("\nDisciplinas:");
        disc.forEach(d -> System.out.printf("  • [%s] %s — %d credito(s) | Curso: %s%n",
                d.getCodigo(), d.getNome(), d.getCreditos(), d.getCurso().getNome()));
    }

    private void alterarDisciplina() {
        System.out.print("\nCodigo da disciplina: ");
        String cod = sc.nextLine().trim();
        Disciplina d = dados.getDisciplinas().stream()
                .filter(x -> x.getCodigo().equals(cod)).findFirst().orElse(null);
        if (d == null) { System.out.println("Disciplina nao encontrada."); return; }
        System.out.print("Novo nome (enter para manter): ");
        String nome = sc.nextLine().trim();
        if (!nome.isBlank()) d.setNome(nome);
        System.out.print("Novos creditos (-1 para manter): ");
        int cred = lerInt();
        if (cred >= 0) d.setCreditos(cred);
        System.out.println("Disciplina atualizada.");
    }

    private void removerDisciplina() {
        System.out.print("\nCodigo da disciplina a remover: ");
        String cod = sc.nextLine().trim();
        boolean removido = dados.getDisciplinas().removeIf(d -> d.getCodigo().equals(cod));
        System.out.println(removido ? "Disciplina removida." : "Disciplina nao encontrada.");
    }

    // =========================================================================
    // CURRICULO
    // =========================================================================

    private void gerarCurriculo() {
        System.out.print("\nSemestre (ex: 2026/2): ");
        String semestre = sc.nextLine().trim();
        try {
            Curriculo c = secretaria.gerarCurriculo(semestre);
            dados.getCurriculos().add(c);
            System.out.println("Curriculo gerado para " + semestre + " (periodo de matricula: ABERTO).");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void alternarPeriodoMatricula() {
        List<Curriculo> lista = dados.getCurriculos();
        if (lista.isEmpty()) { System.out.println("Nenhum curriculo cadastrado."); return; }
        System.out.println("\nCurriculos:");
        for (int i = 0; i < lista.size(); i++) {
            Curriculo c = lista.get(i);
            System.out.printf("  %d. %s — Periodo: %s%n", i + 1, c.getSemestre(),
                    c.isPeriodoMatriculaAberto() ? "ABERTO" : "FECHADO");
        }
        System.out.print("Numero do curriculo (0 para voltar): ");
        int idx = lerInt() - 1;
        if (idx < 0 || idx >= lista.size()) { System.out.println("Cancelado."); return; }
        Curriculo c = lista.get(idx);
        c.setPeriodoMatriculaAberto(!c.isPeriodoMatriculaAberto());
        System.out.println("Periodo agora: " + (c.isPeriodoMatriculaAberto() ? "ABERTO" : "FECHADO"));
    }

    // =========================================================================
    // HISTORICO ACADEMICO
    // =========================================================================

    private void registrarDisciplinaCursada() {
        System.out.println("\n-- Registrar Disciplina Cursada --");
        System.out.print("Matricula do aluno: ");
        String mat = sc.nextLine().trim();
        Aluno aluno = dados.getAlunos().stream()
                .filter(a -> a.getMatricula().equals(mat)).findFirst().orElse(null);
        if (aluno == null) { System.out.println("Aluno nao encontrado."); return; }

        System.out.print("Codigo da disciplina: ");
        String codDisc = sc.nextLine().trim();
        Disciplina disc = dados.getDisciplinas().stream()
                .filter(d -> d.getCodigo().equals(codDisc)).findFirst().orElse(null);
        if (disc == null) { System.out.println("Disciplina nao encontrada."); return; }

        System.out.print("Semestre (ex: 2026/1): ");  String sem = sc.nextLine().trim();
        System.out.print("Nota final: ");              double nota = lerDouble();
        System.out.print("Aprovado? (s/n): ");         boolean ap = sc.nextLine().trim().equalsIgnoreCase("s");

        try {
            DisciplinaCursada dc = new DisciplinaCursada(sem, ap, nota, aluno, disc);
            dados.getDisciplinasCursadas().add(dc);
            System.out.println("Historico registrado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void consultarHistoricoAluno() {
        System.out.print("\nMatricula do aluno: ");
        String mat = sc.nextLine().trim();
        Aluno aluno = dados.getAlunos().stream()
                .filter(a -> a.getMatricula().equals(mat)).findFirst().orElse(null);
        if (aluno == null) { System.out.println("Aluno nao encontrado."); return; }
        List<DisciplinaCursada> hist = aluno.getHistorico();
        if (hist.isEmpty()) { System.out.println("Historico vazio."); return; }
        System.out.println("\nHistorico de " + aluno.getMatricula() + ":");
        hist.forEach(h -> System.out.printf("  • %s | %s | Nota: %.1f | %s%n",
                h.getSemestre(), h.getDisciplina().getNome(),
                h.getNotaFinal(), h.getIsAprovado() ? "APROVADO" : "REPROVADO"));
    }

    // =========================================================================
    // COBRANCA
    // =========================================================================

    private void configurarTabelaPrecos() {
        TabelaPrecos tp = dados.getTabelaPrecos();
        System.out.println("\n-- Configurar Tabela de Precos --");
        System.out.printf("Valor atual por credito OBRIGATORIA: R$ %.2f%n", tp.getValorCreditoObrigatoria());
        System.out.printf("Valor atual por credito OPTATIVA:    R$ %.2f%n", tp.getValorCreditoOptativa());
        System.out.print("Novo valor por credito OBRIGATORIA (enter para manter): ");
        String vo = sc.nextLine().trim();
        if (!vo.isBlank()) {
            try { tp.setValorCreditoObrigatoria(Double.parseDouble(vo)); }
            catch (NumberFormatException e) { System.out.println("Valor invalido, mantido."); }
        }
        System.out.print("Novo valor por credito OPTATIVA (enter para manter): ");
        String vop = sc.nextLine().trim();
        if (!vop.isBlank()) {
            try { tp.setValorCreditoOptativa(Double.parseDouble(vop)); }
            catch (NumberFormatException e) { System.out.println("Valor invalido, mantido."); }
        }
        System.out.printf("Precos atualizados: OBRIGATORIA R$ %.2f | OPTATIVA R$ %.2f%n",
                tp.getValorCreditoObrigatoria(), tp.getValorCreditoOptativa());
    }

    private void gerarFatura() {
        System.out.print("\nMatricula do aluno: ");
        String mat = sc.nextLine().trim();
        Aluno aluno = dados.getAlunos().stream()
                .filter(a -> a.getMatricula().equals(mat)).findFirst().orElse(null);
        if (aluno == null) { System.out.println("Aluno nao encontrado."); return; }
        System.out.print("Semestre (ex: 2026/2): ");
        String sem = sc.nextLine().trim();
        Fatura f = dados.getServicoCobranca().gerarFatura(aluno, sem);
        System.out.printf("Fatura gerada! Total: R$ %.2f | Vencimento: %s | Status: %s%n",
                f.getValorTotal(), f.getDataVencimento(), f.getStatus());
        f.getItens().forEach(i -> System.out.printf("  • %s — R$ %.2f%n",
                i.getDescricao(), i.getValor()));
    }

    private void consultarFaturas() {
        System.out.print("\nMatricula do aluno: ");
        String mat = sc.nextLine().trim();
        Aluno aluno = dados.getAlunos().stream()
                .filter(a -> a.getMatricula().equals(mat)).findFirst().orElse(null);
        if (aluno == null) { System.out.println("Aluno nao encontrado."); return; }
        List<Fatura> faturas = dados.getServicoCobranca().consultarFaturas(aluno);
        if (faturas.isEmpty()) { System.out.println("Nenhuma fatura encontrada."); return; }
        faturas.forEach(f -> System.out.printf(
                "  • Semestre: %s | Total: R$ %.2f | Vencimento: %s | Status: %s%n",
                f.getSemestre(), f.getValorTotal(), f.getDataVencimento(), f.getStatus()));
    }

    // =========================================================================
    // Utilitarios
    // =========================================================================

    private Endereco lerEndereco() {
        System.out.print("Rua: ");          String rua = sc.nextLine().trim();
        System.out.print("Numero: ");       String num = sc.nextLine().trim();
        System.out.print("Cidade: ");       String cid = sc.nextLine().trim();
        System.out.print("Bairro: ");       String bai = sc.nextLine().trim();
        System.out.print("CEP: ");          String cep = sc.nextLine().trim();
        System.out.print("Complemento: "); String comp = sc.nextLine().trim();
        return new Endereco(rua, num, cid, bai, cep, comp);
    }

    private int lerInt() {
        try { return Integer.parseInt(sc.nextLine().trim()); }
        catch (NumberFormatException e) { return -1; }
    }

    private double lerDouble() {
        try { return Double.parseDouble(sc.nextLine().trim()); }
        catch (NumberFormatException e) { return 0.0; }
    }
}
