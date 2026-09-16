package xadrezia;

public class Peca implements Cloneable {

  /**
   * Cores possíveis
   */
  public static enum COR {

    BRANCA, PRETA
  };

  /**
   * Tipos possíveis
   */
  public static enum TIPO {

    PEAO, CAVALO, BISPO, TORRE, RAINHA, REI
  };

  private final COR cor;
  private final TIPO tipo;
  private boolean movimentada;

  /**
   * Construtor completo
   *
   * @param cor
   * @param tipo
   */
  public Peca(COR cor, TIPO tipo) {
    this(cor, tipo, false);
  }

  /**
   * Construtor completo
   *
   * @param cor
   * @param tipo
   * @param movimentada
   */
  public Peca(COR cor, TIPO tipo, boolean movimentada) {
    this.cor = cor;
    this.tipo = tipo;
    this.movimentada = movimentada;
  }

  /**
   * Override para impressão
   *
   * @return sigla da peça
   */
  @Override
  public String toString() {
    String cor, tipo;

    switch (this.getCor()) {
      case BRANCA:
        cor = "W";
        break;

      case PRETA:
        cor = "B";
        break;

      default:
        throw new IllegalArgumentException("Cor de peça inválida");
    }

    switch (this.getTipo()) {
      case PEAO:
        tipo = "P";// Pawn
        break;

      case CAVALO:
        tipo = "N";// kNight
        break;

      case BISPO:
        tipo = "B";// Bishop
        break;

      case TORRE:
        tipo = "R";// Rook
        break;

      case RAINHA:
        tipo = "Q";// Queen
        break;

      case REI:
        tipo = "K";// King
        break;

      default:
        throw new IllegalArgumentException("Tipo de peça inválida");
    }

    return cor + tipo;
  }

  /**
   * @return clone da peça
   */
  protected Peca clone() {
    return new Peca(cor, tipo, movimentada);

  }

  /**
   * @return cor da peça
   */
  public COR getCor() {
    return cor;
  }

  /**
   * @return tipo da peça
   */
  public TIPO getTipo() {
    return tipo;
  }

  /**
   * @return true caso a peça tenha realizado algum movimento
   */
  public boolean isMovimentada() {
    return movimentada;
  }

  /**
   * @param movimentada define a situação de movimento da peça em questão
   */
  public void setMovimentada(boolean movimentada) {
    this.movimentada = movimentada;
  }

  /**
   * @return valor da peça em questão
   */
  public int getValor() {
    switch (tipo) {
      case PEAO:
        return 1;

      case CAVALO:
        return 3;

      case BISPO:
        return 3;

      case TORRE:
        return 5;

      case RAINHA:
        return 9;

      case REI:
        return 40;
    }
    
    return 0;
  }
}
