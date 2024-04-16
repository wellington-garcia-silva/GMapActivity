package com.example.gmapactivity;

import android.widget.TextView;

public class Regioes {
    private double latitude;

    private double longitude;

    private String nome;
    private int user;
    private long timestamp;

    public Regioes(double latitude, double longitude, String nome, int user, long timestamp){
        this.nome = nome;
        this.latitude = latitude;
        this.longitude = longitude;
        //this.timestamp = System.nanoTime();
        this.user = user;
        this.timestamp=timestamp;
    }

    public String getUniqueField() {
        return nome;
    }
    public void setLongitude(double l){
        longitude = l;
    }
    public void setLatitude(double l){
        latitude = l;
    }
    public double getLongitude(){
        return longitude;
    }
    public double getLatitude(){
        return latitude;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setTimestamp(long timestamp){
        this.timestamp=timestamp;
    }
    public void setUser(int user){
        this.user=user;
    }
    public int getUser(){
        return user;
    }

    public long getTimestamp() {
        return timestamp;
    }
    public String getName(){
        return nome;
    }
}
