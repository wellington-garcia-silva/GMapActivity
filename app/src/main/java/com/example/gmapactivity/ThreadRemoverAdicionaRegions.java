/*package com.example.gmapactivity;

import androidx.annotation.NonNull;

import com.example.gmapactivity.Regioes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ThreadRemoverAdicionaRegions extends Thread {
    private Queue<Regioes> regiaoQueue;
    private FirebaseFirestore db;

    public ThreadRemoverAdicionaRegions(Queue<Regioes> regiaoQueue, FirebaseFirestore db) {
        this.regiaoQueue = regiaoQueue;
        this.db = db;
    }

    @Override
    public void run() {
        while (!regiaoQueue.isEmpty()) {
            Regioes regioes = regiaoQueue.poll();
            if (regioes != null) {
                // Supondo que 'regioes' tenha um identificador único ou um campo que possa ser usado para verificar a unicidade
                String uniqueField = regioes.getUniqueField(); // Substitua 'getUniqueField' pelo método real para obter o campo único
                DocumentReference docRef = db.collection("Regioes").document(uniqueField);

                db.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot snapshot = transaction.get(docRef);
                        if (!snapshot.exists()) {
                            // Documento não existe, adicionar 'regioes' ao banco de dados
                            transaction.set(docRef, regioes);
                        }
                        // Caso contrário, o documento já existe e não precisamos fazer nada
                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Transação bem-sucedida
                        System.out.println("Documento adicionado com sucesso.");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Falha na transação
                        System.err.println("Falha ao adicionar documento: " + e.getMessage());
                    }
                });
            }
        }
    }
}
*/