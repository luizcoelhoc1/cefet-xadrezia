package xadrezia;

import java.util.Random;

/**
 * Modelo absolutamente idiota de motor de IA
 *
 * @author guimax
 */
public class MotorIAIdiota extends MotorIA {

    public MotorIAIdiota(Tabuleiro tabuleiro, Peca.COR cor) {
        super(tabuleiro, cor);
    }

    @Override
    public Movimento getProximaJogada() {
        // Sorteia e realiza uma jogada qualquer
        Random r = new Random();

        int jogadasPossiveis = 0;

        for (final Movimento m : getTabuleiro()) {
            jogadasPossiveis++;
        }

        int jogadaSorteada = r.nextInt(jogadasPossiveis);

        int jogadaAtual = 0;
        for (final Movimento m : getTabuleiro()) {
            if (jogadaAtual == jogadaSorteada) {
                return m;
            }
            jogadaAtual++;
        }
        
        return null;
    }
}
