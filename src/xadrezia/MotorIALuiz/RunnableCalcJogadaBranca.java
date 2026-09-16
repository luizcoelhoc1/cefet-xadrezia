/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xadrezia.MotorIALuiz;

import xadrezia.Movimento;
import static xadrezia.Peca.COR.*;
import xadrezia.Tabuleiro;

/**
 *
 * @author Luiz
 */
public class RunnableCalcJogadaBranca implements Runnable, Comparable<RunnableCalcJogadaBranca> {

    private Movimento movimento;
    private int pontos;
    private Tabuleiro tabuleiroAtual;

    public RunnableCalcJogadaBranca(Movimento movimento, Tabuleiro tabuleiroAtual) {
        this.movimento = movimento;
        this.pontos = 0;
        this.tabuleiroAtual = tabuleiroAtual;
    }

    @Override
    public void run() {
        try {
            Tabuleiro t0 = tabuleiroAtual.clone();
            t0.doMovimento(movimento);
            int v0 = getValor(t0);
            pontos += v0 - getValor(tabuleiroAtual);

            for (final Movimento m0 : t0) {

                Tabuleiro t1 = t0.clone();

                t1.doMovimento(m0);
                int v1 = getValor(t1);
                pontos += v1 - v0;

                for (final Movimento m1 : t1) {
                    Tabuleiro t2 = t1.clone();
                    t2.doMovimento(m1);
                    int v2 = getValor(t2);
                    pontos += v2 - v1;
                    for (final Movimento m2 : t2) {
                        Tabuleiro t3 = t2.clone();
                        t3.doMovimento(m2);
                        int v3 = getValor(t3);
                        pontos += v3 - v2;
                        /*
                         for (final Movimento m3 : t3) {
                         Tabuleiro t4 = t3.clone();
                         t4.doMovimento(m3);
                         int v4 = getValor(t4);
                         pontos += v4 - v3;

                         for (final Movimento m4 : t4) {
                         Tabuleiro t5 = t4.clone();
                         t5.doMovimento(m4);
                         int v5 = getValor(t5);
                         pontos += v5 - v4;

                         }
                         }*/

                    }
                }

            }
        } catch (Exception e) {
        }
    }

    /**
     * @param tabuleiro que será verificado a quantidade de pontos
     * @return um vetor com a primeira posição(0) o valor de pontos das peças
     * brancas e a segunda posição(1) com o valor das peças negras
     */
    private int getValor(Tabuleiro tabuleiro) {
        int valorBranca = 0;
        int valorPreta = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tabuleiro.getPeca(i, j) == null) {
                    continue;
                }
                if (tabuleiro.getPeca(i, j).getCor() == BRANCA) {
                    valorBranca += tabuleiro.getPeca(i, j).getValor();
                } else { //se for preta
                    valorPreta += tabuleiro.getPeca(i, j).getValor();
                }
            }
        }
        return valorBranca - valorPreta;
    }

    @Override
    public int compareTo(RunnableCalcJogadaBranca t) {
        return t.getPontos() - this.getPontos();
    }

    public Movimento getMovimento() {
        return movimento;
    }

    public void setMovimento(Movimento m) {
        this.movimento = m;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public Tabuleiro getTabuleiroAtual() {
        return tabuleiroAtual;
    }

    public void setTabuleiroAtual(Tabuleiro tabuleiroAtual) {
        this.tabuleiroAtual = tabuleiroAtual;
    }

}
