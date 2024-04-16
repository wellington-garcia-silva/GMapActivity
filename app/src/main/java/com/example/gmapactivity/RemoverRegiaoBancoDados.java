package com.example.gmapactivity;

import com.example.gmapactivity.Regioes;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

//import java.util.Queue;

public class RemoverRegiaoBancoDados extends Thread {
    //private FirebaseFirestore db;
    private Queue<Regioes> regioes;
    private boolean resultado = true;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public RemoverRegiaoBancoDados(FirebaseFirestore db, Queue<Regioes> regioes) {
        this.db = db;
        this.regioes = regioes;
    }

    @Override
    public void run() {
        while(resultado){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }


    public void getResultado(Queue<Regioes> regioesQueue) {
        AtomicBoolean resultado = new AtomicBoolean(true);

        for (Regioes reg : regioesQueue) {
            db.collection("regioes")
                    .whereEqualTo("latitude", reg.getLatitude())
                    .whereEqualTo("longitude", reg.getLongitude())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Se encontrar um documento com as mesmas coordenadas, muda o resultado para false
                                //resultado.set(false);
                                break;
                            }
                        } else {
                            // Trate o erro aqui
                        }
                    });
        }

        //return resultado.get();
    }
}
/*if (removerDB.getResultado()) {

                    db.collection("Regioes").whereArrayContains("regiao",getLatitude())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (!task.getResult().isEmpty()) {
                                        for (QueryDocumentSnapshot document: task.getResult()){
                                            Toast.makeText(getApplicationContext(), "Sintoma encontrado", Toast.LENGTH_SHORT).show();
                                            sintoma = db.document("sintomas/" + document.getId());
                                        }

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Sintoma não cadastardo", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                db.collection("users")///////////////////////////
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
                }*/
