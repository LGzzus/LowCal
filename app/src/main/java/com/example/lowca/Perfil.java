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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;

import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Perfil#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class Perfil extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
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
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Perfil.
     */
    // TODO: Rename and change types and number of parameters
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
        guardarDatos.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        extraerDatos();
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