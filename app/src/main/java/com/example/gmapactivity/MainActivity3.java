package com.example.gmapactivity;

import static android.content.ContentValues.TAG;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.mylibrary.Cryptography;
import com.example.mylibrary.GeoUtilsLib;
//import com.example.mylibrary.VerificadorDistancia;
import com.example.mylibrary.Region;
import com.example.mylibrary.SubRegion;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.database.DatabaseReference;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.UserData;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity3 extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private GoogleMap mMap;
    private LocationManager locationManager;
    private TextView latitudeLabel, longitudeLabel;
    private double latitude;
    private double longitude;
    private int user;
    private long timestamp;
    private Queue<Region> regiao = new LinkedList<Region>();//Fila de Regioes
    private Button showLocationButton, db_gravarDadosDB;
    //private int numPermissoes = 1;
    //private Semaphore semaforo;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference sintoma;
    private Location localizacao;
    private List<Location> listaLocalizacao = new LinkedList<Location>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitudeLabel = findViewById(R.id.latitudeLabel);
        longitudeLabel = findViewById(R.id.longitudeLabel);
        showLocationButton = findViewById(R.id.showLocationButton);//Cria botão inserir na fila
        db_gravarDadosDB = findViewById(R.id.db_gravarDadosDB);//Cria botão inserir no band=co de dados

        // Inicialize o Firebase
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Solicite as permissões necessárias
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


        //semaforo = new Semaphore(1);
        //AdicionaRegioes r = new AdicionaRegioes(location.getLatitude(),location.getLongitude(),"nameless",semaforo);
        //AdicionaRegioes adicionaFila = new AdicionaRegioes(this, semaforo);// Não posso fazer assim
        //adicionaFila.start();
        //RemoverFila remover = new RemoverFila(this);
        //remover.start();
        Semaphore semaforo = new Semaphore(1);
        ThreadAdicionaRegioesFila adicionarRegiaoFila = new ThreadAdicionaRegioesFila(this);//,semaforo
        adicionarRegiaoFila.start();

        showLocationButton.setOnClickListener(new View.OnClickListener() {//botão para adicionar na fila
            @Override
            public void onClick(View v) {
                //adicionarRegiaoFila.AdicionaNaFila(); // aqui eu adiciono a região na Fila
                adicionarRegiaoFila.AdicionaNaFila();
                Toast.makeText(MainActivity3.this, "Região adicionada!", Toast.LENGTH_SHORT).show();
            }
        });

        //RemoverRegiaoBancoDados removerDB = new RemoverRegiaoBancoDados(db, getRegiao());
        //removerDB.start();
        /*Map<String, Object> user = new HashMap<>();//Regioes, Object
        db_gravarDadosDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Regioes reg: regiao) {
                   reg = regiao.remove();
                   verificaDocumentosUsuarios(reg);
                   if(verificaDocumentosUsuarios(reg)){
                       user.put("latitude", reg.getLatitude());//latitude, getLatitude()
                       user.put("longitude", reg.getLongitude());//longitude,getLongitude()
                       user.put("nome", reg.getNome());//timestamp, timestamp
                       user.put("timestamp", reg.getTimestamp());
                       user.put("user",reg.getUser());

                       // Add a new document with a generated ID
                       db.collection("Regioes")
                               .add(user)
                               .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                   @Override
                                   public void onSuccess(DocumentReference documentReference) {
                                       Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                   }
                               })
                               .addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       Log.w(TAG, "Error adding document", e);
                                   }
                               });
                   }else{
                       Toast.makeText(MainActivity3.this, "documento já cadastrado", Toast.LENGTH_SHORT).show();
                   }

                }
                //verificaDocumentosUsuarios verificaDocumento = new verificaDocumentosUsuarios();

                // Create a new user with a first and last name


            }
        });*/
        db_gravarDadosDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Semaphore semaphoro = new Semaphore(1);
                        try {
                            semaphoro.acquire();

                            while(!regiao.isEmpty()){
                                Region reg = regiao.poll();//reg -> objeto de região
                                VasculhaDB v = new VasculhaDB();
                                if(reg instanceof Region){
                                    Cryptography c = new Cryptography();//descriptografar
                                    c.join();
                                    try {
                                        v.vasculhaBDRegion(c.decryptRegion(reg));//aqui retorna o objeto que está no banco de dados
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }

                                }else if(reg instanceof SubRegion){//aqui retorna o objeto que está no banco de dados
                                    //descriptografar
                                    Cryptography c = new Cryptography();//descriptografar
                                    try {
                                        //v.vasculhaBDSubRegion(c.decryptRegion(reg));//aqui retorna o objeto que está no banco de dados
                                        c.join();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                    //v.vasculhaBDSubRegion(reg);

                                }else{
                                    Cryptography c = new Cryptography();//descriptografar
                                    try {
                                        //v.vasculhaBDRestrictedRegion(c.decryptRegion(reg));//aqui retorna o objeto que está no banco de dados
                                        c.join();
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }

                                    //v.vasculhaBDRestrictedRegion(reg);//aqui retorna o objeto que está no banco de dados
                                    /*if (reg.distance(reg,r.getLatitude(),getLongitude()) <= 5) {
                                        v.inserirRestrictRegion(reg);
                                    }*/

                                }
                            }

                            // Cria um CountDownLatch com o tamanho da fila de regiões
                            //CountDownLatch latch = new CountDownLatch(regiao.size());

                            /*while (!regiao.isEmpty()) {
                                Region region = regiao.poll();
                                db.collection("RegionsData").add(region)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d("Adicionar Região ao BD", "DocumentSnapshot adicionado com ID: " + documentReference.getId());
                                                latch.countDown(); // Decrementa o contador do latch
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("Adicionar Região ao BD", "Erro ao adicionr documento", e);
                                                latch.countDown(); // Decrementa o contador do latch
                                            }
                                        });
                            }

                            // Aguarda até que todas as operações de adição de documentos sejam concluídas
                            latch.await();*/

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity3.this, "Regiões salvas no BD com sucesso.", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            semaphoro.release();
                        }
                    }
                }).start();

                /*for (Region reg : regiao) {
                    verificaDistancia(reg, new VerificaDistanciaCallback() {
                        @Override
                        public void onResult(boolean dentroDoRaio) {
                            if (!dentroDoRaio) {
                                verificaDocumentosUsuarios(reg, new VerificaDocumentosCallback() {
                                    @Override
                                    public void onResult(boolean documentoExiste) {

                                        if (!documentoExiste) {


                                            Map<String, Object> user = new HashMap<>();
                                            user.put("latitude", reg.getLatitude());
                                            user.put("longitude", reg.getLongitude());
                                            user.put("nome", reg.getName());
                                            user.put("timestamp", reg.getTimestamp());
                                            user.put("user", reg.getUser());


                                            // Adicione um novo documento com um ID gerado
                                            db.collection("Regioes")
                                                    .add(user)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w(TAG, "Error adding document", e);
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(MainActivity3.this, "documento já cadastrado", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(MainActivity3.this, "A região está dentro do raio de 30 metros de outra região existente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }*/
            }
        });///////////////

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
    ThreadLerDadosGPS lerDadosGPS = new ThreadLerDadosGPS(this);//Já lancei a thread
    //lerDadosGPS.start();
    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        latitudeLabel.setText("Latitude: " + latitude);
        longitudeLabel.setText("Longitude: " + longitude);

        LatLng latLng = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        mMap.addMarker(new MarkerOptions().position(latLng).title("Minha localização"));
        //t.Ler(location,latitude, longitude);
        setLatitudeLabel(latitude);
        setLongitudeLabel(longitude);
        lerDadosGPS.AtualizaCoordenadas(latitude,longitude, location);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    @Override
    public void onProviderEnabled(String provider) {
    }
    @Override
    public void onProviderDisabled(String provider) {
    }
    public void setLongitudeLabel(double l) {
        longitude = l;
    }
    public void setLatitudeLabel(double l) {
        latitude = l;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public void setAdicionaNaFila(Region r) {
        regiao.add(r);
    }//Adiciona região na fila
    public Queue<Region> getRegiao() {//Retorna a Fila
        return regiao;
    }//aqui veirfico a fila
    public Location getLocalizacao(){

        return localizacao;
    }
    public void setLocalizacao(Location l){
        localizacao = l;
    }
    public List<Location> getListaLocalizacao() {//Retorna a Fila
        return listaLocalizacao;
    }//aqui veirfico a fila
    public void setListaLocalizacao(Location l){
        listaLocalizacao.add(l);
    }

    //public void setNome(String nome){this.name=nome}

    /*public boolean verificaDocumentosUsuarios(Regioes reg) {
        AtomicBoolean documentoExiste = new AtomicBoolean(false);

        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (!querySnapshot.isEmpty()) {
                            documentoExiste.set(true);
                        }
                    } else {
                        // Trate o erro aqui
                    }
                });

        return documentoExiste.get();
    }*/
    public void verificaDocumentosUsuarios(Region reg, VerificaDocumentosCallback callback) {
        db.collection("Regioes")
                .whereEqualTo("latitude", reg.getLatitude())
                .whereEqualTo("longitude", reg.getLongitude())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        boolean documentoExiste = !querySnapshot.isEmpty();
                        callback.onResult(documentoExiste);
                    } else {
                        // Trate o erro aqui
                        callback.onResult(false); // Ou você pode passar o erro para o callback
                    }
                });
    }
    public void verificaDistancia(Region reg, VerificaDistanciaCallback callback) {
        db.collection("Regioes")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            double latitude = (double) document.get("latitude");
                            double longitude = (double) document.get("longitude");
                            double distance = calcularDistanciaEntrePontos(reg.getLatitude(), reg.getLongitude(), latitude, longitude);
                            if (distance < 30) {
                                callback.onResult(true);
                                return;
                            }
                        }
                        callback.onResult(false);
                    } else {
                        // Trate o erro aqui
                        callback.onResult(false); // Ou você pode passar o erro para o callback
                    }
                });
    }

    public double calcularDistanciaEntrePontos(double lat1, double lon1, double lat2, double lon2) {
        // Raio da Terra em metros
        final double R = 6371000;

        // Converte graus para radianos
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Diferença das latitudes e longitudes
        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        // Fórmula de Haversine
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distancia = R * c;

        return distancia;
    }

    interface VerificaDocumentosCallback {
        void onResult(boolean documentoExiste);
    }
    interface VerificaDistanciaCallback {
        void onResult(boolean dentroDoRaio);
    }
    public void verificaPodeinserir(Region region){
        while(!regiao.isEmpty()){
            Region reg = regiao.poll();//reg -> objeto de região
            VasculhaDB v = new VasculhaDB();
            if(reg instanceof Region){
                v.vasculhaBDRegion(reg);//aqui retorna o objeto que está no banco de dados

            }else if(reg instanceof SubRegion){//aqui retorna o objeto que está no banco de dados
               // v.vasculhaBDSubRegion(reg);

            }else{
                //v.vasculhaBDRestrictedRegion(reg);//aqui retorna o objeto que está no banco de dados
                /*if (reg.distance(reg,r.getLatitude(),getLongitude()) <= 5) {
                    v.inserirRestrictRegion(reg);
                }*/

            }
        }

    }

    /*Cria outro método para inserir no banco de dados de volra*/

}
