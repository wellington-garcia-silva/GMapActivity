package com.example.gmapactivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class MinhaThread extends Thread{
    private GoogleMap mMap;
    private String name;
    private int tempo;
    
    private double longitude;
    private double latitude;



    private ThreadAdicionaRegioesFila regiao;
    //ArrayList<Regioes> minhaListaRedioes = new ArrayList<>();
    
    public MinhaThread(String name,int tempo){
        this.name = name;
        this.tempo = tempo;
        start();//Evita de chamar o start para toda thread criada
    }
    public void run(){

        ////devo ler os dados do Maps



        GoogleMap googleMap = null;
        mMap = googleMap;



        // Aqui você pode fazer qualquer coisa com o objeto mMap, como adicionar marcadores, etc.

        // Obter a posição atual do mapa
        LatLng posicaoAtual = mMap.getCameraPosition().target;

        // Obter a latitude e longitude
        double latitude = posicaoAtual.latitude;
        double longitude = posicaoAtual.longitude;

        // Exemplo de uso das coordenadas obtidas
        // Você pode fazer o que desejar com as coordenadas, como exibir em um TextView ou realizar alguma outra ação.
        System.out.println("Latitude: " + latitude);// Aqui já mostro as coordenadas
        System.out.println("Longitude: " + longitude); // Aqui já mostro as coordenadas
        
        this.latitude = latitude;
        this.longitude= longitude;




    }



    public ThreadAdicionaRegioesFila RetornaRegiao(){ //Quando apertar o botão chama este método
        //Regioes regiao = new Regioes(latitude,longitude, "Primeira Região");

        return regiao;
    }

    public void AdicionaRegiao(){ //Quando apertar o botão chama este método
        //Regioes regiao = new Regioes(latitude,longitude, "Primeira Região");

        //minhaListaRedioes.add(regiao);
    }

    public void MostrarCoordenadas(){
        System.out.println("Latitude: " + latitude);// Aqui já mostro as coordenadas no label
        System.out.println("Longitude: " + longitude); // Aqui já mostro as coordenadas no label
    }
}
