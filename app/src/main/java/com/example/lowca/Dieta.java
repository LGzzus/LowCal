package com.example.lowca;


import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lowca.Adaptadores.ListAdapterDietas;
import com.example.lowca.Adaptadores.OnItemClickListener;
import com.example.lowca.Models.ListDietas;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Dieta#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Dieta extends Fragment implements ListAdapterDietas.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<ListDietas> elements= new ArrayList<>();
    ListAdapterDietas listAdapter,listAdapter2;
    Spinner spinnerDesayuno;
    View vista,view;
    Activity main;

    String [] datos;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String mainDocumentId_1 = "baja_calorica";
    private String mainDocumentId_2 = "balanceada";
    TextView tvDietaAsiganada;
    String numero_dieta,userUid,tipo_dieta, calorias,infoDieta,tipo,idDieta;
    Double calorias_dieta;
    int caloriasRecuperadas;
    Bundle datosDieta;
    String [] comidas;

    public Dieta() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Dieta.
     */
    // TODO: Rename and change types and number of parameters
    public static Dieta newInstance(String param1, String param2) {
        Dieta fragment = new Dieta();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //spinnerDesayuno=(Spinner) findViewById(R.id.spinnerDesayuno);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        main = getActivity();
        vista = inflater.inflate(R.layout.fragment_dieta, container, false);
        tvDietaAsiganada=vista.findViewById(R.id.tvDietaAsignada);


        iniciarFirebase();
       obtenerDietaAsiganada();
        recuperarDietas();



        return vista;
       // return inflater.inflate(R.layout.fragment_dieta, container, false);
    }
    private void iniciarFirebase(){
        mAuth=FirebaseAuth.getInstance();
        userUid = mAuth.getCurrentUser().getUid();
        db=FirebaseFirestore.getInstance();
    }

    @Override
    public void onItemClick(int position) {
        // Manejar el clic del elemento en la posición 'position' del RecyclerView
        ListDietas dieta = elements.get(position);



        // Haz lo que necesites con la dieta seleccionada, por ejemplo, mostrar detalles, etc.


    }
    public void recuperarDietas(){
        verificarPeso();

        db.collection("dieta")
                .whereEqualTo("tipo", tipo_dieta)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        RecyclerView recyclerView= vista.findViewById(R.id.listRecyclerView);

                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();
                            //List<ListDietas>  dietasRecuperadas=  new ArrayList<>();
                            elements= new ArrayList<>();


                            for (DocumentSnapshot document : documents) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                idDieta= document.getId();
                                Log.d("Dieta id: ",idDieta);
                                calorias_dieta=document.getDouble("calorias");
                                System.out.println("///Calorias de la dieta: "+ calorias_dieta+"////////");
                                numero_dieta=document.getString("num_dieta");
                                caloriasRecuperadas=Double.valueOf(calorias_dieta).intValue();
                                System.out.println(numero_dieta+"*******");
                               // calorias = (Double.toString(calorias_dieta ))+" calorias";
                                calorias=String.valueOf(caloriasRecuperadas+" Kcal");
                                //infoDieta=document.getString("descripcion");
                               //String calorias= String.valueOf(caloriasRecuperadas+" calorias");
                                 tipo=document.getString("tipo");
                                System.out.println("Tipo: "+tipo);
                                String desayuno=document.getString("desayuno");
                                String almuerzo=document.getString("almuerzo");
                                String cena=document.getString("cena");
                                comidas=new String[]{desayuno,almuerzo,cena};


                                /**Revisar lo de infoDieta */
                                elements.add(new ListDietas(numero_dieta,calorias,comidas,tipo_dieta,idDieta));

                                //lista para enviar a Dieta

                                //datos= new String[]{tipo,calorias,infoDieta};


                            }

                            listAdapter= new ListAdapterDietas(elements,getContext());
                            listAdapter.setDietaFragment(Dieta.this);
                            listAdapter.setOnItemClickListener(Dieta.this);
                            System.out.println("****Datos a enviar: "+datos+" ********");
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(listAdapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public void recibirDatos(String tipoDieta, String calorias, String [] infoDieta,String idDieta,String numDieta) {
        // Aquí recibes los datos desde el adaptador ListAdapterDietas y puedes hacer lo que necesites con ellos

        Bundle datosDieta = new Bundle();
        Intent intent = new Intent(getContext(), Dieta2.class);

        // Agregamos los datos al Bundle
        datosDieta.putString("num_dieta",numDieta);
        datosDieta.putString("tipo_dieta", tipoDieta);
        datosDieta.putString("calorias", calorias);
        datosDieta.putString("desayuno", infoDieta[0]);
        datosDieta.putString("almuerzo", infoDieta[1]);
        datosDieta.putString("cena", infoDieta[2]);
        datosDieta.putString("id_dieta",idDieta);


        // Agregamos el Bundle al Intent
        intent.putExtras(datosDieta);

        // Iniciamos la actividad Dieta2 con los datos
        startActivity(intent);
    }



    public void verificarPeso(){
        DocumentReference documentReferencia=db.collection("antropometric_dates").document(userUid);
            documentReferencia.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // El documento existe, puedes obtener los campos aquí
                            Double peso= document.getDouble("weight");
                            Double pesoObjetivo=document.getDouble("target_weight");

                            if(pesoObjetivo < peso){
                                tipo_dieta="baja en calorias";

                            }else{
                                tipo_dieta="dieta hipercalorica";
                            }
                            System.out.println("***Tipo de dieta para el usuario"+tipo_dieta+" ********");

                            // Haz lo que necesites con los campos recuperados
                        } else {
                            // El documento no existe, puedes manejarlo aquí
                        }
                    } else {
                        // Error al obtener el documento, puedes manejarlo aquí
                    }
                }
            });
    }

    public void obtenerDietaAsiganada(){
        DocumentReference userDocRef = db.collection("dieta_asignada").document(userUid);

        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String num_dieta= document.getString("num_dieta");

                        tvDietaAsiganada.setText(num_dieta);

                    } else {
                        // El documento no existe
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