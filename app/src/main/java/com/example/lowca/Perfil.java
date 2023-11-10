package com.example.lowca;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lowca.model.DatosAnt;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Perfil extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    View vista;
    TextView tvNombre, tvCorreo, tvPesoO;
    EditText etGenero, etPesoA, etEstatura, etActividadF, etFechan;
    Button cerrarSesion, editarDatos, guardarDatos,btnExportarInfo;
    Spinner spinnerGen, spinnerAct;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    public String userUid;
    Activity main;
    public String NombreD, CorreoD, FechaND, EstaturaD, PesoAD, PesoOD, GeneroD, ActividadD;
    DatosAnt datosA = new DatosAnt();

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
        tvPesoO = (TextView) vista.findViewById(R.id.tvPesoO);
        cerrarSesion = (Button) vista.findViewById(R.id.cerrar_sesion);
        editarDatos = (Button) vista.findViewById(R.id.editar);
        guardarDatos = (Button) vista.findViewById(R.id.guardar);
        spinnerGen = (Spinner) vista.findViewById(R.id.spinnerGenero);
        spinnerAct = (Spinner) vista.findViewById(R.id.spinnerActividad);
        guardarDatos.setVisibility(View.INVISIBLE);
        spinnerGen.setVisibility(View.INVISIBLE);
        spinnerAct.setVisibility(View.INVISIBLE);
        String[] generos={"Seleccione su genero","Mujer","Hombre"};

        btnExportarInfo=vista.findViewById(R.id.btnExportarInfo);
        
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(main,android.R.layout.simple_spinner_item,
                generos);
        spinnerGen.setAdapter(adapter);
        String[] opciones={"Seleccione la actividad fisica","Sedentaria","Activa"};
        ArrayAdapter<String> adapter2= new ArrayAdapter<String>(main,android.R.layout.simple_spinner_item,
                opciones);
        spinnerAct.setAdapter(adapter2);
        extraerDatos();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        //Establece que la alturo solo debe ser max de 3 cifras
        InputFilter lengthFilter = new InputFilter.LengthFilter(3);
        etEstatura.setFilters(new InputFilter[]{lengthFilter});
        // Crea un InputFilter para limitar los decimales
        InputFilter decimalFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String inputText = dest.toString();
                String newText = inputText.substring(0, dstart) + source.subSequence(start, end) + inputText.substring(dend);

                // Verifica si el nuevo texto cumple con el formato de dos decimales después del punto
                if (!newText.matches("^\\d+(\\.\\d{0,2})?$")) {
                    return "";
                }

                return null; // Deja pasar el texto ingresado
            }
        };



