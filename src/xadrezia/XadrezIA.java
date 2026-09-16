package xadrezia;

import xadrezia.MotorIALuiz.MotorIALuiz;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Scanner;

public class XadrezIA {

    public static int brancas = 0;
    public static int pretas = 0;
    public static int empate = 0;

    /**
     * @param t
     * @return se há uma situação de fim de jogo, ou seja, empate ou xeque mate
     */
    public static boolean checarFimDeJogo(Tabuleiro t) {

        if (t.isXequeMate(Peca.COR.BRANCA)) {
            System.out.println("Brancas em xeque mate!");
            pretas++;
            return true;
        }
        if (t.isXequeMate(Peca.COR.PRETA)) {
            System.out.println("Pretas em xeque mate!");
            brancas++;
            return true;
        }
        Tabuleiro.SITUACAO_EMPATE empate = t.getEmpate();
        if (empate != null) {
            System.out.println("Empate: " + empate);
            XadrezIA.empate++;
            return true;
        }
        return false;
    }

    /**
     * Implementação completa do jogo
     *
     * @param args
     */
    public static void main(String[] args) {
        for (int zxcv = 0; zxcv < 1; zxcv++) {
            // Cria um novo tabuleiro
            Tabuleiro tabuleiro = new Tabuleiro();

            // Cria os motores
            MotorIA motorBranca = new MotorIAIdiota(tabuleiro, Peca.COR.BRANCA);
            MotorIA motorPreta = new MotorIALuiz(tabuleiro, Peca.COR.PRETA);

            // Inicia o jogo
            int jogada = 0; // ID da jogada
            Movimento proximaJogada; // Próxima jogada
            tabuleiro.imprimir(); // Imprime posição inicial do tabuleiro
            System.out.println("\n---\n");

            while (!checarFimDeJogo(tabuleiro)) {
                jogada++;
                System.out.println("Jogada ID " + jogada + " (" + tabuleiro.getTurno() + ")\n");

                if (tabuleiro.getTurno() == Peca.COR.BRANCA) {
                    // Jogada das brancas
                    proximaJogada = motorBranca.getProximaJogada();
                } else {
                    // Jogada das pretas
                    proximaJogada = motorPreta.getProximaJogada();
                }

                tabuleiro.doMovimento(proximaJogada);
                tabuleiro.imprimir();
                System.out.println("\n---\n");
            }
        }
        System.out.println("pretas ganharam " + pretas + " vezes \nbrancas ganharam " + brancas + " vezes \ne empataram " + empate + "vezes");
        System.out.println("--- FIM DE JOGO ---");
    }

}
