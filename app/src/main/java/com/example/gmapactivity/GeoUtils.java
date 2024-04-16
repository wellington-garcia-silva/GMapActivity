package com.example.gmapactivity;

import android.location.Location;

import java.util.List;

public class GeoUtils {
    public  GeoUtils(){
        
    }

    /**
     * Verifica se uma nova região pode ser inserida na fila, considerando que não deve haver regiões cadastradas
     * dentro de um raio de 30 metros da localização atual.
     *
     * @param currentLocation A localização atual do usuário.
     * @param newRegionLatitude A latitude da nova região a ser inserida.
     * @param newRegionLongitude A longitude da nova região a ser inserida.
     * @param regions Lista de regiões cadastradas.
     * @return true se a nova região pode ser inserida, false caso contrário.
     */
    public static boolean canInsertRegion(Location currentLocation, double newRegionLatitude, double newRegionLongitude, List<Location> regions) {
        float[] results = new float[1];
        for (Location region : regions) {
            Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), newRegionLatitude, newRegionLongitude, results);
            if (results[0] < 30) { // 30 metros
                return false; // Não permite inserir a nova região
            }
        }
        return true; // Permite inserir a nova região
    }
}