// Agrega el InputFilter al TextInput
        etPesoA.setFilters(new InputFilter[]{decimalFilter});

        etFechan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(getContext(), "Cerrando sesion", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(),IniciarSesion.class);
                startActivity(i);
                getActivity().finish();
            }
        });
        editarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarDatos.setVisibility(View.INVISIBLE);
                guardarDatos.setVisibility(View.VISIBLE);
                etGenero.setVisibility(View.INVISIBLE);
                spinnerGen.setVisibility(View.VISIBLE);
                etActividadF.setVisibility(View.INVISIBLE);
                spinnerAct.setVisibility(View.VISIBLE);
                etFechan.setEnabled(true);
                etEstatura.setEnabled(true);
                etPesoA.setEnabled(true);
            }
        });
        guardarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(validar()){
                        datosA.setFechaN(etFechan.getText().toString());
                        datosA.setGenero(spinnerGen.getSelectedItem().toString());
                        datosA.setEstatura(Integer.parseInt(etEstatura.getText().toString()));
                        Float pesoA = Float.parseFloat(etPesoA.getText().toString());
                        datosA.setActividadF(spinnerAct.getSelectedItem().toString());
                        Map<String, Object> dat = new HashMap<>();
                        dat.put("birth_date",datosA.getFechaN());
                        dat.put("gender", datosA.getGenero());
                        dat.put("height", datosA.getEstatura());
                        dat.put("weight",pesoA);
                        dat.put("physical_activity_lever",datosA.getActividadF());

                        db.collection("antropometric_dates").document(userUid).update(dat);
                        Toast.makeText(main,"Actualizando",Toast.LENGTH_LONG).show();
                        editarDatos.setVisibility(View.VISIBLE);
                        guardarDatos.setVisibility(View.INVISIBLE);
                        etGenero.setVisibility(View.VISIBLE);
                        spinnerGen.setVisibility(View.INVISIBLE);
                        etActividadF.setVisibility(View.VISIBLE);
                        spinnerAct.setVisibility(View.INVISIBLE);
                        etFechan.setEnabled(false);
                        etGenero.setEnabled(false);
                        etEstatura.setEnabled(false);
                        etPesoA.setEnabled(false);
                        etActividadF.setEnabled(false);
                    }
                }catch (Exception e){

                }
            }
        });
        //Exportar informacion




        //extrae los datos para actualizarlos

        return vista;
    }





    private void extraerDatos(){
        try {
            userUid = mAuth.getCurrentUser().getUid();
            DocumentReference refA = db.collection("account").document(userUid);
            DocumentReference refU = db.collection("user").document(userUid);
            DocumentReference refD = db.collection("antropometric_dates").document(userUid);
            refU.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    NombreD = value.getString("name");
                    tvNombre.setText(NombreD);
                }
            });
            refA.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    CorreoD = value.getString("mail");
                    tvCorreo.setText(CorreoD);
                }
            });

            refD.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    FechaND = value.getString("birth_date");
                    GeneroD = value.getString("gender");
                    EstaturaD = String.valueOf(value.getLong("height"));
                    PesoAD = String.valueOf(value.getLong("weight").floatValue());
                    PesoOD = String.valueOf(value.getLong("target_weight"));
                    ActividadD = value.getString("physical_activity_lever");
                    etFechan.setText(FechaND);
                    etGenero.setText(GeneroD);
                    etEstatura.setText(EstaturaD);
                    etPesoA.setText(PesoAD);
                    tvPesoO.setText(PesoOD);
                    etActividadF.setText(ActividadD);
                }
            });
            etFechan.setEnabled(false);
            etGenero.setEnabled(false);
            etEstatura.setEnabled(false);
            etPesoA.setEnabled(false);
            etActividadF.setEnabled(false);


        }catch (Exception e){
            System.out.println(e);
        }
    }
    public void openDialog(){
        DatePickerDialog fechaNacido = new DatePickerDialog(main, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                etFechan.setText(String.valueOf(day)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year));
            }
        }, 2023, 06, 03);
        fechaNacido.show();
    }
    public boolean validar(){
        boolean retorno = true;
        String FechaNac = datosA.getFechaN();
        String Genero = datosA.getGenero();
        String Estatura = etEstatura.getText().toString();
        String PesoAc = etPesoA.getText().toString();
        String ActividadFis = datosA.getActividadF();
        /*if(etFechan.getText()==null){
            etFechan.setError("Llena el campo");
            retorno = false;
        }*/
        if (spinnerGen.getSelectedItemPosition()==0){
            Toast.makeText(main,"Seleccione una opcion",Toast.LENGTH_LONG).show();
            retorno = false;
        }
        if (Estatura.isEmpty()){
            etEstatura.setError("Ingresa tu estatura correcta");
            retorno = false;
        }
        if (PesoAc.isEmpty()){
            etPesoA.setError("Ingresa tu Peso correcto");
            retorno = false;
        }
        if(spinnerAct.getSelectedItemPosition()==0){
            Toast.makeText(main,"Selecciona la opcion correcta",Toast.LENGTH_LONG).show();
            retorno = false;
        }
        return retorno;
    }
}