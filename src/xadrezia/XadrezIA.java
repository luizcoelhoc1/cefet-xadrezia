package xadrezia;

import xadrezia.MotorIALuiz.MotorIALuiz;
import java.awt.Point;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
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
            List<String> sanMoves = new ArrayList<>();
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

                String san = PgnUtil.toSAN(tabuleiro, proximaJogada);
                tabuleiro.doMovimento(proximaJogada);
                tabuleiro.imprimir();
                System.out.println("\n---\n");

                sanMoves.add(san);
            }

            this.gerarPgn(sanMoves);
        }
        System.out.println("pretas ganharam " + pretas + " vezes \nbrancas ganharam " + brancas + " vezes \ne empataram " + empate + "vezes");
        System.out.println("--- FIM DE JOGO ---");
    }

    /**
     * Monta o PGN da partida recém-finalizada e grava em partida.pgn
     *
     * @param sanMoves lances da partida em notação SAN, em ordem
     */
    private static void gerarPgn(List<String> sanMoves) {
        String resultado;
        if (brancas > 0) {
            resultado = "1-0";
        } else if (pretas > 0) {
            resultado = "0-1";
        } else {
            resultado = "1/2-1/2";
        }

        String pgn = PgnUtil.montarPgn(sanMoves, resultado, "MotorIAIdiota", "MotorIALuiz");

        try (PrintWriter pw = new PrintWriter(new FileWriter("partida.pgn"))) {
            pw.print(pgn);
        } catch (IOException e) {
            System.err.println("Falha ao gravar partida.pgn: " + e.getMessage());
        }

        System.out.println(pgn);
    }

}
