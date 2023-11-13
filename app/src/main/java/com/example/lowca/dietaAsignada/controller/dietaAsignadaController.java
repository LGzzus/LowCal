package com.example.lowca.dietaAsignada.controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.lowca.dieta.modelo.ListDietas;
import com.example.lowca.dieta.view.Dieta;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class dietaAsignadaController {
    private final FirebaseAuth mAuth;
    private final String userUid;
    private Dieta dieta;

    public ListDietas dietasModel;

    public dietaAsignadaController(Dieta dieta, FirebaseAuth mAuth, String userUid ){
        this.dieta=dieta;
        this.mAuth=mAuth;
        this.userUid=userUid;

    }

    public void obtenerDietaAsiganada(String userUid){
        DocumentReference userDocRef = dieta.db.collection("dieta_asignada").document(userUid);

        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String num_dieta= document.getString("num_dieta");
                        dietasModel.setDietaAsiganda(num_dieta);



                    } else {

                        Log.d("Firestore", "El documento no existe");
                    }
                } else {
                    // Error al obtener el documento
                    Log.e("Firestore", "Error al obtener el documento", task.getException());
                }
            }
        });


    }



}
