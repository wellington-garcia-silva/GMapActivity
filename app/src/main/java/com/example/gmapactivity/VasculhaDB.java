package com.example.gmapactivity;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mylibrary.Cryptography;
import com.example.mylibrary.Region;
import com.example.mylibrary.RestrictedRegion;
import com.example.mylibrary.SubRegion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class VasculhaDB {
    private boolean b1,b2,b3 = true;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void vasculhaBDRegion(Region reg){//criar 3 vasculhaBD para percorrer region, subregion e restrictRegion
        db.collection("regions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Region region = document.toObject(Region.class);//Aqui tenho o objeto que veio do BD
                                //regionsList.add(region);
                                // Aqui você pode usar o objeto region conforme necessário
                                //Apos comparar os objetos, inserir no banco de dados, se possível
                                if (reg.distance(region,reg.getLatitude(),reg.getLongitude()) <= 30) {
                                    b1 = false;
                                }
                            }
                            if (b1){
                                //criptografar
                                Cryptography c = new Cryptography();
                                try {
                                    inserirRegion(c.encryptRegion(reg));
                                    c.join();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                                b1=true;
                            }
                            // Aqui você pode processar a lista de regiões após todos os documentos serem recuperados
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public void vasculhaBDSubRegion(SubRegion reg){//criar 3 vasculhaBD para percorrer region, subregion e restrictRegion
        db.collection("subRegions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Region region = document.toObject(Region.class);//Aqui tenho o objeto que veio do BD
                                //regionsList.add(region);
                                // Aqui você pode usar o objeto region conforme necessário
                                //Apos comparar os objetos, inserir no banco de dados, se possível
                                if (reg.distance(region,reg.getLatitude(),reg.getLongitude()) <= 5) {
                                    b2 = false;
                                }

                            }
                            if (b2){
                                Cryptography c = new Cryptography();
                                try {
                                    inserirSubRegion(c.encryptRegion(reg));
                                    c.join();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            b2=true;

                            // Aqui você pode processar a lista de regiões após todos os documentos serem recuperados
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public void vasculhaBDRestrictedRegion(RestrictedRegion reg){//criar 3 vasculhaBD para percorrer region, subregion e restrictRegion
        db.collection("restrictRegions")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Region region = document.toObject(Region.class);//Aqui tenho o objeto que veio do BD
                                //regionsList.add(region);
                                // Aqui você pode usar o objeto region conforme necessário
                                //Apos comparar os objetos, inserir no banco de dados, se possível
                                if (reg.distance(region,reg.getLatitude(),reg.getLongitude()) <= 5) {
                                    b3 = false;
                                }

                            }
                            if (b3){
                                Cryptography c = new Cryptography();
                                try {
                                    inserirRestrictRegion(c.encryptRegion(reg));
                                    c.join();
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            b3=true;
                            // Aqui você pode processar a lista de regiões após todos os documentos serem recuperados
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public void inserirRegion(Region region){
        db.collection("RegionsData").add(region)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Adicionar Região ao BD", "DocumentSnapshot adicionado com ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Adicionar Região ao BD", "Erro ao adicionr documento", e);

                    }
                });
    }
    public void inserirSubRegion(Region region){
        db.collection("SubRegions").add(region)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Adicionar Região ao BD", "DocumentSnapshot adicionado com ID: " + documentReference.getId());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Adicionar Região ao BD", "Erro ao adicionr documento", e);
                    }
                });
    }
    public void inserirRestrictRegion(Region region){
        db.collection("RegionsData").add(region)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Adicionar Região ao BD", "DocumentSnapshot adicionado com ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Adicionar Região ao BD", "Erro ao adicionr documento", e);
                    }
                });
    }
}
