package com.example.lowca;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Dieta#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Dieta extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Spinner spinnerDesayuno;
    View vista;
    Activity main;
    private ImageButton btnDieta1,btnDieta2,btnDieta3;
    String [] datos;

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
        btnDieta1=vista.findViewById(R.id.btnDieta1);
        btnDieta2=vista.findViewById(R.id.btnDieta2);
        btnDieta3=vista.findViewById(R.id.btnDieta3);



        //Dieta 1: Baja en calorias  1325 calorias totales
         btnDieta1.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Bundle pasarDatos=new Bundle();
                 String nombreDieta=" Baja en calorias";
                 String totalCalorias="1325";
                 String infoDieta="dieta1";
                 datos= new String[]{nombreDieta,totalCalorias,infoDieta};

                 Intent intent= new Intent(getContext(),Dieta2.class);
                 pasarDatos.putStringArray("keyDatos",datos);
                 intent.putExtras(pasarDatos);
                 startActivity(intent);
             }
         });


        //Dieta 2:Baja en carbohidratos  1200 calorias totales
        btnDieta2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle pasarDatos=new Bundle();
                String nombreDieta=" Baja en carbohidratos";
                String totalCalorias="1200";
                String infoDieta="dieta2";
                datos= new String[]{nombreDieta,totalCalorias,infoDieta};

                Intent intent= new Intent(getContext(),Dieta2.class);
                pasarDatos.putStringArray("keyDatos",datos);
                intent.putExtras(pasarDatos);
                startActivity(intent);
            }
        });

        //Dieta 3: Vegetariana   1600 calorias totales

        btnDieta3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle pasarDatos=new Bundle();
                String nombreDieta=" Vegetariana";
                String totalCalorias="1600";
                String infoDieta="dieta3";
                datos= new String[]{nombreDieta,totalCalorias,infoDieta};
                Intent intent= new Intent(getContext(),Dieta2.class);
                pasarDatos.putStringArray("keyDatos",datos);
                intent.putExtras(pasarDatos);
                startActivity(intent);
            }
        });
        return vista;
       // return inflater.inflate(R.layout.fragment_dieta, container, false);
    }
}