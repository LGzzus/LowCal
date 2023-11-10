package com.example.lowca.perfil.Controller;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.lowca.model.DatosAnt;
import com.example.lowca.perfil.Model.PerfilModel;
import com.example.lowca.perfil.View.Perfil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PerfilController {
    private Perfil perfilF;
    public PerfilModel perfilModel;
    public FirebaseAuth mAuth;
    DatosAnt datosA = new DatosAnt();

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
    public void saveDataNew (String userUID){
        try {
            if(validar()){
                DatosAnt datosA = new DatosAnt();
                datosA.setFechaN(perfilF.etFechan.getText().toString());
                datosA.setGenero(perfilF.spinnerGen.getSelectedItem().toString());
                datosA.setEstatura(Integer.parseInt(perfilF.etEstatura.getText().toString()));
                datosA.setPesoO(Float.parseFloat((perfilF.etPesoO.getText().toString())));
                Float pesoA = Float.parseFloat(perfilF.etPesoA.getText().toString());
                datosA.setActividadF(perfilF.spinnerAct.getSelectedItem().toString());
                Map<String, Object> dat = new HashMap<>();
                dat.put("birth_date",datosA.getFechaN());
                dat.put("gender", datosA.getGenero());
                dat.put("height", datosA.getEstatura());
                dat.put("weight",pesoA);
                dat.put("physical_activity_lever",datosA.getActividadF());
                dat.put("target_weight",datosA.getPesoO());

                FirebaseFirestore.getInstance().collection("antropometric_dates").document(userUID).update(dat);
                Toast.makeText(perfilF.main,"Actualizando",Toast.LENGTH_LONG).show();
                perfilF.editarDatos.setVisibility(View.VISIBLE);
                perfilF.guardarDatos.setVisibility(View.INVISIBLE);
                perfilF.etGenero.setVisibility(View.VISIBLE);
                perfilF.spinnerGen.setVisibility(View.INVISIBLE);
                perfilF.etActividadF.setVisibility(View.VISIBLE);
                perfilF.spinnerAct.setVisibility(View.INVISIBLE);
                perfilF.etFechan.setEnabled(false);
                perfilF.etGenero.setEnabled(false);
                perfilF.etEstatura.setEnabled(false);
                perfilF.etPesoA.setEnabled(false);
                perfilF.etActividadF.setEnabled(false);
                perfilF.etPesoO.setEnabled(false);
            }
        }catch (Exception e){

        }
    }
    public boolean validar(){
        boolean retorno = true;
        String FechaNac = datosA.getFechaN();
        String Genero = datosA.getGenero();
        String Estatura = perfilF.etEstatura.getText().toString();
        String PesoAc = perfilF.etPesoA.getText().toString();
        String ActividadFis = datosA.getActividadF();
        /*if(etFechan.getText()==null){
            etFechan.setError("Llena el campo");
            retorno = false;
        }*/
        if (perfilF.spinnerGen.getSelectedItemPosition()==0){
            Toast.makeText(perfilF.main,"Seleccione una opcion",Toast.LENGTH_LONG).show();
            retorno = false;
        }
        if (Estatura.isEmpty()){
            perfilF.etEstatura.setError("Ingresa tu estatura correcta");
            retorno = false;
        }
        if (PesoAc.isEmpty()){
            perfilF.etPesoA.setError("Ingresa tu Peso correcto");
            retorno = false;
        }
        if(perfilF.spinnerAct.getSelectedItemPosition()==0){
            Toast.makeText(perfilF.main,"Selecciona la opcion correcta",Toast.LENGTH_LONG).show();
            retorno = false;
        }
        return retorno;
    }
}
