package com.example.gmapactivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

public class GPSReader implements Runnable, LocationListener {
    private Context context;
    private Handler handler;
    private TextView latitudeLabel, longitudeLabel;

    public GPSReader(Context context, Handler handler, TextView latitudeLabel, TextView longitudeLabel) {
        this.context = context;
        this.handler = handler;
        this.latitudeLabel = latitudeLabel;
        this.longitudeLabel = longitudeLabel;
    }

    @Override
    public void run() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        final double latitude = location.getLatitude();
        final double longitude = location.getLongitude();

        handler.post(new Runnable() {
            @Override
            public void run() {
                latitudeLabel.setText("Latitude: " + latitude);
                longitudeLabel.setText("Longitude: " + longitude);
            }
        });
    }

    // Implemente os outros métodos do LocationListener conforme necessário
}

