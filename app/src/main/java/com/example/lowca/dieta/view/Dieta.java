package com.example.lowca.dieta.view;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lowca.dietaAsignada.controller.dietaAsignadaController;
import com.example.lowca.dietaAsignada.view.Dieta2;
import com.example.lowca.R;
import com.example.lowca.dieta.controller.DietaController;
import com.example.lowca.dieta.modelo.ListDietas;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

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
    public ListDietas dietasModel;
    public List<ListDietas> elements;

    ListAdapterDietas listAdapter,listAdapter2;

    public DietaController dietaController;
    public dietaAsignadaController dietaAsignadaCon;
    Spinner spinnerDesayuno;

    View vista,view;
    Activity main;

    String [] datos;
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    private ProgressDialog progressDialog;
    //private DietaController dietaController;
    TextView tvDietaAsiganada;
    String numero_dieta,userUid,tipo_dieta, calorias,infoDieta,tipo,idDieta;
    Double calorias_dieta;
    int caloriasRecuperadas;
    Bundle datosDieta;
    String [] comidas;

    public  RecyclerView recyclerView;

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
        recyclerView= vista.findViewById(R.id.listRecyclerView);
        mAuth=FirebaseAuth.getInstance();
        userUid = mAuth.getCurrentUser().getUid();

        elements= new ArrayList<>();

        recyclerView = vista.findViewById(R.id.listRecyclerView);

        dietaController = new DietaController(this,mAuth,userUid);


        String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dietaController.obtenerDietas(userUID);
        //dietaController.obtenerDietaAsiganada(userUID);


        ListAdapterDietas adapter = new ListAdapterDietas(elements,getContext());


        dietaController.DietaControllerContext(getContext());

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Obteniendo Datos");
        progressDialog.setMessage("Cargando dietas");
        progressDialog.show();
        if(progressDialog.isShowing()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            },500);
        }

        return vista;

    }


    @Override
    public void onItemClick(int position) {
        if (elements != null && position < elements.size()) {

            ListDietas dieta = elements.get(position);

        } else {
            System.out.println("Tamaño elements: "+elements.size());
        }
    }

    /*public void viewDietasAsignada(ListDietas dietasModel){
        tvDietaAsiganada.setText(dietasModel.getDietaAsiganda());

    }*/


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





}