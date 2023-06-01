package com.example.lowca;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lowca.model.Cuenta;
import com.example.lowca.model.DatosAnt;
import com.example.lowca.model.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;

import org.checkerframework.checker.units.qual.C;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class Perfil extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    View vista;
    TextView tvNombre, tvCorreo, tvPesoO;
    EditText etGenero, etPesoA, etEstatura, etActividadF, etFechan;
    Button cerrarSesion, editarDatos, guardarDatos;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    public String userUid;
    Activity main;
    public String NombreD, CorreoD, FechaND, EstaturaD, PesoAD, PesoOD, GeneroD, ActividadD;

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
        extraerDatos();
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
        guardarDatos.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


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
                etFechan.setEnabled(true);
                etGenero.setEnabled(true);
                etEstatura.setEnabled(true);
                etPesoA.setEnabled(true);
                etActividadF.setEnabled(true);
            }
        });
        guardarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatosAnt datosA = new DatosAnt();
                datosA.setFechaN(etFechan.getText().toString());
                datosA.setGenero(etGenero.getText().toString());
                datosA.setGenero(etGenero.getText().toString());
                datosA.setEstatura(Integer.parseInt(etEstatura.getText().toString()));
                datosA.setPesoA(Integer.parseInt(etPesoA.getText().toString()));
                datosA.setActividadF(etActividadF.getText().toString());

                Map<String, Object> dat = new HashMap<>();
                dat.put("birth_date",datosA.getFechaN());
                dat.put("gender", datosA.getGenero());
                dat.put("height", datosA.getEstatura());
                dat.put("weight",datosA.getPesoA());

                db.collection("antropometric_dates").document(userUid).update(dat);
                Toast.makeText(main,"Actualizando",Toast.LENGTH_LONG).show();
                editarDatos.setVisibility(View.VISIBLE);
                guardarDatos.setVisibility(View.INVISIBLE);
                etFechan.setEnabled(false);
                etGenero.setEnabled(false);
                etEstatura.setEnabled(false);
                etPesoA.setEnabled(false);
                etActividadF.setEnabled(false);

            }
        });

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
                    PesoAD = String.valueOf(value.getLong("weight"));
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
}