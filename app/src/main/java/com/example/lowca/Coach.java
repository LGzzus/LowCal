package com.example.lowca;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.lowca.Adaptadores.ListViewAlimentosAdapter;
import com.example.lowca.Adaptadores.ListViewEjerciciosAdapter;
import com.example.lowca.Models.Alimentos;
import com.example.lowca.Models.Ejercicios;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

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
    private ArrayList<Ejercicios> listaEjercicios = new ArrayList<Ejercicios>();
    ArrayAdapter<Ejercicios> arrayAdapterPersona;
    ListViewEjerciciosAdapter listViewEjerciciosAdapter;
    LinearLayout linearEntrenado, linearEjercicio;
    ListView listViewEntrena;
    //Spinner spinnerCategoria, spinnerAlimento, spinnerCantidad;
    //Button btnAgregarAlimento,btnAgregarEjercicio, btnAlimentacionMas , btnEjercicioMas;
    //Se usara para que se identique el objeto
    Ejercicios ejercicioSelecicionado;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_coach, container, false);

        // Ejercicios generales
        //listaEjercicios = vista.findViewById(R.id.listaEjercicios);
        // Ejercicios que ha hecho el usuario
        listViewEntrena = vista.findViewById(R.id.listaEntrena);
        /**
        btnAlimentacionMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarAlimento(view);
            }
        });
         **/

        inicializarFirebase();
        listarEjercicios();

        return vista;
    }

    private void inicializarFirebase(){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }
    private void listarEjercicios(){
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
            System.out.println(e);
        }
    }
}