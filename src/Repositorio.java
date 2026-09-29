import java.util.List;

/**
 * Contrato generico para persistencia de entidades em qualquer mecanismo de armazenamento.
 * As implementacoes concretas usam arquivos CSV (Subsistema 6).
 *
 * @param <T> o tipo da entidade gerenciada por este repositorio
 */
public interface Repositorio<T> {

    /** Persiste uma entidade (acrescenta ou sobrescreve se ja existir). */
    void salvar(T entidade);

    /** Substitui o conteudo completo do armazenamento por esta lista. */
    void salvarTodos(List<T> entidades);

    /** Retorna todas as entidades armazenadas. */
    List<T> carregarTodos();

    /** Remove uma entidade do armazenamento. */
    void remover(T entidade);

    /** Apaga todo o conteudo do armazenamento. */
    void limpar();
}
