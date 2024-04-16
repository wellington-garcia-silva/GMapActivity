package com.example.gmapactivity;

import java.util.Queue;

public class RemoverFila extends Thread {

    private boolean onOff;
    private boolean retorno;
    private MainActivity3 mA;

    public RemoverFila(MainActivity3 mA){
        this.mA=mA;
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean verificaFila(Queue<Regioes> regioes, double latitude, double longitude){//aqui entro com as coordenadas da fila
        for (Regioes reg: regioes) {
            if((reg.getLatitude() == latitude) && (reg.getLongitude() == longitude)){
                return  false;
            }
            ///reg.getLatitude();
        }
        return true;
    }

    /*public Regioes removerRegioesFila(Queue<Regioes> regioes){

        return regioes = remove();
    }*/
}