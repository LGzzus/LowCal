package com.example.lowca.perfil.Controller;

import android.view.View;
import android.widget.ArrayAdapter;

import com.example.lowca.perfil.Model.PerfilModel;
import com.example.lowca.perfil.View.Perfil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

public class PerfilController {
    private Perfil perfilF;
    public PerfilModel perfilModel;
    public FirebaseAuth mAuth;

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
    public void editarPerfil(String gender, String phyAct){
        String[] generos={"Mujer","Hombre"};
        List<String> genderList = Arrays.asList(generos);
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(perfilF.main,android.R.layout.simple_spinner_item,
                generos);
        perfilF.spinnerGen.setAdapter(adapter);

        int positiongender = genderList.indexOf(gender);
        perfilF.spinnerGen.setSelection(positiongender);

        String[] opciones={"Sedentaria","Moderada","Activa"};
        List<String> actList = Arrays.asList(opciones);
        ArrayAdapter<String> adapter2= new ArrayAdapter<String>(perfilF.main,android.R.layout.simple_spinner_item,
                opciones);
        perfilF.spinnerAct.setAdapter(adapter2);

        int positionAct = actList.indexOf(phyAct);
        perfilF.spinnerAct.setSelection(positionAct);

        perfilF.editarDatos.setVisibility(View.INVISIBLE);
        perfilF.guardarDatos.setVisibility(View.VISIBLE);
        perfilF.etGenero.setVisibility(View.INVISIBLE);
        perfilF.spinnerGen.setVisibility(View.VISIBLE);
        perfilF.etActividadF.setVisibility(View.INVISIBLE);
        perfilF.spinnerAct.setVisibility(View.VISIBLE);
        perfilF.etFechan.setEnabled(true);
        perfilF.etEstatura.setEnabled(true);
        perfilF.etPesoA.setEnabled(true);
        perfilF.etPesoO.setEnabled(true);
    }
}
