package xadrezia;

public abstract class MotorIA {
    private final Peca.COR cor;
    private final Tabuleiro tabuleiro;

    public MotorIA(Tabuleiro tabuleiro, Peca.COR cor) {
        this.cor = cor;
        this.tabuleiro = tabuleiro;
    }
    
    public Peca.COR getCor() {
        return cor;
    }
    
    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }
    
    public abstract Movimento getProximaJogada();
    
    /**
     * @param posPeaoX
     * @param posPeaoY
     * @return para qual peça um peão deverá ser promovido
     */
    // TODO atualmente, toda promoção está sendo feita para rainha
    //public abstract Peca getPromocao(int posPeaoX, int posPeaoY);
}
