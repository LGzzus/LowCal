package com.example.lowca.perfil.View;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lowca.R;
import com.example.lowca.perfil.Controller.PerfilController;
import com.example.lowca.perfil.Model.PerfilModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class Perfil extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private PerfilController perfilController;
    public View vista;
    public  TextView tvNombre, tvCorreo, tvPesoO;
    public EditText etGenero, etPesoA, etEstatura, etActividadF, etFechan, etPesoO;
    public Button cerrarSesion, editarDatos, guardarDatos,btnExportarInfo;
    public Spinner spinnerGen, spinnerAct;
    public FirebaseAuth mAuth;
    public  FirebaseFirestore db;
    String userID;
    private ProgressDialog progressDialog;
    public  ProgressDialog progressDialogsingOut;
    public Activity main;
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult( new ActivityResultContracts.RequestPermission(), isTrue -> {
       if(isTrue){
           Toast.makeText(main,"Permisos Concedidos",Toast.LENGTH_SHORT).show();
       }else{
           Toast.makeText(main,"Permisos Rechazados",Toast.LENGTH_SHORT).show();
       }
    });
    public static Perfil newInstance(String param1, String param2) {
        Perfil fragment = new Perfil();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Perfil() {
        // Required empty public constructor
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

        main = getActivity();
        vista = inflater.inflate(R.layout.fragment_perfil, container, false);
        tvNombre = (TextView) vista.findViewById(R.id.tvNombre);
        tvCorreo = (TextView) vista.findViewById(R.id.tvCorreo);
        etEstatura = (EditText) vista.findViewById(R.id.etEstaturaD);
        etFechan = (EditText) vista.findViewById(R.id.etFechaN);
        etGenero = (EditText) vista.findViewById(R.id.etGenero);
        etPesoA = (EditText) vista.findViewById(R.id.etPesoA);
        etActividadF = (EditText) vista.findViewById(R.id.etActf);
        etPesoO = (EditText) vista.findViewById(R.id.tvPesoO);
        cerrarSesion = (Button) vista.findViewById(R.id.cerrar_sesion);
        editarDatos = (Button) vista.findViewById(R.id.editar);
        guardarDatos = (Button) vista.findViewById(R.id.guardar);
        spinnerGen = (Spinner) vista.findViewById(R.id.spinnerGenero);
        spinnerAct = (Spinner) vista.findViewById(R.id.spinnerActividad);
        guardarDatos.setVisibility(View.INVISIBLE);
        spinnerGen.setVisibility(View.INVISIBLE);
        spinnerAct.setVisibility(View.INVISIBLE);
        btnExportarInfo=vista.findViewById(R.id.btnExportarInfo);

        etGenero.setEnabled(false);
        etEstatura.setEnabled(false);
        etPesoA.setEnabled(false);
        etFechan.setEnabled(false);
        etActividadF.setEnabled(false);
        etPesoO.setEnabled(false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Obteniendo Datos");
        progressDialog.setMessage("Cargando el perfil");
        progressDialog.show();

        perfilController = new PerfilController(this);
        String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        perfilController.loadPerfilData(userUID);
        //button signOut succes
        etFechan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                String user = mAuth.getCurrentUser().getUid();
                Log.d("UID","user "+user);
                progressDialogsingOut = new ProgressDialog(getContext());
                progressDialogsingOut.setTitle("Cerrando Sesion...");
                progressDialogsingOut.setMessage("Su sesion se esta cerrando");
                progressDialogsingOut.show();
                mAuth.signOut();
                if(mAuth.getCurrentUser() == null){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialogsingOut.show();
                        }
                    },1000);
                }

            }
        });
        editarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gender = String.valueOf(etGenero.getText());
                String phyAct = String.valueOf(etActividadF.getText());
                Log.d("data","El genero es "+gender+"la actividad"+phyAct);
                perfilController.editarPerfil(gender,phyAct);
            }
        });
        guardarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perfilController.saveDataNew(userUID);
            }
        });
        btnExportarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(v);
            }
        });


        return vista;
    }
    private void checkPermission(View view){
        if (
                ContextCompat.checkSelfPermission(
                        main,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )  == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(main,"Permisos Concedidos",Toast.LENGTH_SHORT).show();
            perfilController.createPDF();
        }else if(ActivityCompat.shouldShowRequestPermissionRationale(
                main,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        )){
            Snackbar.make(view,"Este permiso es necesario para crear el archivo",Snackbar.LENGTH_INDEFINITE).setAction("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            });
        }else{
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }
    public void viewData(PerfilModel perfilModel){
        tvCorreo.setText(perfilModel.getCorre());
        tvNombre.setText(perfilModel.getName());
        etGenero.setText(perfilModel.getGender());
        etEstatura.setText(perfilModel.getHeight());
        etPesoA.setText(perfilModel.getWeight());
        etFechan.setText(perfilModel.getBirthDate());
        etActividadF.setText(perfilModel.getPhysical_activity());
        etPesoO.setText(perfilModel.getTargetWeight());
        if(progressDialog.isShowing()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            },500);
        }
    }
    public void openDialog(){
        final Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);
        int dia = d;
        int year = y-15;
        int yearmin = y-50;
        int mes = m;
        DatePickerDialog fechaNacido = new DatePickerDialog(main, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int y, int m, int d) {
                if(y<yearmin ){
                    String title = "Fecha invalida";
                    String message = "Ingrese una fecha valida";
                    dialogAlert(title,message);
                }else if(y>year || y>=year && d>dia && m>=mes){
                    String title = "Eres menor de edad";
                    String message = "Tienes que tener 15 años cumplidos";
                    dialogAlert(title,message);
                }else{
                    etFechan.setText(String.valueOf(d)+"/"+String.valueOf(m+1)+"/"+String.valueOf(y));
                }
            }
        }, y-15, m, d);
        fechaNacido.show();
    }
    private void dialogAlert (String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(main);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openDialog();
                    }
                });
        AlertDialog dialog = builder.create();
        builder.show();
    }
}