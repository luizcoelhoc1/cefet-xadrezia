/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xadrezia.MotorIALuiz;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import xadrezia.MotorIA;
import xadrezia.Movimento;
import xadrezia.Peca;
import static xadrezia.Peca.COR.*;
import static xadrezia.Peca.TIPO.*;
import xadrezia.Tabuleiro;

/**
 *
 * @author Luiz
 */
public class MotorIALuiz extends MotorIA {

    private boolean mateDoLouco;

    public MotorIALuiz(Tabuleiro tabuleiro, Peca.COR cor) {
        super(tabuleiro, cor);
        mateDoLouco = true;
    }

    @Override
    public Movimento getProximaJogada() {
        Long l = System.currentTimeMillis();
        Movimento m;
        if (getCor() == PRETA) {
            m = movPreta();
        } else {
            m = movBranca();
        }
        System.err.println((System.currentTimeMillis() - l) + "ms");
        return m;
    }

    private Movimento movBranca() {

        ArrayList<RunnableCalcJogadaBranca> alRunnable = new ArrayList<>();
        for (final Movimento m : getTabuleiro()) {
            alRunnable.add(new RunnableCalcJogadaBranca(m, getTabuleiro()));
        }
        ArrayList<Thread> alThread = new ArrayList<>();
        int i = 0;
        for (final RunnableCalcJogadaBranca x : alRunnable) {
            alThread.add(new Thread(x));
            alThread.get(i).start();
            i++;
        }
        Collections.sort(alRunnable);
        for (final Thread t : alThread) {
            try {
                t.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(MotorIALuiz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Collections.sort(alRunnable);


        return alRunnable.get(0).getMovimento();
    }

    private Movimento movPreta() {

        if (mateDoLouco) {
            Movimento m = mateDoLouco();
            if (m == null) {
                mateDoLouco = false;
            } else {
                return m;
            }
        }

        ArrayList<RunnableCalcJogadaPreta> alRunnable = new ArrayList<>();
        for (final Movimento m : getTabuleiro()) {
            if (getTabuleiro().getPeca(m.getOrigem()).getCor() == PRETA) {
                alRunnable.add(new RunnableCalcJogadaPreta(m, getTabuleiro()));
            }
        }
        ArrayList<Thread> alThread = new ArrayList<>();
        int i = 0;
        for (final RunnableCalcJogadaPreta x : alRunnable) {
            alThread.add(new Thread(x));
            alThread.get(i).start();
            i++;
        }
        /*try {
         Thread.sleep(9500);
         } catch (InterruptedException ex) {
         Logger.getLogger(MotorIALuiz.class.getName()).log(Level.SEVERE, null, ex);
         }*/
        for (final Thread t : alThread) {
            try {
                t.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(MotorIALuiz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Collections.sort(alRunnable);

        return alRunnable.get(0).getMovimento();
    }

    private ArrayList<Movimento> movimentosPossiveis(Tabuleiro tabuleiro, Peca.COR cor) {
        ArrayList<Movimento> movimentosPossiveis = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (tabuleiro.getPeca(i, j) == null) {
                    continue;
                }
                if ((cor == PRETA) && (tabuleiro.getPeca(i, j).getCor() != PRETA)) {
                    continue;
                }
                if ((cor == BRANCA) && (tabuleiro.getPeca(i, j).getCor() != BRANCA)) {
                    continue;
                }

                for (int k = 0; k < 7; k++) {
                    for (int l = 0; l < 7; l++) {
                        if (k == i & l == j) {
                            continue;
                        }
                        Movimento m = new Movimento(new Point(i, j), new Point(k, l));
                        if (tabuleiro.isMovimentoPossivel(m)) {
                            movimentosPossiveis.add(m);
                        }
                    }
                }
            }
        }
        return movimentosPossiveis;
    }

    private Movimento mateDoLouco() {
        int primeiraJogada = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 2; j++) {
                if (getTabuleiro().getPeca(i, j) != null) {
                    primeiraJogada += getTabuleiro().getPeca(i, j).getValor();
                }
            }
        }
        if ((primeiraJogada == 79)
                && ((getTabuleiro().getPeca(6, 6) == null) || (getTabuleiro().getPeca(6, 5) == null))) {
            return new Movimento("e7", "e6");
        }

        boolean condicao0 = false;
        if (getTabuleiro().getPeca(4, 6) != null) {
            if (getTabuleiro().getPeca(4, 6).getTipo() == PEAO) {
                condicao0 = true;
            }
        }

        boolean condicao1 = false;
        if (getTabuleiro().getPeca(5, 5) != null) {
            if (getTabuleiro().getPeca(5, 5).getTipo() == PEAO) {
                condicao1 = true;
            }
        }

        boolean condicao2 = false;
        if (getTabuleiro().getPeca(4, 5) != null) {
            if (getTabuleiro().getPeca(4, 5).getTipo() == PEAO) {
                condicao2 = true;
            }
        }

        if ((condicao0) && (condicao1 || condicao2)) {
            return new Movimento("d8", "h4");
        }
        return null;
    }
}
