public class Inscricao {
    private TipoInscricao tipoInscricao;
    private LocalDate dataHora;
    private boolean isAtiva;

    public Inscricao(TipoInscricao tipoInscricao, LocalDate dataHora, boolean isAtiva) {
        this.tipoInscricao = tipoInscricao;
        this.dataHora = dataHora;
        this.isAtiva = isAtiva;
    }

    public TipoInscricao getTipoInscricao() {
        return tipoInscricao;
    }
    public void setTipoInscricao(TipoInscricao tipoInscricao) {
        this.tipoInscricao = tipoInscricao;
    }
    public LocalDate getDataHora() {
        return dataHora;
    }
    public void setDataHora(LocalDate dataHora) {
        this.dataHora = dataHora;
    }
    public boolean isAtiva() {
        return isAtiva;
    }
    public void setAtiva(boolean ativa) {
        isAtiva = ativa;
    }
    
}
