package xadrezia;

import java.awt.Point;
import java.util.AbstractMap;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import static xadrezia.Tabuleiro.SITUACAO_EMPATE.Maximo_Jogadas;

public class Tabuleiro implements Cloneable, Iterable<Movimento> {

  private Peca[][] tabuleiro;
  private Movimento movimentoAnterior; // Usado para En Passant
  private Peca.COR turno;// Usado para turnos
  private int numeroJogadas;

  /**
   * Cria um tabuleiro vazio
   */
  public Tabuleiro() {
    this(false);
  }

  /**
   * @param vazio true se não devem ser colocadas peças
   */
  private Tabuleiro(boolean vazio) {
    this(new Peca[8][8], Peca.COR.BRANCA, 0);

    if (vazio) {
      for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
          tabuleiro[i][j] = null;
        }
      }
      return;
    }

    for (int j = 2; j < 6; j++) {
      for (int i = 0; i < 8; i++) {
        tabuleiro[i][j] = null;
      }
    }

    for (int i = 0; i < 8; i++) {
      tabuleiro[i][1] = new Peca(Peca.COR.PRETA, Peca.TIPO.PEAO);
      tabuleiro[i][6] = new Peca(Peca.COR.BRANCA, Peca.TIPO.PEAO);
    }

    tabuleiro[0][0] = new Peca(Peca.COR.PRETA, Peca.TIPO.TORRE);
    tabuleiro[7][0] = new Peca(Peca.COR.PRETA, Peca.TIPO.TORRE);
    tabuleiro[0][7] = new Peca(Peca.COR.BRANCA, Peca.TIPO.TORRE);
    tabuleiro[7][7] = new Peca(Peca.COR.BRANCA, Peca.TIPO.TORRE);

    tabuleiro[1][0] = new Peca(Peca.COR.PRETA, Peca.TIPO.CAVALO);
    tabuleiro[6][0] = new Peca(Peca.COR.PRETA, Peca.TIPO.CAVALO);
    tabuleiro[1][7] = new Peca(Peca.COR.BRANCA, Peca.TIPO.CAVALO);
    tabuleiro[6][7] = new Peca(Peca.COR.BRANCA, Peca.TIPO.CAVALO);

    tabuleiro[2][0] = new Peca(Peca.COR.PRETA, Peca.TIPO.BISPO);
    tabuleiro[5][0] = new Peca(Peca.COR.PRETA, Peca.TIPO.BISPO);
    tabuleiro[2][7] = new Peca(Peca.COR.BRANCA, Peca.TIPO.BISPO);
    tabuleiro[5][7] = new Peca(Peca.COR.BRANCA, Peca.TIPO.BISPO);

    tabuleiro[3][0] = new Peca(Peca.COR.PRETA, Peca.TIPO.RAINHA);
    tabuleiro[4][0] = new Peca(Peca.COR.PRETA, Peca.TIPO.REI);
    tabuleiro[3][7] = new Peca(Peca.COR.BRANCA, Peca.TIPO.RAINHA);
    tabuleiro[4][7] = new Peca(Peca.COR.BRANCA, Peca.TIPO.REI);
  }

  /**
   * Cria um novo objeto tabuleiro com o posicionamento de peças previamente definido
   *
   * @param tabuleiro
   */
  private Tabuleiro(Peca[][] tabuleiro, Peca.COR turno, int numeroJogadas) {
    this.tabuleiro = tabuleiro;
    this.turno = turno;
    this.numeroJogadas = numeroJogadas;
  }

  /**
   * @return clone do tabuleiro corrente
   * @throws java.lang.CloneNotSupportedException
   */
  @Override
  public Tabuleiro clone() {
    Peca[][] tabuleiroClone = new Peca[8][8];
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        tabuleiroClone[i][j] = tabuleiro[i][j] == null ? null : tabuleiro[i][j].clone();
      }
    }
    return new Tabuleiro(tabuleiroClone, turno, numeroJogadas);
  }

  /**
   * @param posicao
   * @return Peça na referida posição
   */
  public Peca getPeca(Point posicao) {
    assert (posicao != null) : "Posição inválida";
    assert (tabuleiro != null) : "Tabuleiro inválido";
    return tabuleiro[posicao.x][posicao.y];
  }

  /**
   * @param x
   * @param y
   * @return Peça na referida posição
   */
  public Peca getPeca(int x, int y) {
    return tabuleiro[x][y];
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("   a  b  c  d  e  f  g  h\n");
    int linha = 8;
    for (int j = 0; j < 8; j++) {
      sb.append(linha).append(" ");
      for (int i = 0; i < 8; i++) {
        if (tabuleiro[i][j] == null) {
          sb.append("  ");
        } else {
          sb.append(tabuleiro[i][j]);
        }
        sb.append(" ");
      }
      sb.append(linha).append("\n");
      linha--;
    }
    sb.append("   a  b  c  d  e  f  g  h\n");

    return sb.toString();
  }

  /**
   * Imprime o tabuleiro corrente
   */
  public void imprimir() {
    System.out.println(this);
  }

  /**
   * Realiza o movimento especificado no tabuleiro
   *
   * @param movimento
   */
  public void doMovimento(Movimento movimento) {
    doMovimento(movimento, true);
  }

  /**
   * Realiza o movimento especificado no tabuleiro
   *
   * @param movimento
   * @param checarPossibilidade true verifica se o movimento é possível de ser realizado
   */
  private void doMovimento(Movimento movimento, boolean checarPossibilidade) {
    if (checarPossibilidade && !isMovimentoPossivel(movimento)) {
      throw new IllegalArgumentException("Movimento impossível!" + "\n" + movimento.toString() + "\n" + this.toString());
    }

    Peca p = tabuleiro[movimento.getOrigem().x][movimento.getOrigem().y];
    if ((p.getTipo() == Peca.TIPO.REI)
            && (Math.abs(movimento.getDestino().x - movimento.getOrigem().x) == 2)) {

      // Roque (previamente verificado pelo isMovimentoPossivel
      if (movimento.getDestino().x < movimento.getOrigem().x) {
        // Roque à esquerda

        // Movimenta Torre
        int y = movimento.getOrigem().y;
        tabuleiro[0][y].setMovimentada(true);
        tabuleiro[3][y] = tabuleiro[0][y];
        tabuleiro[0][y] = null;

        // Movimento do rei feito normalmente ao final
      } else {
        // Roque à direita

        // Movimenta torre
        int y = movimento.getOrigem().y;
        tabuleiro[7][y].setMovimentada(true);
        tabuleiro[5][y] = tabuleiro[7][y];
        tabuleiro[7][y] = null;

        // Movimento do rei feito normalmente ao final
      }
    } else if ((p.getTipo() == Peca.TIPO.PEAO)
            && (Math.abs(movimento.getDestino().x - movimento.getOrigem().x) == 1)
            && (getPeca(movimento.getDestino()) == null)) {
      // Captura En Passant
      assert (tabuleiro[movimento.getDestino().x][movimento.getOrigem().y] != null) : "En passant absurdo!\n" + this + "\n\nDe: ["
              + movimento.getOrigem().x + ", " + movimento.getOrigem().y + "] para [" + movimento.getDestino().x + ", "
              + movimento.getDestino().y + "]";
      tabuleiro[movimento.getDestino().x][movimento.getOrigem().y] = null;
    } else if ((p.getTipo() == Peca.TIPO.PEAO)
            && (((movimento.getDestino().y == 0) && (p.getCor() == Peca.COR.BRANCA))
            || (((movimento.getDestino().y == 7) && (p.getCor() == Peca.COR.PRETA))))) {
      // Promoção de peão

      // TODO Possibilitar outras promoções, por padrão toda promoção de peão será para Rainha
      Peca promocao = new Peca(p.getCor(), Peca.TIPO.RAINHA, true);
      setPeca(movimento.getOrigem().x, movimento.getOrigem().y, promocao);
      p = promocao;
    }

    movimentoAnterior = movimento;
    p.setMovimentada(true);
    tabuleiro[movimento.getDestino().x][movimento.getDestino().y] = tabuleiro[movimento.getOrigem().x][movimento.getOrigem().y];
    tabuleiro[movimento.getOrigem().x][movimento.getOrigem().y] = null;

    // Define turno do próximo jogador
    alternarTurno();

    // Incrementa em 1 o número de jogadas
    numeroJogadas++;
  }

  /**
   * @param movimento
   * @return true se o movimento é possível para um peão (não verifica xeque)
   * @see Testado
   */
  private boolean isMovimentoPossivelPeao(Movimento movimento) {
    Peca.COR cor = getPeca(movimento.getOrigem()).getCor();

    if (cor == Peca.COR.BRANCA) {
      // Peça branca, move-se para cima

      if (movimento.getOrigem().x == movimento.getDestino().x) {
        // Movimento de 1 casa para cima
        // Testado
        if ((movimento.getDestino().y == (movimento.getOrigem().y - 1))
                && (getPeca(movimento.getDestino()) == null)) {
          return true;
        }

        // Se  o movimento for o inicial, pode ser duas casas para cima
        // Testado
        if ((movimento.getOrigem().y == 6)
                && (movimento.getDestino().y == (movimento.getOrigem().y - 2))
                && (getPeca(movimento.getDestino()) == null)
                && (getPeca(movimento.getDestino().x, movimento.getOrigem().y - 1) == null)) {
          return true;
        }
      } else if ((Math.abs(movimento.getDestino().x - movimento.getOrigem().x) == 1)
              && (movimento.getDestino().y == (movimento.getOrigem().y - 1))) {
        // Captura Simples
        // Testado
        if ((getPeca(movimento.getDestino()) != null)
                && (getPeca(movimento.getDestino()).getCor() != getPeca(movimento.getOrigem()).getCor())) {
          return true;
        }

        // Captura En Passant
        // Testado
        if ((movimento.getDestino().y == 2)
                && movimentoAnterior != null
                && movimentoAnterior.getOrigem().equals(new Point(movimento.getDestino().x, 1))
                && movimentoAnterior.getDestino().equals(new Point(movimento.getDestino().x, 3))) {
          return true;
        }
      }
    } else {
      // Peça preta, move-se para baixo

      if (movimento.getOrigem().x == movimento.getDestino().x) {
        // Movimento de 1 casa para baixo
        // Testado
        if ((movimento.getDestino().y == (movimento.getOrigem().y + 1))
                && (getPeca(movimento.getDestino()) == null)) {
          return true;
        }

        // Se  o movimento for o inicial, pode ser duas casas para baixo
        // Testado
        if ((movimento.getOrigem().y == 1)
                && (movimento.getDestino().y == (movimento.getOrigem().y + 2))
                && (getPeca(movimento.getDestino()) == null)
                && (getPeca(movimento.getDestino().x, movimento.getOrigem().y + 1) == null)) {
          return true;
        }

      } else if ((Math.abs(movimento.getDestino().x - movimento.getOrigem().x) == 1)
              && (movimento.getDestino().y == (movimento.getOrigem().y + 1))) {
        // Captura Simples
        // Testado
        if ((getPeca(movimento.getDestino()) != null)
                && (getPeca(movimento.getDestino()).getCor() != getPeca(movimento.getOrigem()).getCor())) {
          return true;
        }

        // Captura En Passant
        // Testado
        if ((movimento.getDestino().y == 5)
                && movimentoAnterior != null
                && movimentoAnterior.getOrigem().equals(new Point(movimento.getDestino().x, 6))
                && movimentoAnterior.getDestino().equals(new Point(movimento.getDestino().x, 4))) {
          return true;
        }
      }

    }

    return false;
  }

  /**
   * @param movimento
   * @return true se o movimento é possível para um cavalo (não verifica xeque)
   * @see Testado
   */
  private boolean isMovimentoPossivelCavalo(Movimento movimento) {
    if (movimento.getOrigem().x == movimento.getDestino().x) {
      return false;
    }

    if (movimento.getOrigem().y == movimento.getDestino().y) {
      return false;
    }

    return (((Math.abs(movimento.getOrigem().x - movimento.getDestino().x)
            + Math.abs(movimento.getOrigem().y - movimento.getDestino().y)) == 3)
            && ((getPeca(movimento.getDestino()) == null)
            || (getPeca(movimento.getDestino()).getCor() != getPeca(movimento.getOrigem()).getCor())));
  }

  /**
   * @param movimento
   * @return true se o movimento é possível para um bispo (não verifica xeque)
   * @see Testado
   */
  private boolean isMovimentoPossivelBispo(Movimento movimento) {
    if (Math.abs(movimento.getDestino().x - movimento.getOrigem().x) != Math.abs(movimento.getDestino().y - movimento.getOrigem().y)) {
      return false;
    }

    int incX = (movimento.getDestino().x - movimento.getOrigem().x) > 0 ? 1 : -1;
    int incY = (movimento.getDestino().y - movimento.getOrigem().y) > 0 ? 1 : -1;
    Point posAtual = new Point(movimento.getOrigem().x + incX, movimento.getOrigem().y + incY);
    while (!posAtual.equals(movimento.getDestino())) {
      if (getPeca(posAtual) != null) {
        return false;
      }
      posAtual = new Point(posAtual.x + incX, posAtual.y + incY);
    }

    return ((getPeca(posAtual) == null) || (getPeca(posAtual).getCor() != getPeca(movimento.getOrigem()).getCor()));
  }

  /**
   * @param movimento
   * @return true se o movimento é possível para uma torre (não verifica xeque)
   * @see Testado
   */
  private boolean isMovimentoPossivelTorre(Movimento movimento) {
    int incX = 0;
    int incY = 0;
    if (movimento.getOrigem().x == movimento.getDestino().x) {
      // Movimento Vertical
      incY = (movimento.getDestino().y > movimento.getOrigem().y) ? 1 : -1;
    } else if (movimento.getOrigem().y == movimento.getDestino().y) {
      // Movimento Horizontal
      incX = (movimento.getDestino().x > movimento.getOrigem().x) ? 1 : -1;
    }
    Point posAtual = new Point(movimento.getOrigem().x, movimento.getOrigem().y);
    while (!posAtual.equals(movimento.getDestino())) {
      posAtual = new Point(posAtual.x + incX, posAtual.y + incY);
      if (posAtual.equals(movimento.getDestino())) {
        return (getPeca(posAtual) == null) || (getPeca(posAtual).getCor() != getPeca(movimento.getOrigem()).getCor());
      } else {
        if (getPeca(posAtual) != null) {
          return false;
        }
      }
    }
    return false;
  }

  /**
   * @param movimento
   * @return true se o movimento é possível para uma rainha (não verifica xeque)
   * @see Testado
   */
  private boolean isMovimentoPossivelRainha(Movimento movimento) {
    return isMovimentoPossivelBispo(movimento) || isMovimentoPossivelTorre(movimento);
  }

  /**
   * @param movimento
   * @return true se o movimento é possível para um rei (não verifica xeque)
   * @see Testado
   */
  private boolean isMovimentoPossivelRei(Movimento movimento) {
    Peca pecaOrigem = getPeca(movimento.getOrigem());
    Peca pecaDestino = getPeca(movimento.getDestino());

    if (((Math.abs(movimento.getDestino().x - movimento.getOrigem().x) <= 1)
            && (Math.abs(movimento.getDestino().y - movimento.getOrigem().y) <= 1))
            && ((pecaDestino == null)
            || (pecaDestino.getCor() != pecaOrigem.getCor()))) {

      // Movimento ordinário
      return true;
    }

    /**
     * Roques
     */
    if (pecaOrigem.isMovimentada() || (movimento.getOrigem().y != movimento.getDestino().y)) {
      /**
       * Rei já foi movimentado ou não está movendo na mesma linha, Roque impossível
       */
      return false;
    }

    if (movimento.getDestino().x == 2) {
      // Roque à esquerda

      // Nem o rei e nem a torre podem ter sido movimentados
      Peca torre = getPeca(0, movimento.getOrigem().y);
      if ((torre == null) || torre.isMovimentada() || pecaOrigem.isMovimentada()) {
        return false;
      }

      /**
       * Se a torre não foi movimentada, já está garantido que o rei e a torre já são da mesma cor e que o tipo da peça
       * "torre" é uma torre...
       */
      // Não pode haver peças entre a torre e o rei
      for (int i = 1; i <= 3; i++) {
        if (getPeca(i, movimento.getDestino().y) != null) {
          return false;
        }
      }

      return true;
    }

    if (movimento.getDestino().x == 6) {
      // Roque à direita

      // Nem o rei e nem a torre podem ter sido movimentados
      Peca torre = getPeca(7, movimento.getOrigem().y);
      if ((torre == null) || torre.isMovimentada() || pecaOrigem.isMovimentada()) {
        return false;
      }

      /**
       * Se a torre não foi movimentada, já está garantido que o rei e a torre já são da mesma cor e que o tipo da peça
       * "torre" é uma torre...
       */
      // Não pode haver peças entre a torre e o rei
      for (int i = 5; i <= 6; i++) {
        if (getPeca(i, movimento.getDestino().y) != null) {
          return false;
        }
      }

      return true;
    }

    return false;
  }

  /**
   * @return true caso haja pelo menos um movimento possível para o jogador atual
   */
  private boolean isMovimentoPossivelParaJogador(Peca.COR cor) {
    Movimento movimento;
    Point origem;
    Point destino;
    Peca peca;
    for (int origemX = 0; origemX < 8; origemX++) {
      for (int origemY = 0; origemY < 8; origemY++) {
        origem = new Point(origemX, origemY);
        peca = getPeca(origem);
        if ((peca == null) || (peca.getCor() != cor)) {
          continue;
        }
        for (int destinoX = 0; destinoX < 8; destinoX++) {
          for (int destinoY = 0; destinoY < 8; destinoY++) {
            destino = new Point(destinoX, destinoY);
            if (origem.equals(destino)) {
              continue;
            }
            movimento = new Movimento(origem, destino);
            if (isMovimentoPossivel(movimento)) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }

  /**
   * @param cor
   * @param movimento
   * @return true se após o movimento em questão a cor em questão fica em xeque
   */
  public boolean isXequeAposMovimento(Peca.COR cor, Movimento movimento) {
    // Problema: está sempre retornando false.
    Tabuleiro clone = this.clone();
    clone.doMovimento(movimento, false);

    // Está sempre retornando false aparentemente
    boolean retorno = clone.isXeque(cor);

    return retorno;
  }

  /**
   * @param cor do rei em questão
   * @return Se o rei da cor informada está em xeque
   */
  public boolean isXeque(Peca.COR cor) {
    Point posRei = getPosicaoRei(cor);

    assert (posRei != null) : "Posição inválida do rei " + cor + " (ele existe?) \n\nPosição do Tabuleiro:\n" + this;

    Peca pecaRei = getPeca(posRei);
    Peca pecaAdversaria;

    assert (pecaRei != null) : "Rei " + cor + " inexiste";
    assert (pecaRei.getTipo() == Peca.TIPO.REI) : "Peça obtida não é rei";

    // Verifica se qualquer uma das peças adversárias pode atacá-lo
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if ((posRei.x == i) && (posRei.y == j)) {
          continue;
        }

        pecaAdversaria = getPeca(i, j);
        if (pecaAdversaria == null) {
          continue;
        }

        if (pecaAdversaria.getCor() == cor) {
          continue;
        }

        if (isMovimentoPossivel(new Movimento(new Point(i, j), posRei), false, false)) {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * @param cor
   * @return número de peças de um determinado jogador
   */
  public Map<Peca.TIPO, Integer> getNumeroPecas(Peca.COR cor) {
    EnumMap<Peca.TIPO, Integer> mapa = new EnumMap<>(Peca.TIPO.class);

    Peca peca;
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        peca = getPeca(i, j);
        if ((peca != null) && (peca.getCor() == cor)) {
          mapa.put(peca.getTipo(), mapa.getOrDefault(peca.getTipo(), 0) + 1);
        }
      }
    }

    return mapa;
  }

  /**
   * @return the numeroJogadas
   */
  public int getNumeroJogadas() {
    return numeroJogadas;
  }

  /**
   * Possíveis situações de empate
   */
  public enum SITUACAO_EMPATE {

    Mate_Perpetuo, Rei_vs_Rei, Rei_vs_Rei_Bispo_ou_Cavalo, Rei_Bispo_vs_Rei_Bispo, Maximo_Jogadas
  };

  /**
   * @return SITUACAO_EMPATE caso o jogo está em uma situação de empate, null caso contrário
   */
  public SITUACAO_EMPATE getEmpate() {
    /**
     * Mate perpétuo
     */
    if (!isXeque(turno) && !isMovimentoPossivelParaJogador(turno)) {
      return SITUACAO_EMPATE.Mate_Perpetuo;
    }

    /**
     * Repetição tripla de jogada
     */
    /**
     * Impossibilidade de Mate
     *
     * Rei v.s. Rei; Rei v.s. Rei + Bispo; Rei v.s. Rei + Cavalo; Rei e Bispo v.s. Rei e Bispo da mesma cor de quadrado
     */
    Map<Peca.TIPO, Integer> ctBrancas = getNumeroPecas(Peca.COR.BRANCA);
    Map<Peca.TIPO, Integer> ctPretas = getNumeroPecas(Peca.COR.PRETA);
    int totalBrancas = 0;
    int totalPretas = 0;
    for (final Peca.TIPO peca : ctBrancas.keySet()) {
      totalBrancas += ctBrancas.get(peca);
    }
    for (final Peca.TIPO peca : ctPretas.keySet()) {
      totalPretas += ctPretas.get(peca);
    }

    assert ((ctBrancas.get(Peca.TIPO.REI) == 1) && (ctPretas.get(Peca.TIPO.REI) == 1)) : "Contabilização impossível de peças para validade do jogo";

    if ((totalBrancas == 1) && (totalPretas == 1)) {
      // Rei v.s. Rei
      return SITUACAO_EMPATE.Rei_vs_Rei;
    } else if ((totalBrancas == 1) && (totalPretas == 2) && ((ctPretas.getOrDefault(Peca.TIPO.BISPO, 0) == 1)
            || (ctPretas.getOrDefault(Peca.TIPO.CAVALO, 0) == 1))) {
      // Branca = Rei
      // Preta = Rei + Bispo OU Cavalo
      return SITUACAO_EMPATE.Rei_vs_Rei_Bispo_ou_Cavalo;
    } else if ((totalBrancas == 2) && (totalPretas == 1) && ((ctBrancas.getOrDefault(Peca.TIPO.BISPO, 0) == 1)
            || (ctBrancas.getOrDefault(Peca.TIPO.CAVALO, 0) == 1))) {
      // Branca = Rei + Bispo OU Cavalo
      // Preta = Rei
      return SITUACAO_EMPATE.Rei_vs_Rei_Bispo_ou_Cavalo;
    } else if ((totalBrancas == 2) && (totalPretas == 2) && ((ctBrancas.getOrDefault(Peca.TIPO.BISPO, 0) == 1)
            || (ctPretas.getOrDefault(Peca.TIPO.BISPO, 0) == 1))) {
      // TODO: Rei e Bispo v.s. Rei e Bispo *da mesma cor de quadrado*
      return SITUACAO_EMPATE.Rei_Bispo_vs_Rei_Bispo;
    }

    /**
     * Regra dos 3 movimentos repetidos
     */
    // TODO
    /**
     * Regra dos 50 movimentos (em 50 jogadas o peão de um dos lados deve ser movimentado pelo menos uma vez ou uma
     * captura deve ter sido feita)
     */
    // TODO
    /**
     * Segundo o chess-poster.com o número máximo de jogadas possíveis em um jogo é 5.950. Se atingimos este limite,
     * consideraremos um empate
     */
    if (numeroJogadas >= 5950) {
      return Maximo_Jogadas;
    }
    return null;
  }

  /**
   * @param cor
   * @return posição atual do rei
   */
  private Point getPosicaoRei(Peca.COR cor) {
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if ((tabuleiro[i][j] != null)
                && (tabuleiro[i][j].getTipo() == Peca.TIPO.REI)
                && (tabuleiro[i][j].getCor() == cor)) {
          return new Point(i, j);
        }
      }
    }
    // Rei inexistente
    return null;
  }

  /**
   * Condições para xeque mate
   *
   * - Turno ser da cor atual
   *
   * - Cor atual está em xeque
   *
   * - Para todos os movimentos possíveis, cor atual continua em xeque
   *
   * @param cor
   * @return caso o rei na posRei está em XequeMate
   */
  public boolean isXequeMate(Peca.COR cor) {
    return isXeque(cor) && !isMovimentoPossivelParaJogador(cor);
  }

  /**
   * @param movimento
   * @return se o movimento passado é possível, verificando xeques subsequentes
   */
  public boolean isMovimentoPossivel(Movimento movimento) {
    return isMovimentoPossivel(movimento, true, true);
  }

  /**
   * @param movimento
   * @return se o movimento passado é possível, verificando xeques subsequentes
   */
  public boolean isMovimentoPossivel(Movimento movimento, boolean verificarCor) {
    return isMovimentoPossivel(movimento, true, verificarCor);
  }

  /**
   * @param movimento
   * @param checarXeque verifica xeques subsequentes
   * @return se o movimento passado é possível
   */
  private boolean isMovimentoPossivel(Movimento movimento, boolean checarXeque, boolean verificarCor) {
    Peca peca = getPeca(movimento.getOrigem());

    // Não há peça na origem, movimento impossível
    if (peca == null) {
      return false;
    }

    if (verificarCor) {
      if (peca.getCor() != turno) {
        // Não é o turno da peça em questão
        return false;
      }
    }

    /**
     * A checagem de movimento por tipo de peça é feita antes da checagem de xeque após movimento para evitar
     * processamento desnecessário
     */
    boolean movimentoPossivel;

    switch (peca.getTipo()) {
      case PEAO:
        movimentoPossivel = isMovimentoPossivelPeao(movimento);
        break;

      case CAVALO:
        movimentoPossivel = isMovimentoPossivelCavalo(movimento);
        break;

      case BISPO:
        movimentoPossivel = isMovimentoPossivelBispo(movimento);
        break;

      case TORRE:
        movimentoPossivel = isMovimentoPossivelTorre(movimento);
        break;

      case RAINHA:
        movimentoPossivel = isMovimentoPossivelRainha(movimento);
        break;

      case REI:
        movimentoPossivel = isMovimentoPossivelRei(movimento);
        break;

      default:
        throw new IllegalStateException("Peça de origem desconhecida");
    }

    if (!movimentoPossivel) {
      return false;
    }

    if (checarXeque && isXequeAposMovimento(turno, movimento)) {
      // O rei fica em xeque após o movimento
      return false;
    }

    return true;
  }

  /**
   * @return Iteração com todos os movimentos possíveis
   */
  @Override
  public Iterator<Movimento> iterator() {
    Iterator<Movimento> it = new Iterator<Movimento>() {
      int origemX = 0;
      int origemY = 0;
      int destinoX = 0;
      int destinoY = 0;
      Movimento proximo = null;

      @Override
      public boolean hasNext() {
        Movimento movimento;
        for (; origemX < 8; origemX++) {
          for (; origemY < 8; origemY++) {
            if (getPeca(origemX, origemY) == null) {
              continue;
            }
            for (; destinoX < 8; destinoX++) {
              for (; destinoY < 8; destinoY++) {
                if ((origemX != destinoX) || (origemY != destinoY)) {
                  movimento = new Movimento(new Point(origemX, origemY), new Point(destinoX, destinoY));
                  if (isMovimentoPossivel(movimento)) {
                    proximo = movimento;
                    destinoY++;// Para a próxima iteração
                    return true;
                  }
                }
              }
              destinoY = 0;
            }
            destinoX = 0;
          }
          origemY = 0;
        }
        origemX = 0;
        return false;
      }

      @Override
      public Movimento next() {
        return proximo;
      }
    };

    return it;
  }

  /**
   * TEMPORARIO
   */
  public void setPeca(int x, int y, Peca p) {
    tabuleiro[x][y] = p;
  }

  /**
   * Imprime os movimentos possiveis para uma peça numa dada origem
   *
   * @param x
   * @param y
   * @see Testado
   */
  public void imprimirMovimentosPossiveis(int x, int y) {
    Point origem = new Point(x, y);
    Point destino;
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        destino = new Point(i, j);
        if (!origem.equals(destino)) {
          Movimento m = new Movimento(origem, destino);
          if (isMovimentoPossivel(m)) {
            System.out.println(m);
          }
        }
      }
    }
  }

  /**
   * @return the turno
   */
  public Peca.COR getTurno() {
    return turno;
  }

  /**
   * Alterna o turno corrente
   */
  public void alternarTurno() {
    if (turno == Peca.COR.BRANCA) {
      turno = Peca.COR.PRETA;
      return;
    }
    turno = Peca.COR.BRANCA;
  }
}
