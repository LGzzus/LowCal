package com.example.lowca;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lowca.Adaptadores.ListViewAlimentosAdapter;
import com.example.lowca.Models.Alimentos;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Agregar_mas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Agregar_mas extends Fragment {
    View view;
    private ArrayList<Alimentos> listAlimentos = new ArrayList<Alimentos>();
    ArrayAdapter<Alimentos> arrayAdapterPersona;
    ListViewAlimentosAdapter listViewAlimentosAdapter;
    LinearLayout linearLayoutAgregarComida;

    ListView listViewAlimentos, listViewEjericicios;
    Spinner spinnerCategoria, spinnerAlimento, spinnerCantidad;
    Button btnAgregarAlimento,btnAgregarEjercicio, btnAlimentacionMas , btnEjercicioMas;
    //Se usara para que se identique el objeto
    Alimentos alimentoSelecicionado;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    public String userId;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Agregar_mas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Agregar_mas.
     */
    // TODO: Rename and change types and number of parameters
    public static Agregar_mas newInstance(String param1, String param2) {
        Agregar_mas fragment = new Agregar_mas();
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
        view = inflater.inflate(R.layout.fragment_agregar_mas, container, false);
        spinnerCategoria = (Spinner) view.findViewById(R.id.spinnerCategoria);
        spinnerAlimento = (Spinner) view.findViewById(R.id.spinnerAlimento);
        spinnerCantidad = (Spinner) view.findViewById(R.id.spinnerCantidad);

        btnAgregarAlimento = (Button) view.findViewById(R.id.btnAgregarComida);

        listViewAlimentos = view.findViewById(R.id.listViewAlimentos);
        listViewEjericicios = view.findViewById(R.id.listViewEjercicios);
        btnAlimentacionMas = view.findViewById(R.id.btnAlimentacionMas);
        btnAlimentacionMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarAlimento(view);
            }
        });
        inicializarFirebase();
        listarAlimentos();
        return view;
    }

    private void inicializarFirebase(){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }
    private void listarAlimentos(){
        try {
            userId = mAuth.getCurrentUser().getUid();
            DocumentReference acountRef = db.collection("account").document(userId);
            CollectionReference alimentosRef = acountRef.collection("eat");
            alimentosRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    listAlimentos.clear(); // Limpiar la lista actual de alimentos
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Obtener los datos de cada documento y agregarlos a la lista
                        Alimentos alimento = document.toObject(Alimentos.class);
                        alimento.setAlimento(document.getString("eat"));
                        alimento.setCalorias(document.getString("category"));
                        alimento.setCantidad(document.getString("amount"));
                        listAlimentos.add(alimento);
                    }
                    // Crear el adaptador para el ListView y asignarlo
                    listViewAlimentosAdapter = new ListViewAlimentosAdapter(getActivity(), listAlimentos);
                    listViewAlimentos.setAdapter(listViewAlimentosAdapter);
                } else {
                    // Manejar el caso de error
                    System.out.println("Error en el momento de mostar la lista de alimentos");
                }
            });
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public void agregarAlimento(View view){
        Context context = getContext();
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(
                context
        );
        View mView = getLayoutInflater().inflate(R.layout.agregar_comida,null);
        Button btnAgregarComida = (Button) mView.findViewById(R.id.btnAgregarComida);
        Spinner spinnerCategoria = mView.findViewById(R.id.spinnerCategoria);
        Spinner spinnerAlimento = mView.findViewById(R.id.spinnerAlimento);
        Spinner spinnerCantidad = mView.findViewById(R.id.spinnerCantidad);
        spinnerCategoria.setSelection(0);
        spinnerAlimento.setSelection(0);
        spinnerCantidad.setSelection(0);
        String categorias = spinnerCategoria.getSelectedItem().toString();
        String alimentos = spinnerAlimento.getSelectedItem().toString();
        String cantidades = spinnerCantidad.getSelectedItem().toString();

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        btnAgregarComida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userId = mAuth.getCurrentUser().getUid();
                String categoria = categorias;
                String alimento = alimentos;
                String cantidad = cantidades;
                Alimentos alimentoss = new Alimentos();
                alimentoss.setAlimento(alimento);
                alimentoss.setCategoria(categoria);
                alimentoss.setCantidad(cantidad);
                alimentoss.setFechaRegistro(getFechaNormal(getFechaMilisegundos()));
                DocumentReference acountRef = db.collection("account").document(userId);
                CollectionReference alimentosRef = acountRef.collection("eat");
                DocumentReference nuevoAlimentoRef = alimentosRef.document();
                Map<String,Object> alimentosDb=new HashMap<>();
                alimentosDb.put("category",categoria);
                alimentosDb.put("eat",alimento);
                alimentosDb.put("amount",cantidad);
                //alimentosDb.put("day",1);
                nuevoAlimentoRef.set(alimentosDb).addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                System.out.println("Se agrego correctamente" + alimento);
                                //Toast.makeText(Agregar_mas.this,"El alimento se agrego correctamente",Toast.LENGTH_SHORT).show();
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

    public long getFechaMilisegundos(){
        Calendar calendar = Calendar.getInstance();
        long timepoUnix = calendar.getTimeInMillis();
        return timepoUnix;
    }
    public String getFechaNormal(long fechaMilisegundos){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GTM-5"));
        String fecha = sdf.format(fechaMilisegundos);
        return fecha;
    }

}