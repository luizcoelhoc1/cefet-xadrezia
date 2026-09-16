/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package xadrezia.MotorIALuiz;

import xadrezia.Movimento;

/**
 *
 * @author Luiz
 */
public class PontoMovimento implements Comparable<PontoMovimento>{
    private Movimento movimento;
    private int ponto;

    public PontoMovimento(Movimento movimento) {
        this.movimento = movimento;
        this.ponto = 0;
    }

    public int getPonto() {
        return ponto;
    }

    public void setPonto(int ponto) {
        this.ponto = ponto;
    }

    public Movimento getMovimento() {
        return movimento;
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
    }

    //ordem decrescente
    @Override
    public int compareTo(PontoMovimento t) {
        return t.getPonto() - this.getPonto();
    }
    
}
