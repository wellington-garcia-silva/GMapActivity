package com.example.gmapactivity;

import android.widget.Toast;

import com.example.mylibrary.Cryptography;
import com.example.mylibrary.GeoUtilsLib;
import com.example.mylibrary.Region;
import com.example.mylibrary.RestrictedRegion;
import com.example.mylibrary.SubRegion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
//import com.google.gson.Gson;

public class ThreadAdicionaRegioesFila extends Thread{
    private double latitude;//TextView
    private double longitude;//TextView
    private String name;
   private Semaphore semaforo;
   private MainActivity3 mA;
   private int user;
   private long timestamp;
   private Queue<Region> regiao = new LinkedList<>();
   private boolean verificaSubRegionOuRestrictedRegion = false;
   private boolean isSubRegion = true;
   private String tipoRegiao = "SubRegion";
   private Region copiaRegion, mainRegion;
    public ThreadAdicionaRegioesFila(MainActivity3 mA){////Aqui a região vai receber as coordenadas vindas do botão
        //this.latitude = latitude;
        //this.longitude = longetude;
        //this.name = name;
        //this.semaforo = semaforo;
        this.mA=mA;
    }

    @Override
    public void run(){
        while(true) {
            //AdicionaNaFila();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public void AdicionaNaFila(){
        try{
            Semaphore semaforo = new Semaphore(1);
            semaforo.acquire();
            timestamp = System.nanoTime();
            Gson gson = new Gson();

            CalculadoraDistancia calculadora = new CalculadoraDistancia();
            //Regioes r = new Regioes(latitude,longitude,name, 5, timestamp);
            if(mA.getRegiao().isEmpty()){//Se a fila estiver vazia, criar objeto Region
                Region r = new Region("nameless",mA.getLatitude(),mA.getLongitude(), 5, timestamp);//String name, double latitude, double longitude,int user, long timestamp
                //String regionJson = gson.toJson(r);
                //String encryptedRegionJson = Cryptography.encrypt(regionJson);
                ThreadJson t = new ThreadJson();
                t.transformaJson(r);//aqui tenho o obejeto Json

                Cryptography c = new Cryptography();
                mA.setAdicionaNaFila(c.encryptRegion(r));//
                //mA.setAdicionaNaFila(r);
                //Toast.makeText(, "Regiões salvas no BD com sucesso.", Toast.LENGTH_SHORT).show();
            }

            for (Region reg: mA.getRegiao()) {//regiao
                //descriptografar

                if(reg instanceof Region){
                    //boolean dentroDoRaio = calculadora.estaDentroDoRaio(reg, mA.getLatitude(), mA.getLongitude());//regiao, fila´// e coordenadas lat e long
                    Region r = new Region("nameless",mA.getLatitude(),mA.getLongitude(), 5, timestamp);
                    if(r.distance(reg,mA.getLatitude(),mA.getLongitude())>= 30){//dentroDoRaio
                        verificaSubRegionOuRestrictedRegion=false;//Significa que não posso inserir na fila
                    }
                    //else verificaSubRegionOuRestrictedRegion=true;
                    mainRegion=reg;
                }
            }

            if (verificaSubRegionOuRestrictedRegion){
                Region r = new Region("nameless",latitude,longitude, 5, timestamp);//String name, double latitude, double longitude,int user, long timestamp
                //tenho que criptografar
                ThreadJson t = new ThreadJson();
                t.transformaJson(r);//aqui tenho o obejeto Json
                regionToJson(r);
                Cryptography c = new Cryptography();
                mA.setAdicionaNaFila(c.encryptRegion(r));//
                verificaSubRegionOuRestrictedRegion=true;

            }else{
                for (Region reg: mA.getRegiao()) {//regiao //Só Verifico se é o ultimo tipo foi subReg ou Restrict
                    //descriptografar
                    if(reg instanceof SubRegion){//istanceOf(reg).iquals("SubRegion")
                        //tipoRegiao="SubRegion";
                        isSubRegion = true;
                        copiaRegion = reg;
                    }
                    else if (reg instanceof RestrictedRegion){
                        //tipoRegiao="RestrictedRegion";
                        isSubRegion = false;
                        copiaRegion = reg;
                    }
                }
                if(isSubRegion){//O tipo aqui deve ser sub = tipoRegiao=="SubRegion"
                    Region r = new SubRegion("nameless",mA.getLatitude(),mA.getLongitude(), 5, timestamp);//String name, double latitude, double longitude,int user, long timestamp
                    //r.setMainRegion(mainRegion);
                    if(r.distance(copiaRegion, mA.getLatitude(), getLongitude()) >= 5){
                        ThreadJson t = new ThreadJson();
                        t.transformaJson(r);//aqui tenho o obejeto Json
                        Cryptography c = new Cryptography();
                        mA.setAdicionaNaFila(c.encryptRegion(r));//
                        //mA.setAdicionaNaFila(r);
                    }

                    //Region r = new Region(name,latitude,longitude, 5, timestamp);//String name, double latitude, double longitude,int user, long timestamp
                }
                else{//O tipo aqui deve ser Restrict
                    Region r = new RestrictedRegion("nameless",mA.getLatitude(),mA.getLongitude(), 5, timestamp);//String name, double latitude, double longitude,int user, long timestamp
                    if(r.distance(copiaRegion, mA.getLatitude(), getLongitude()) >= 5){
                        ThreadJson t = new ThreadJson();
                        t.transformaJson(r);//aqui tenho o obejeto Json
                        Cryptography c = new Cryptography();
                        mA.setAdicionaNaFila(c.encryptRegion(r));//
                        //mA.setAdicionaNaFila(r);
                    }
                    //Region r = new Region(name,latitude,longitude, 5, timestamp);//String name, double latitude, double longitude,int user, long timestamp
                }
            }
            Thread.sleep(1000);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            semaforo.release();
        }
    }

    public double getLatitude(){
        return latitude;
    }

    public double getLongitude(){
        return longitude;
    }

    public void setLatitude(double l){
         latitude = l;
    }

    public void setLongitude(double l){
        latitude = l;
    }

    public class CalculadoraDistancia {
        // Método para calcular a distância entre dois pontos geográficos (latitude e longitude)
        private double calcularDistancia(Region regiao1, double latitude, double longitude) {
            final int RAIO_TERRA = 6371000; // Raio da Terra em metros

            double lat1 = Math.toRadians(regiao1.getLatitude());
            double lon1 = Math.toRadians(regiao1.getLongitude());
            double lat2 = Math.toRadians(latitude);//regiao2.getLatitude()
            double lon2 = Math.toRadians(longitude);//regiao2.getLongitude()

            double dLat = lat2 - lat1;
            double dLon = lon2 - lon1;

            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(lat1) * Math.cos(lat2) *
                            Math.sin(dLon / 2) * Math.sin(dLon / 2);

            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            return RAIO_TERRA * c;
        }

        // Método para verificar se um objeto de Regiao está dentro de um raio de 30 metros de outra Regiao
        public boolean estaDentroDoRaio(Region regiao, double lat, double longi) {
            //for (Regioes regiaoFila : fila) {
            double distancia = calcularDistancia(regiao, lat, longi);//regiaoFila
            if (distancia <= 30) {
                return true; // Está dentro do raio
            }
            return false; // Não está dentro do raio
            //}

        }
        public boolean estaDentroDoRaioCincoMetros(Region regiao, double lat, double longi) {
            //for (Regioes regiaoFila : fila) {
            double distancia = calcularDistancia(regiao, lat, longi);//regiaoFila
            if (distancia <= 5) {
                return false; // Está dentro do raio
            }
            return true; // Não está dentro do raio
            //}

        }
    }
    public Region Criptografa(Region r){
        return r;
    }
    public Region Descriptografa(Region r){
        return r;
    }
    public static String regionToJson(Region region) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(region);
    }
    public class RegionJsonWrapper {
        private Region region;
        private String json;

        public RegionJsonWrapper(Region region) {
            this.region = region;
            // Supondo que você tenha uma instância de Gson
            Gson gson = new Gson();
            this.json = gson.toJson(region);
        }

        public Region getRegion() {
            return region;
        }

        public String getJson() {
            return json;
        }
    }


}
