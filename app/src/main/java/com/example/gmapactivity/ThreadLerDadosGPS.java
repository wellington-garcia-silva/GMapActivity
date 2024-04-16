package com.example.gmapactivity;//Thread para atualizar a região


import android.location.Location;
import android.location.LocationListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;

import java.util.concurrent.Semaphore;


public class ThreadLerDadosGPS extends Thread {


    private GoogleMap mMap;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView latitudeLabel, longitudeLabel;
    private Button showLocationButton;

    private double latitude, longitude;

    private Semaphore semaforo;

    private boolean onOff;


    private MainActivity3 mA;

    private LocationListener location;

    //MapsActivity mA ....... mA.atualizamapa(lat, long) USAR ISSO
    public ThreadLerDadosGPS(MainActivity3 mA){//, LocationListener location, double latitude, double longitude
        this.mA = mA;
        //start();
    }
    @Override
    public void run(){
        while(true){//Assim a thread não fecha
            try{
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public void AtualizaCoordenadas(double latitude, double longitude, Location location){// Aqui atualiza os dados
        mA.setLongitudeLabel(longitude);//mA.getLongetudeLabel() //location.getLongitude()
        mA.setLatitudeLabel(latitude);//mA.getLatitudeLabel() //location.getLatitude()
        mA.setLocalizacao(location);
        mA.setListaLocalizacao(location);
    }
}
