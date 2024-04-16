package com.example.gmapactivity;

//import static android.location.LocationRequest.*;

//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationRequest;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mylibrary.GeoUtilsLib;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {//implements OnMapReadyCallback implements LocationListener

    private final Context mContext = this;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    Location location;
    double latitude;
    double longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
    private static final long MIN_TIME_BW_UPDATES = 0;

    private GeoUtilsLib geoUtils;

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView latitudeLabel, longitudeLabel;

    private double longetude;//latitude
    private double longetudeNoBanco, latitudeNoBanco;
    private double longetudeNaFila, latitudeNaFila;
    private Button showLocationButton, db_gravarDadosDB;
    private Semaphore semaforo;

    private int numPermissoes;

    private boolean onOff = true;
    private int user;
    private long timestamp;
    private Queue<Regioes> regiao = new LinkedList<Regioes>();

    //private Location location;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    protected LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //FirebaseApp.initializeApp(this);

        getLocation();


        setContentView(R.layout.activity_main);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        latitudeLabel = findViewById(R.id.latitudeLabel);
        longitudeLabel = findViewById(R.id.longitudeLabel);
        showLocationButton = findViewById(R.id.showLocationButton);//Cria botão inserir na fila
        db_gravarDadosDB = findViewById(R.id.db_gravarDadosDB);//Cria botão inserir no band=co de dados


        //setLatitudeLabel(-21.2292888);
        //setLongetudeLabel(-44.9907155);

        //setLatitudeNaFila(-21.2292888);
        //setLongetudeNaFila(-44.9907155);

        numPermissoes = 1;
        semaforo = new Semaphore(numPermissoes);
        //AdicionaRegioes r = new AdicionaRegioes(location.getLatitude(),location.getLongitude(),"nameless",semaforo);
        //ThreadAdicionaRegioesFila r = new ThreadAdicionaRegioesFila(this, semaforo);// Não posso fazer assim
        //r.start();
        //RemoverFila rem = new RemoverFila();
        //rem.start();

        showLocationButton.setOnClickListener(new View.OnClickListener() {//botão para adicionar na fila
            @Override
            public void onClick(View v) {
                //com.example.mylibrary.GeoUtils g = new com.example.mylibrary.GeoUtils();//Para verificar se a região está a 30m
                //g.canInsertRegion(location,getLatitudeLabel(),getLongetudeLabel(),regiao);

                //LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                //LatLng currentLocation = new LatLng(-21.2435161,-44.9989994);
                //LatLng currentLocation = new LatLng(-getLatitudeLabel(), -getLongetudeLabel());
                /*LatLng currentLocation = new LatLng(-getLatitudeNaFila(), -getLongetudeNaFila());
                mMap.addMarker(new MarkerOptions().position(currentLocation).title("Minha Marcação"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                Toast.makeText(MapsActivity.this, "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude(), Toast.LENGTH_LONG).show();
                //Toast.makeText(MapsActivity.this, "Latitude: " + currentLocation.latitude+ ", Longitude: " + currentLocation.longitude, Toast.LENGTH_LONG).show();
                r.AdicionaNaFila(getLatitudeLabel(),getLongetudeLabel(), "name");*/


                //fusedLocationProviderClient.getLastLocation();


                if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //fusedLocationProviderClient.requestLocationUpdates().
                    fusedLocationProviderClient.getLastLocation()
                            .addOnSuccessListener(MapsActivity.this, location -> {
                                GeoUtilsLib g = new GeoUtilsLib();
                                /*if (location != null) {
                                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                    mMap.addMarker(new MarkerOptions().position(currentLocation).title("Minha Marcação 1"));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));
                                    Toast.makeText(MapsActivity.this, "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude(), Toast.LENGTH_LONG).show();
                                    //boolean b = g.canInsertRegion(location,getLatitudeNaFila(),getLongetudeLabel());
                                    if (existeRegionProxima(getLatitudeNaFila(), getLongetudeLabel())) {//Verificar pela biblioteca criada

                                        if (rem.verificaFila(regiao, getLatitudeLabel(), getLongetudeLabel())) {

                                        }
                                        r.AdicionaNaFila(getLatitudeLabel(), getLongetudeLabel(), "name");
                                    } else {
                                        Toast.makeText(MapsActivity.this, "Esta região está a menos de 30 metros, não pode ser inserida!", Toast.LENGTH_SHORT).show();
                                    }

                                    fusedLocationProviderClient.getLastLocation();

                                } else {
                                    Toast.makeText(MapsActivity.this, "Não foi possível obter a localização Ratual", Toast.LENGTH_SHORT).show();
                                }*/
                            });
                } else {
                    ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }

            }
        });


        //Adiciona marcação no mapa
        Button btnAddMarker = findViewById(R.id.btnAddMarker);
        btnAddMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setTitle("Adicionar Marcação");

                // Layout do AlertDialog
                LinearLayout layout = new LinearLayout(MapsActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(10, 10, 10, 10);

                // Campos de entrada para latitude e longitude
                final EditText inputLatitude = new EditText(MapsActivity.this);
                inputLatitude.setHint("Latitude");
                layout.addView(inputLatitude);

                final EditText inputLongitude = new EditText(MapsActivity.this);
                inputLongitude.setHint("Longitude");
                layout.addView(inputLongitude);

                builder.setView(layout);

                // Botão de confirmação
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String latitudeStr = inputLatitude.getText().toString();
                        String longitudeStr = inputLongitude.getText().toString();

                        if (!latitudeStr.isEmpty() && !longitudeStr.isEmpty()) {
                            double latitude = Double.parseDouble(latitudeStr);
                            double longitude = Double.parseDouble(longitudeStr);

                            //setLatitudeLabel(latitude); //atualiza a latitude
                            //setLongetudeLabel(longitude);

                            setLatitudeNaFila(latitude);
                            setLongetudeNaFila(longitude);

                            // Adicionar marcação no mapa
                            MarkerOptions marker = new MarkerOptions().position(new LatLng(-latitude, -longitude)).title("New Marcação");//Aqui

                            mMap.addMarker(marker);
                        }
                    }
                });

                builder.setNegativeButton("Cancelar", null);
                builder.show();
            }
        });////fim do botão adiciona região mapa

        RemoverRegiaoBancoDados removerDB = new RemoverRegiaoBancoDados(db, getRegiao());
        removerDB.start();



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng localizacao = new LatLng(-21.2292888, -44.9907155); // Coordenadas da marcação
        //LatLng localizacao = new LatLng(getLatitudeLabel(), getLongetudeLabel());
        mMap.addMarker(new MarkerOptions().position(localizacao).title("Minha Marcação")); // Adiciona a marcação no mapa com um título
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(localizacao));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localizacao, 15));
        //latitudeLabel.setText("Latitude: " + -23.55052);//  getLatitudeLabel() localizacao.latitude
        //longitudeLabel.setText("Longitude: " + -46.633308);// getLongetudeLabel() localizacao.longitude


        //LatLng currentLocation = new LatLng(localizacao.getLatitude(), location.getLongitude());
        //mMap.addMarker(new MarkerOptions().position(currentLocation).title("Minha localização Atual"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        //latitudeLabel.setText("Latitude: " + getLatitudeLabel());
        //longitudeLabel.setText("Longitude: " + location.getLongitude());

        //setLatitudeLabel(location.getLatitude());
        //setLongetudeLabel(location.getLongitude());

        //ThreadAdicionaRegiao t = new ThreadAdicionaRegiao(this, location);//Quando o mapa é iniciado começa a ler os dados do mapa
        //t.start();//


        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        //location.getLa
                        if (location != null) {//Entrou!
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(currentLocation).title("Minha localização WRAtual"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                            //latitudeLabel.setText("Latitude: " +  location.getLatitude());//currentLocation.latitude
                            //longitudeLabel.setText("Longitude: " + location.getLongitude());

                            //ThreadAdicionaRegiao t = new ThreadAdicionaRegiao(this, location,getLatitudeLabel(),getLongetudeLabel());//Quando o mapa é iniciado começa a ler os dados do mapa
                            //ThreadAdicionaRegiao t = new ThreadAdicionaRegiao(this, location,getLatitudeLabel(),getLongetudeLabel());//Quando o mapa é iniciado começa a ler os dados do mapa
                            //t.start();//
                        } else {
                            Toast.makeText(this, "Não foi possível obter a localização Ratual", Toast.LENGTH_SHORT).show();
                            //fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }*/


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onMapReady(mMap);
            } else {
                Toast.makeText(this, "Permissão de localização negada", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setLatitudeLabel(double l) {
        latitude = l;
    }

    public void setLatitudeNaFila(double l) {
        latitudeNaFila = l;
    }

    public void setLongetudeLabel(double l) {
        longetude = l;
    }

    public void setLongetudeNaFila(double l) {
        longetudeNaFila = l;
    }

    public double getLatitudeLabel() {
        return latitude;
    }

    public double getLatitudeNaFila() {
        return latitudeNaFila;
    }

    public double getLongetudeLabel() {
        return longetude;
    }

    public double getLongetudeNaFila() {
        return longetudeNaFila;
    }

    public boolean getOnOff() {
        return onOff;
    }

    public void setAdicionaNaFila(Regioes r) {
        regiao.add(r);
    }

    public Queue<Regioes> getRegiao() {//Retorna a Fila
        return regiao;
    }//aqui veirfico a fila

    private boolean existeRegionProxima(double novaLatitude, double novaLongitude) {
        double raio = 30; // Raio de 30 metros
        double raioTerra = 6371000; // Raio da Terra em metros
        double dLat = Math.toRadians(novaLatitude - latitude);
        double dLng = Math.toRadians(novaLongitude - longetude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(novaLatitude)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distancia = raioTerra * c; // Distância em metros

        // Verificar se a distância é menor ou igual ao raio de 30 metros
        return distancia <= raio;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        if (location != null) {

            getLocation();
        }

    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // Nenhum provedor de rede está habilitado
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return null;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            //textView.setText("Latitude: " + latitude + ", Longitude: " + longetude);//lat lng
                            latitudeLabel.setText("Latitude: " + latitude);//location.getLatitude()
                            longitudeLabel.setText("Longitude: " + longetude);//location.getLongitude()
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(currentLocation).title("Minha localização WRAtual"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));


                        }
                    }
                }
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                latitudeLabel.setText("Latitude: " + latitude);//location.getLatitude()
                                longitudeLabel.setText("Longitude: " + longetude);//location.getLongitude()
                                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                mMap.addMarker(new MarkerOptions().position(currentLocation).title("Minha localização WRAtual"));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));

                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }


    public void buscarInformacoesGPS(View v) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)   != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MapsActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(MapsActivity.this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            ActivityCompat.requestPermissions(MapsActivity.this, new String[] {Manifest.permission.ACCESS_NETWORK_STATE}, 1);
            return;
        }

        LocationManager  mLocManager  = (LocationManager) getSystemService(MapsActivity.this.LOCATION_SERVICE);
        LocationListener mLocListener = new MinhaLocalizacaoListener();

        mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocListener);


        if (mLocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            String texto = "Latitude.: " + MinhaLocalizacaoListener.latitude + "\n" +
                    "Longitude: " + MinhaLocalizacaoListener.longitude + "\n";
            Toast.makeText(MapsActivity.this, texto, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MapsActivity.this, "GPS DESABILITADO.", Toast.LENGTH_LONG).show();
        }


        //this.mostrarGoogleMaps(MinhaLocalizacaoListener.latitude, MinhaLocalizacaoListener.longitude);
        //ThreadAdicionaRegiao t = new ThreadAdicionaRegiao(this, mLocListener,MinhaLocalizacaoListener.latitude,MinhaLocalizacaoListener.longitude);//Quando o mapa é iniciado começa a ler os dados do mapa
        //t.start();

    }

    /*public void mostrarGoogleMaps(double latitude, double longitude) {
        WebView wv = findViewById(R.id.webv);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl("https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude);
    }*/

}
