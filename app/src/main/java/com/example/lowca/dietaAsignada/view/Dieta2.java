package com.example.lowca.dietaAsignada.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lowca.Inicio;
import com.example.lowca.R;
import com.example.lowca.dieta.controller.DietaController;
import com.example.lowca.dieta.modelo.ListDietas;
import com.example.lowca.dieta.view.Dieta;
import com.example.lowca.dieta.view.ListAdapterDietas;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Dieta2 extends AppCompatActivity {
     //Bundle recibirDatos;
     String [] datosRecibidos;
    private TextView tvNombreDieta,tvTotalCalorias,tvDesayuno,tvAlmuerzo,tvCena;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    String userUid,tipoDieta,idDieta,calorias,numDieta;
    Button btnAsignarDieta;
    ImageButton btnRegresarDieta;

    private ProgressDialog mDialog;

    //Cambiar de color
    private ListAdapterDietas mAdapter;
    private RecyclerView mRecyclerView;
    public Dieta dieta;

    public ListDietas dietasModel;
    public DietaController dietaController;

    public String dietaAComparar;

    private ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dieta2);

        tvNombreDieta=(TextView) findViewById(R.id.tvNombreDieta);
        tvTotalCalorias=(TextView) findViewById(R.id.tvTotalCalorias);
        tvDesayuno=(TextView)findViewById(R.id.tvDesayuno);
        tvAlmuerzo=(TextView)findViewById(R.id.tvAlmuerzo);
        tvCena=(TextView)findViewById(R.id.tvCena);
        btnAsignarDieta=(Button) findViewById(R.id.btnAsignarDieta);
        btnRegresarDieta=(ImageButton)findViewById(R.id.btnRegresarDieta);
        mAuth=FirebaseAuth.getInstance();
        mDialog= new ProgressDialog(this);
        //dietaController.obtenerDietaAsiganada(userUid);

        db = FirebaseFirestore.getInstance();

        Bundle recibirDatos = getIntent().getExtras();
        if (recibirDatos != null) {
            iniciarFirebase();

            tipoDieta = recibirDatos.getString("tipo_dieta");
            calorias = recibirDatos.getString("calorias");
            numDieta=recibirDatos.getString("num_dieta");
            String desayuno=recibirDatos.getString("desayuno");
            String almuerzo=recibirDatos.getString("almuerzo");
            String cena=recibirDatos.getString("cena");

            idDieta=recibirDatos.getString("id_dieta");
            System.out.println("ID dieta: "+idDieta);


            tvNombreDieta.setText("Tipo: "+tipoDieta);
            tvTotalCalorias.setText("Calorias: "+calorias);
            tvDesayuno.setText(desayuno);
            tvAlmuerzo.setText(almuerzo);
            tvCena.setText(cena);

        } else {

            System.out.println("Datos nulos");
        }


    }

    public void verificarDietaAsiganada(View view ) {
        Calendar calendar = Calendar.getInstance();
        java.util.Date horaActual =  calendar.getTime();

        SimpleDateFormat formatoFecha2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String horaActualFormateada = formatoFecha2.format(horaActual);
        Map<String, Object> dieta_registrada = new HashMap<>();
        dieta_registrada.put("tipo", tipoDieta);
        dieta_registrada.put("id_dieta", idDieta);
        dieta_registrada.put("calorias",calorias);
        dieta_registrada.put("registro",horaActualFormateada);
        dieta_registrada.put("num_dieta",numDieta);


        progressDialog = new ProgressDialog(Dieta2.this);
        progressDialog.setTitle("Asignando Dieta");
        progressDialog.setMessage("Cargando dietas");

        DocumentReference userDocRef = db.collection("dieta_asignada").document(userUid);

        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String num_dieta = document.getString("num_dieta");
                        String idD= document.getString("id_dieta");


                        if (idDieta.equals(idD)){
                            Toast.makeText(Dieta2.this, "Dieta ya asignada",Toast.LENGTH_LONG).show();

                        }else  {

                            userDocRef.update(dieta_registrada)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("Firestore", "Documento actualizado correctamente");
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("Firestore", "Error al actualizar el documento", e);
                                    });
                            progressDialog.show();
                        }

                        Log.d("IdRecibido: ",""+idDieta);
                        Log.d("IdRecuperado: ", ""+idD);

                    } else {
                        // El documento no existe

                            userDocRef.set(dieta_registrada)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("Firestore", "Documento creado correctamente");
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("Firestore", "Error al crear el documento", e);
                                    });
                            progressDialog.show();
                        Log.d("Firestore", "El documento no existe");
                    }



                    if(progressDialog.isShowing()){
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(Dieta2.this,"Dieta asignada",Toast.LENGTH_LONG).show();
                            }
                        },500);
                    }
                } else {
                    // Error al obtener el documento
                    Log.e("Firestore", "Error al obtener el documento", task.getException());
                }
            }
        });
    }






    public void regresar(View view){
        this.finish();
    }
    private void iniciarFirebase(){
        mAuth=FirebaseAuth.getInstance();
        userUid = mAuth.getCurrentUser().getUid();
        db=FirebaseFirestore.getInstance();
    }


}