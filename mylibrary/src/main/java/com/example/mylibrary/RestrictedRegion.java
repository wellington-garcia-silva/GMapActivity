package com.example.mylibrary;



public class RestrictedRegion extends Region{
    private Region mainRegion;
    private boolean restricted;

    public RestrictedRegion(String _name, double latitude, double longitude,int user, long timestamp){
        super(_name, latitude, longitude, user, timestamp);//(double latitude, double longitude, String nome, int user, long timestamp){
    }

    //@Override
    public double distance(Region regiao, double lat, double longi){
        double distancia = calcularDistancia(regiao, lat, longi);//regiaoFila
        return distancia;
    }
    public void setMainRegion(Region r){
        mainRegion=r;
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
