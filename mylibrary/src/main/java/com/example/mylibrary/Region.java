package com.example.mylibrary;

public class Region{
    //@Expose
    protected String name;
    //@Expose
    protected double latitude;
    //@Expose
    protected double longitude;
    //@Expose
    protected int user;
    //@Expose
    protected long timestamp;
    public Region( String name, double latitude, double longitude,int user, long timestamp){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.user = user;
        this.timestamp=timestamp;
    }

    public double distance(){
        return 5;
    }
    public String getName(){
        return name;
    }
    public double getLatitude(){
        return latitude;
    }
    public double getLongitude(){
        return longitude;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public int getUser(){
        return user;
    }

    public String getUniqueField() {
        return name;
    }

    public void setLongitude(double l){
        longitude = l;
    }
    public void setLatitude(double l){
        latitude = l;
    }
    public void setNome(String nome) {
        this.name = name;
    }
    public void setTimestamp(long timestamp){
        this.timestamp=timestamp;
    }
    public void setUser(int user){
        this.user=user;
    }
    public double distance(Region regiao, double lat, double longi){
        double distancia = calcularDistancia(regiao, lat, longi);//regiaoFila
        return distancia;
    }
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
            return false; // Está dentro do raio
        }
        return true; // Não está dentro do raio
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

