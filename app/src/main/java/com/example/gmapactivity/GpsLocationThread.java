/*package com.example.gmapactivity;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;

public class GpsLocationThread implements Runnable {
    private Context context;
    private LocationManager locationManager;
    private LocationListener locationListener;

    public GpsLocationThread(Context context) {
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Atualize a latitude e longitude aqui
                // Por exemplo, você pode chamar métodos para atualizar UI ou armazenar os dados
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onLocationChanged(@NonNull Location location) {

            }

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };
    }

    @Override
    public void run() {
        try {
            // Verifique se o GPS está habilitado
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGPSEnabled) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
            } else {
                // Se o GPS não estiver habilitado, tente usar o provedor de rede
                boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}*/

