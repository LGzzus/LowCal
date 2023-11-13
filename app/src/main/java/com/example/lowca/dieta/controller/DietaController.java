package com.example.lowca.dieta.controller;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.lowca.dietaAsignada.view.Dieta2;
import com.example.lowca.dieta.modelo.ListDietas;
import com.example.lowca.dieta.view.Dieta;
import com.example.lowca.dieta.view.ListAdapterDietas;
import com.example.lowca.perfil.Model.PerfilModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class DietaController {
    private Dieta dieta;
    private ListAdapterDietas adaptadorDieta;
    public ListDietas dietasModel;
    public PerfilModel perfilModel;
    public Dieta2 viewDietaAsignada;
    public FirebaseAuth mAuth;
    //public FirebaseFirestore db;

    public String numero_dieta,userUid, calorias,infoDieta,tipo,idDieta;
    public String tipo_dieta;


    //public List<ListDietas> elements= new ArrayList<>();

    ListAdapterDietas listAdapter;

    Double calorias_dieta;
    String [] comidas;
    int caloriasRecuperadas;
    public Context context;
    public DietaController(Dieta dieta,FirebaseAuth mAuth, String userUid ){
        this.dieta=dieta;
        this.mAuth=mAuth;
        this.userUid=userUid;
        dietasModel= new ListDietas();
    }
    public void DietaControllerContext(Context context) {
        this.context = context;
    }

    public void obtenerDatos(){

        if(tipo_dieta!=null && !tipo_dieta.isEmpty()) {

            FirebaseFirestore.getInstance().collection("dieta")
                    .whereEqualTo("tipo", tipo_dieta)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {


                            if (task.isSuccessful()) {
                                List<DocumentSnapshot> documents = task.getResult().getDocuments();

                                //elements = new ArrayList<>();


                                for (DocumentSnapshot document : documents) {
                                    //Log.d(TAG, document.getId() + " => " + document.getData());
                                    idDieta = document.getId();
                                    dietasModel.setId_dieta(idDieta);



                                    Log.d("Dieta id: ", idDieta);
                                    calorias_dieta = document.getDouble("calorias");
                                    dietasModel.setCalorias(calorias);


                                    System.out.println("///Calorias de la dieta: " + calorias_dieta + "////////");
                                    numero_dieta = document.getString("num_dieta");
                                    dietasModel.setDieta(numero_dieta);


                                    caloriasRecuperadas = Double.valueOf(calorias_dieta).intValue();
                                    System.out.println(numero_dieta + "*******");

                                    calorias = String.valueOf(caloriasRecuperadas + " Kcal");


                                    tipo = document.getString("tipo");
                                    dietasModel.setTipoDieta(tipo);


                                    System.out.println("Tipo: " + tipo);

                                    String desayuno = document.getString("desayuno");
                                    String almuerzo = document.getString("almuerzo");
                                    String cena = document.getString("cena");
                                    comidas = new String[]{desayuno, almuerzo, cena};
                                    dietasModel.setInfoDieta(comidas);


                                    dieta.elements.add(new ListDietas(numero_dieta, calorias, comidas, tipo_dieta, idDieta));


                                }
                            }
                            listAdapter= new ListAdapterDietas(dieta.elements,context);
                            listAdapter.setDietaFragment(dieta);
                            listAdapter.setOnItemClickListener(dieta);

                            dieta.recyclerView.setHasFixedSize(true);
                            dieta.recyclerView.setLayoutManager(new LinearLayoutManager(context));
                            dieta.recyclerView.setAdapter(listAdapter);

                        }

                    });
        }else {
            Log.e("Firestore", "Tipo de dieta nulo o vacío");
        }

    }
    public void  obtenerDietas(String userUid){
        DocumentReference documentReferencia=dieta.db.collection("antropometric_dates").document(userUid);
        documentReferencia.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Double peso= document.getDouble("weight");
                        Double pesoObjetivo=document.getDouble("target_weight");

                        if(pesoObjetivo < peso){
                            tipo_dieta="baja en calorias";

                        }else{
                            tipo_dieta="dieta hipercalorica";
                        }
                        obtenerDatos();

                    }
                }
            }

        });
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

                        //dieta.viewDietasAsignada(dietasModel);

                        //viewDietaAsignada.viewDieta(dietasModel);


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
