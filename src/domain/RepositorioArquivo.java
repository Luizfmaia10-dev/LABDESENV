package domain;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Implementacao base de Repositorio que persiste entidades em arquivos CSV (delimitador: ';').
 *
 * Cada subclasse precisa implementar:
 *  - getCaminhoArquivo(): retorna o path relativo ao projeto (ex: "data/alunos.csv")
 *  - serializar(T): converte uma entidade para uma linha CSV
 *  - desserializar(String linha, Map contexto): reconstroi uma entidade a partir de uma linha CSV
 *    usando o mapa de contexto para resolver referencias por ID
 *
 * Linhas que comecam com '#' sao tratadas como comentarios e ignoradas na leitura.
 *
 * @param <T> o tipo da entidade gerenciada
 */
public abstract class RepositorioArquivo<T> implements Repositorio<T> {

    private static final String DELIMITADOR = ";";
    protected static final String DELIMITADOR_REGEX = ";";

    /** Retorna o caminho relativo do arquivo CSV (ex: "data/alunos.csv"). */
    protected abstract String getCaminhoArquivo();

    /**
     * Converte uma entidade para uma linha CSV.
     * Os campos devem estar na mesma ordem esperada por {@link #desserializar}.
     */
    protected abstract String serializar(T entidade);

    /**
     * Reconstroi uma entidade a partir de uma linha CSV.
     * O mapa {@code contexto} contem todas as entidades ja carregadas, indexadas por ID (UUID String).
     * Isso permite resolver referencias entre entidades sem dependencia circular.
     */
    protected abstract T desserializar(String[] campos, Map<String, Object> contexto);

    // -------------------------------------------------------------------------
    // Implementacao de Repositorio<T>
    // -------------------------------------------------------------------------

    @Override
    public void salvar(T entidade) {
        List<T> todos = new ArrayList<>(carregarTodos());
        todos.removeIf(e -> mesmId(e, entidade));
        todos.add(entidade);
        salvarTodos(todos);
    }

    @Override
    public void salvarTodos(List<T> entidades) {
        garantirDiretorio();
        File arquivo = new File(getCaminhoArquivo());
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(
                new FileWriter(arquivo, false)))) {
            pw.println("# " + getCaminhoArquivo() + " â€” gerado automaticamente");
            for (T entidade : entidades) {
                pw.println(serializar(entidade));
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gravar " + getCaminhoArquivo(), e);
        }
    }

    @Override
    public List<T> carregarTodos() {
        return carregarTodos(Collections.emptyMap());
    }

    /**
     * Versao com contexto â€” usada pelo GerenciadorDados para resolver referencias entre entidades.
     */
    public List<T> carregarTodos(Map<String, Object> contexto) {
        garantirDiretorio();
        File arquivo = new File(getCaminhoArquivo());
        List<T> resultado = new ArrayList<>();
        if (!arquivo.exists()) return resultado;
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty() || linha.startsWith("#")) continue;
                String[] campos = linha.split(DELIMITADOR_REGEX, -1);
                T entidade = desserializar(campos, contexto);
                if (entidade != null) resultado.add(entidade);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler " + getCaminhoArquivo(), e);
        }
        return resultado;
    }

    @Override
    public void remover(T entidade) {
        List<T> todos = new ArrayList<>(carregarTodos());
        todos.removeIf(e -> mesmId(e, entidade));
        salvarTodos(todos);
    }

    @Override
    public void limpar() {
        File arquivo = new File(getCaminhoArquivo());
        if (arquivo.exists()) arquivo.delete();
    }

    // -------------------------------------------------------------------------
    // Utilitarios
    // -------------------------------------------------------------------------

    /** Escapa um campo para CSV: substitui ';' interno por '|' para nao quebrar o parse. */
    protected String esc(String valor) {
        if (valor == null) return "";
        return valor.replace(DELIMITADOR, "|");
    }

    /**
     * Verifica se duas entidades representam o mesmo registro.
     * Subclasses podem sobrescrever se precisarem de logica diferente de equals.
     */
    protected boolean mesmId(T a, T b) {
        return a == b;
    }

    private void garantirDiretorio() {
        File dir = new File(getCaminhoArquivo()).getParentFile();
        if (dir != null && !dir.exists()) dir.mkdirs();
    }
}

