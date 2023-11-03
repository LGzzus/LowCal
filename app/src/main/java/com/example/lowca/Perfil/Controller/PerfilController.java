package com.example.lowca.Perfil.Controller;


import android.app.ProgressDialog;
import android.util.Log;

import com.example.lowca.Perfil.Model.PerfilModel;
import com.example.lowca.Perfil.View.Perfil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PerfilController {
    private Perfil perfilF;
    public PerfilModel perfilModel;
    public FirebaseAuth mAuth;
    public  FirebaseFirestore db;

    public PerfilController(Perfil perfil) {
        this.perfilF = perfil;
        perfilModel = new PerfilModel();
    }
    public void loadPerfilData(String UserUID){
        loadAccount(UserUID);
        loadUser(UserUID);
        loadDatesAnthropomethics(UserUID);
    }
    public void loadAccount(String UserUID){
        DocumentReference account = FirebaseFirestore.getInstance().collection("account").document(UserUID);
        account.addSnapshotListener((documentSnapshot,e)->{
            if(e != null){

            }
            if(documentSnapshot != null && documentSnapshot.exists()){
                String mail = documentSnapshot.getString("mail");
                perfilModel.setCorre(mail);
                perfilF.viewData(perfilModel);
            }
        });
    }
    public void loadUser(String UserUID){
        DocumentReference account = FirebaseFirestore.getInstance().collection("user").document(UserUID);
        account.addSnapshotListener((documentSnapshot,e)->{
            if(e != null){

            }
            if(documentSnapshot != null && documentSnapshot.exists()){
                String name = documentSnapshot.getString("name");
                perfilModel.setName(name);
                perfilF.viewData(perfilModel);
            }
        });
    }
    public void loadDatesAnthropomethics(String UserUID){
        DocumentReference account = FirebaseFirestore.getInstance().collection("antropometric_dates").document(UserUID);
        account.addSnapshotListener((documentSnapshot,e)->{
            if(e != null){

            }
            if(documentSnapshot != null && documentSnapshot.exists()){
                String FechaND = documentSnapshot.getString("birth_date");
                String GeneroD = documentSnapshot.getString("gender");
                String EstaturaD = String.valueOf(documentSnapshot.getLong("height"));
                String PesoAD = String.valueOf(documentSnapshot.getLong("weight").floatValue());
                String PesoOD = String.valueOf(documentSnapshot.getLong("target_weight"));
                String ActividadD = documentSnapshot.getString("physical_activity_lever");
                perfilModel.setBirthDate(FechaND);
                perfilModel.setGender(GeneroD);
                perfilModel.setHeight(EstaturaD);
                perfilModel.setWeight(PesoAD);
                perfilModel.setPhysical_activity(ActividadD);
                perfilModel.setTargetWeight(PesoOD);
                perfilF.viewData(perfilModel);
            }
        });
    }
}