package com.example.gmapactivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.LinkedList;
import java.util.Queue;

public class GpsThread extends Thread {
    private Context context;
    private GoogleMap mMap;
    private TextView latitudeLabel, longitudeLabel;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Queue<LatLng> regionQueue = new LinkedList<>();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public GpsThread(Context context, GoogleMap mMap, TextView latitudeLabel, TextView longitudeLabel) {
        this.context = context;
        this.mMap = mMap;
        this.latitudeLabel = latitudeLabel;
        this.longitudeLabel = longitudeLabel;
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    @Override
    public void run() {
        while (true) {

            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
            //if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(location -> {
                            if (location != null) {
                                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                addRegionToQueue(currentLocation);
                            }
                        });
                try {
                    Thread.sleep(5000); // Atualiza a cada 5 segundos
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addRegionToQueue(LatLng region) {
        regionQueue.add(region);
        processQueue();
    }

    private void processQueue() {
        if (!regionQueue.isEmpty()) {
            LatLng region = regionQueue.poll();
            mainHandler.post(() -> {
                mMap.addMarker(new MarkerOptions().position(region).title("Nova Região"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(region, 15));
            });
        }
    }
}

/*
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GpsThread extends Thread {
    private Context context;
    private GoogleMap mMap;
    private TextView latitudeLabel, longitudeLabel;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public GpsThread(Context context, GoogleMap mMap, TextView latitudeLabel, TextView longitudeLabel) {
        this.context = context;
        this.mMap = mMap;
        this.latitudeLabel = latitudeLabel;
        this.longitudeLabel = longitudeLabel;
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    @Override
    public void run() {
        while (true) {
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }else {
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(location -> {
                            if (location != null) {
                                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                new Handler(Looper.getMainLooper()).post(() -> {
                                    mMap.addMarker(new MarkerOptions().position(currentLocation).title("Minha localização atual"));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                                    latitudeLabel.setText("Latitude: " + location.getLatitude());
                                    longitudeLabel.setText("Longitude: " + location.getLongitude());
                                });
                            }
                        });
                try {
                    Thread.sleep(5000); // Atualiza a cada 5 segundos
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}*/

/*

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GpsThread extends Thread {
    private Context context;
    private GoogleMap mMap;
    private TextView latitudeLabel, longitudeLabel;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public GpsThread(Context context, GoogleMap mMap, TextView latitudeLabel, TextView longitudeLabel) {
        this.context = context;
        this.mMap = mMap;
        this.latitudeLabel = latitudeLabel;
        this.longitudeLabel = longitudeLabel;
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    @Override
    public void run() {
        while (true) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            new Handler(Looper.getMainLooper()).post(() -> {
                                mMap.addMarker(new MarkerOptions().position(currentLocation).title("Minha localização atual"));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                                latitudeLabel.setText("Latitude: " + location.getLatitude());
                                longitudeLabel.setText("Longitude: " + location.getLongitude());
                            });
                        }
                    });
            try {
                Thread.sleep(5000); // Atualiza a cada 5 segundos
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}*/
