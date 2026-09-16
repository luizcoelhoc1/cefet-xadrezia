package xadrezia;

import java.awt.Point;

public class Movimento {

  private Point origem;
  private Point destino;

  /**
   * Instancia um novo movimento baseado em pontos de origem e ponto de destino, considerando o tabuleiro uma matriz
   * bidimensional 8x8
   *
   * @param origem
   * @param destino
   */
  public Movimento(Point origem, Point destino) {
    if ((origem.x < 0) || (origem.x > 7)
            || (origem.y < 0) || (origem.y > 7)) {
      throw new IllegalArgumentException("Origem fora do tabuleiro.");
    }

    if ((destino.x < 0) || (destino.x > 7)
            || (destino.y < 0) || (destino.y > 7)) {
      throw new IllegalArgumentException("Destino fora do tabuleiro.");
    }

    if (origem.equals(destino)) {
      throw new IllegalArgumentException("Não é um movimento válido (origem é igual ao destino).");
    }

    this.origem = origem;
    this.destino = destino;
  }

  /**
   * Realiza um movimento com notação de xadrez
   *
   * Ex: Movimento("c2", "c4") = Movimento([2, 6], [2, 4])
   *
   * @param origem
   * @param destino
   */
  public Movimento(String origem, String destino) {
    if (origem.length() != 2) {
      throw new IllegalArgumentException("Número inválido de parâmetros de origem");
    }

    if (destino.length() != 2) {
      throw new IllegalArgumentException("Número inválido de parâmetros de destino");
    }

    int origemX = origem.charAt(0) - 'a';
    if ((origemX < 0) || (origemX > 7)) {
      throw new IllegalArgumentException("Parâmetro X de origem inválido");
    }

    int origemY = '8' - origem.charAt(1);
    if ((origemY < 0) || (origemY > 7)) {
      throw new IllegalArgumentException("Parâmetro Y de origem inválido");
    }

    Point pOrigem = new Point(origemX, origemY);

    int destinoX = destino.charAt(0) - 'a';
    if ((destinoX < 0) || (destinoX > 7)) {
      throw new IllegalArgumentException("Parâmetro X de destino inválido");
    }

    int destinoY = '8' - destino.charAt(1);
    if ((destinoY < 0) || (destinoY > 7)) {
      throw new IllegalArgumentException("Parâmetro Y de destino inválido");
    }

    Point pDestino = new Point(destinoX, destinoY);

    if (origem.equals(destino)) {
      throw new IllegalArgumentException("Não é um movimento válido (origem é igual ao destino).");
    }
    
    this.origem = pOrigem;
    this.destino = pDestino;
  }

  /**
   * @return the origem
   */
  public Point getOrigem() {
    return origem;
  }

  /**
   * @return the destino
   */
  public Point getDestino() {
    return destino;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    sb.append(((char) ('a' + getOrigem().x)));
    sb.append(", ");
    sb.append(8 - getOrigem().y);
    sb.append("] -> [");
    sb.append(((char) ('a' + getDestino().x)));
    sb.append(", ");
    sb.append(8 - getDestino().y);
    sb.append("]");
    return sb.toString();
  }
}
