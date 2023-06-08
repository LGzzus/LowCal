package com.example.lowca;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lowca.Adaptadores.ListViewEjerciciosAdapter;
import com.example.lowca.Models.Ejercicios;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Coach#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Coach extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Variables de entorno
    View vista;
    private ArrayList<Ejercicios> listaEjercicios = new ArrayList<>();
    //private ArrayList<Generales> listaGenerales = new ArrayList<>();
    ListViewEjerciciosAdapter listViewEjerciciosAdapter;
    //ListViewGeneralesAdapter listViewGeneralesAdapter;
    ListView listViewEntrena, listaViewEjercicios;
    Spinner spinnerEjercicio;
    EditText txtDuracion;
    Button btnAgregarEjercicio, btnEjercicioMas;

    // TODO: Variables de conexion
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    public String userId;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Coach() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Coach.
     */
    // TODO: Rename and change types and number of parameters
    public static Coach newInstance(String param1, String param2) {
        Coach fragment = new Coach();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_coach, container, false);

        spinnerEjercicio = (Spinner) vista.findViewById(R.id.spinnerEjercicio);
        txtDuracion = (EditText) vista.findViewById(R.id.txtDuracion);

        btnAgregarEjercicio = (Button) vista.findViewById(R.id.btnAgregarEjercicio);
        btnEjercicioMas = vista.findViewById(R.id.btnEjercicioMas3);

        // Ejercicios generales
        listaViewEjercicios = vista.findViewById(R.id.listaEjercicios);
        // Ejercicios que ha hecho el usuario
        listViewEntrena = vista.findViewById(R.id.listaRealizados);

        btnEjercicioMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarEjercicio(view);
            }
        });

        inicializarFirebase();
        listarEjerciciosRealizados();
        //listarEjerciciosGenerales();

        return vista;
    }

    private void inicializarFirebase(){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }
    private void listarEjerciciosRealizados(){
        try {
            userId = mAuth.getCurrentUser().getUid();
            DocumentReference acountRef = db.collection("account").document(userId);
            CollectionReference ejerciciosRef = acountRef.collection("ejercicio");
            ejerciciosRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    listaEjercicios.clear(); // Limpiar la lista actual de alimentos
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Obtener los datos de cada documento y agregarlos a la lista
                        Ejercicios ejercicio = document.toObject(Ejercicios.class);
                        ejercicio.setEjercicio(document.getString("nombre"));
                        ejercicio.setDuracion(document.getString("duracion"));
                        ejercicio.setCalorias(document.getString("calorias_quemadas"));

                        listaEjercicios.add(ejercicio);
                    }
                    // Crear el adaptador para el ListView y asignarlo
                    listViewEjerciciosAdapter = new ListViewEjerciciosAdapter(getActivity(), listaEjercicios);
                    listViewEntrena.setAdapter(listViewEjerciciosAdapter);
                } else {
                    // Manejar el caso de error
                    System.out.println("Error en el momento de mostar la lista de alimentos");
                }
            });
        } catch (Exception e){
            Toast.makeText(this.getContext(), "Error: "+e, Toast.LENGTH_SHORT).show();
            //System.out.println(e);
        }
    }

    //Esta funcion no esta completa...
    //Al dar clic en el boton "+" se sale de la app
    public void agregarEjercicio(View view){
        View mView = getLayoutInflater().inflate(R.layout.agregar_ejercicio,null);
        Context context = getContext();
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);

        spinnerEjercicio.setSelection(0);
        String ejercicios = spinnerEjercicio.getSelectedItem().toString();
        String duracion = String.valueOf(txtDuracion.getText());

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        btnAgregarEjercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userId = mAuth.getCurrentUser().getUid();
                String ejercicio = ejercicios;
                int calxmin = Integer.parseInt(duracion);
                calxmin = calxmin*12;

                Ejercicios eje = new Ejercicios();
                eje.setEjercicio(ejercicio);
                eje.setDuracion(duracion);
                eje.setCalorias(String.valueOf(calxmin));

                DocumentReference acountRef = db.collection("account").document(userId);
                CollectionReference ejercicioRef = acountRef.collection("ejercicio");
                DocumentReference nuevoEjercicioRef = ejercicioRef.document();

                Map<String,Object> ejerciciosDb = new HashMap<>();
                ejerciciosDb.put("calorias_quemadas",String.valueOf(calxmin));
                ejerciciosDb.put("duracion",duracion);
                ejerciciosDb.put("ejercicio",ejercicio);

                nuevoEjercicioRef.set(ejerciciosDb).addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                System.out.println("Se agrego correctamente" + ejercicio);
                                //Toast.makeText(getContext(),"El alimento se agrego correctamente",Toast.LENGTH_SHORT).show();
                            }
                        }
                ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Ocurrio un error");
                    }
                });

            }
        });
    }


    /**
    private void listarEjerciciosGenerales(){
        try {
            //userId = mAuth.getCurrentUser().getUid();
            CollectionReference generalesRef = db.collection("exercise");
            //CollectionReference ejerciciosRef = ejerciciosRef.collection("exercise");
            generalesRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    listaGenerales.clear(); // Limpiar la lista actual de alimentos
                    for (DocumentSnapshot document : task.getResult()) {
                        // Obtener los datos de cada documento y agregarlos a la lista
                        Generales generales = document.toObject(Generales.class);
                        generales.setNombre(document.getString("ejercicio"));
                        generales.setCalxmin(document.getString("calxmin"));
                        generales.setRepeticiones(document.getString("repeticiones"));
                        generales.setSets(document.getString("series"));

                        listaGenerales.add(generales);
                    }
                    // Crear el adaptador para el ListView y asignarlo
                    listViewGeneralesAdapter = new ListViewGeneralesAdapter(getActivity(), listaGenerales);
                    listaViewEjercicios.setAdapter(listViewGeneralesAdapter);
                } else {
                    // Manejar el caso de error
                    System.out.println("Error en el momento de mostar la lista de alimentos");
                }
            });
        } catch (Exception e){
            Toast.makeText(this.getContext(), "Error: "+e, Toast.LENGTH_SHORT).show();
            //System.out.println(e);
        }
     }

     */

}