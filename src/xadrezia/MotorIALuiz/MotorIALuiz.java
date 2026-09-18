package xadrezia.MotorIALuiz;

import java.util.ArrayList;
import java.util.List;
import xadrezia.MotorIA;
import xadrezia.Movimento;
import xadrezia.Peca;
import xadrezia.Tabuleiro;

/**
 * Motor que aprofunda a busca enquanto houver tempo disponível.
 *
 * <p>Ao contrário da implementação anterior, a profundidade não é limitada a
 * três lances. A busca é feita em iterações completas de profundidade crescente
 * e somente o resultado da última iteração terminada é usado.</p>
 */
public class MotorIALuiz extends MotorIA {

    /** Tempo usado pelo construtor legado. */
    public static final long TEMPO_PADRAO_MILLIS = 10_000L;
    private static final int VALOR_MATE = 1_000_000;

    private final long tempoPorJogadaNanos;
    private long limiteBuscaNanos;

    public MotorIALuiz(Tabuleiro tabuleiro, Peca.COR cor) {
        this(tabuleiro, cor, TEMPO_PADRAO_MILLIS);
    }

    /**
     * @param tempoPorJogadaMillis orçamento máximo, em milissegundos, para uma jogada
     */
    public MotorIALuiz(Tabuleiro tabuleiro, Peca.COR cor, long tempoPorJogadaMillis) {
        super(tabuleiro, cor);
        if (tempoPorJogadaMillis <= 0) {
            throw new IllegalArgumentException("O tempo por jogada deve ser maior que zero.");
        }
        this.tempoPorJogadaNanos = tempoPorJogadaMillis * 1_000_000L;
    }

    @Override
    public Movimento getProximaJogada() {
        List<Movimento> movimentos = movimentosPossiveis(getTabuleiro());
        if (movimentos.isEmpty()) {
            return null;
        }

        Movimento melhorMovimento = movimentos.get(0);
        limiteBuscaNanos = System.nanoTime() + tempoPorJogadaNanos;

        // Cada iteração é independente: uma expirada nunca substitui a melhor
        // jogada encontrada pela última profundidade inteiramente analisada.
        for (int profundidade = 1; ; profundidade++) {
            try {
                Movimento candidato = melhorMovimentoNaProfundidade(movimentos, profundidade);
                melhorMovimento = candidato;
            } catch (TempoEsgotadoException ex) {
                break;
            }
        }
        return melhorMovimento;
    }

    private Movimento melhorMovimentoNaProfundidade(List<Movimento> movimentos, int profundidade) {
        Movimento melhorMovimento = null;
        int melhorValor = Integer.MIN_VALUE;
        int alpha = Integer.MIN_VALUE + 1;
        int beta = Integer.MAX_VALUE;

        for (Movimento movimento : movimentos) {
            verificarTempo();
            Tabuleiro proximo = getTabuleiro().clone();
            proximo.doMovimento(movimento);
            int valor = minimax(proximo, profundidade - 1, alpha, beta);
            if (melhorMovimento == null || valor > melhorValor) {
                melhorValor = valor;
                melhorMovimento = movimento;
            }
            alpha = Math.max(alpha, melhorValor);
        }
        return melhorMovimento;
    }

    private int minimax(Tabuleiro tabuleiro, int profundidade, int alpha, int beta) {
        verificarTempo();
        if (profundidade == 0) {
            if (tabuleiro.isXequeMate(tabuleiro.getTurno())) {
                return tabuleiro.getTurno() == getCor() ? -VALOR_MATE : VALOR_MATE;
            }
            return avaliar(tabuleiro);
        }

        List<Movimento> movimentos = movimentosPossiveis(tabuleiro);
        if (movimentos.isEmpty()) {
            if (tabuleiro.isXequeMate(tabuleiro.getTurno())) {
                return tabuleiro.getTurno() == getCor()
                        ? -VALOR_MATE - profundidade : VALOR_MATE + profundidade;
            }
            return 0;
        }

        boolean maximizando = tabuleiro.getTurno() == getCor();
        int melhorValor = maximizando ? Integer.MIN_VALUE + 1 : Integer.MAX_VALUE;
        for (Movimento movimento : movimentos) {
            Tabuleiro proximo = tabuleiro.clone();
            proximo.doMovimento(movimento);
            int valor = minimax(proximo, profundidade - 1, alpha, beta);
            if (maximizando) {
                melhorValor = Math.max(melhorValor, valor);
                alpha = Math.max(alpha, melhorValor);
            } else {
                melhorValor = Math.min(melhorValor, valor);
                beta = Math.min(beta, melhorValor);
            }
            if (beta <= alpha) {
                break;
            }
        }
        return melhorValor;
    }

    private List<Movimento> movimentosPossiveis(Tabuleiro tabuleiro) {
        List<Movimento> movimentos = new ArrayList<>();
        for (Movimento movimento : tabuleiro) {
            movimentos.add(movimento);
        }
        return movimentos;
    }

    private int avaliar(Tabuleiro tabuleiro) {
        int valor = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Peca peca = tabuleiro.getPeca(x, y);
                if (peca != null) {
                    valor += peca.getCor() == getCor() ? peca.getValor() : -peca.getValor();
                }
            }
        }
        return valor;
    }

    private void verificarTempo() {
        if (System.nanoTime() >= limiteBuscaNanos) {
            throw new TempoEsgotadoException();
        }
    }

    private static final class TempoEsgotadoException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }
}
