package com.example.lowca;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Inicio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Inicio extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View vista;
    TextView tvCaloriasDieta;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    String userUid;

    public Inicio() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Inicio.
     */
    // TODO: Rename and change types and number of parameters
    public static Inicio newInstance(String param1, String param2) {
        Inicio fragment = new Inicio();
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
        vista = inflater.inflate(R.layout.fragment_inicio, container, false);
        tvCaloriasDieta=vista.findViewById(R.id.tvCaloriasDieta);
        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        userUid = mAuth.getCurrentUser().getUid();


        CollectionReference parentCollectionRef = db.collection("account");
        DocumentReference documentRef = parentCollectionRef.document(userUid);
        CollectionReference subCollectionRef = documentRef.collection("dieta");
        Query query = subCollectionRef.orderBy("calorias", Query.Direction.DESCENDING);
        subCollectionRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String documentId=documentSnapshot.getId();

                        String dato = documentSnapshot.getString("calorias_usuario");
                        // Realiza las operaciones necesarias con los datos obtenidos de la subcolección
                        String  cal= dato;
                        System.out.println("********Calorias tablero: "+cal+"**********");
                        tvCaloriasDieta.setText(dato+" kcal");
                    }
                })
                .addOnFailureListener(e -> {
                    // Maneja el error en caso de que la lectura de la subcolección falle
                });



        /*

            Calcula el metabolismo basal (MB):

            Para hombres: MB = 66 + (13.75 x peso en kg) + (5 x altura en cm) - (6.75 x edad en años).
            Para mujeres: MB = 655 + (9.56 x peso en kg) + (1.85 x altura en cm) - (4.68 x edad en años).

            Determina el nivel de actividad física:

            Sedentario (poco o ningún ejercicio): MB x 1.2.
            Moderadamente activo (ejercicio moderado de 3-5 días por semana): MB x 1.55.

       */
        DocumentReference documentReferencia=db.collection("antropometric_dates").document(userUid);
        documentReferencia.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // El documento existe y contiene datos
                        String genero = documentSnapshot.getString("gender");
                        Double altura= documentSnapshot.getDouble("height");

                        Double peso= documentSnapshot.getDouble("weight");
                        String nivelActividad=documentSnapshot.getString("physical_activity_lever");
                        String nacido=documentSnapshot.getString("birth_date");
                        Double pesoObjetivo=documentSnapshot.getDouble("target_weight");

                        System.out.println("******NAcido: "+nacido+"************");




                        // Realiza las operaciones necesarias con los datos del documento
                    } else {
                        // El documento no existe
                    }
                })
                .addOnFailureListener(e -> {
                    // Maneja el error en caso de que la lectura del documento falle
                });




        return vista;
        //return inflater.inflate(R.layout.fragment_inicio, container, false);




    }
}