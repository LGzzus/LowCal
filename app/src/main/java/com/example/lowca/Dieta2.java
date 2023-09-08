package com.example.lowca;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lowca.Adaptadores.ListAdapterDietas;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Date;
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

    private ProgressDialog mDialog;

    //Cambiar de color
    private ListAdapterDietas mAdapter;
    private RecyclerView mRecyclerView;



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
        mAuth=FirebaseAuth.getInstance();
        mDialog= new ProgressDialog(this);

        db = FirebaseFirestore.getInstance();

        Bundle recibirDatos = getIntent().getExtras();
        if (recibirDatos != null) {
            iniciarFirebase();
            tipoDieta = recibirDatos.getString("tipo_dieta");
            calorias = recibirDatos.getString("calorias");
            numDieta=recibirDatos.getString("num_dieta");
            //String infoDieta = recibirDatos.getString("info_dieta");
            String desayuno=recibirDatos.getString("desayuno");
            String almuerzo=recibirDatos.getString("almuerzo");
            String cena=recibirDatos.getString("cena");
            idDieta=recibirDatos.getString("id_dieta");
            System.out.println("ID dieta: "+idDieta);

            // Ahora puedes usar estos valores para mostrarlos en la interfaz o realizar otras operaciones
            tvNombreDieta.setText(tvNombreDieta.getText()+tipoDieta);
            tvTotalCalorias.setText(tvTotalCalorias.getText()+calorias);
            tvDesayuno.setText(desayuno);
            tvAlmuerzo.setText(almuerzo);
            tvCena.setText(cena);
        } else {
            // Si no se recibieron datos, muestra un mensaje o realiza alguna acción
            System.out.println("Datos nulos");
        }


        /** Revisar el layout */



        btnAsignarDieta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Obtiene la hora actual
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


                DocumentReference userDocRef = db.collection("dieta_asignada").document(userUid);


                // Agregar el documento a la colección
                userDocRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // El documento existe, actualizar los campos
                            userDocRef.update(dieta_registrada)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("Firestore", "Documento actualizado correctamente");
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("Firestore", "Error al actualizar el documento", e);
                                    });
                        } else {
                            // El documento no existe, crearlo con los datos proporcionados
                            userDocRef.set(dieta_registrada)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("Firestore", "Documento creado correctamente");
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("Firestore", "Error al crear el documento", e);
                                    });
                        }
                    } else {
                        Log.e("Firestore", "Error al verificar el documento", task.getException());
                    }
                });




                Toast.makeText(Dieta2.this,"Dieta asignada",Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }
    private void iniciarFirebase(){
        mAuth=FirebaseAuth.getInstance();
        userUid = mAuth.getCurrentUser().getUid();
        db=FirebaseFirestore.getInstance();
    }

}