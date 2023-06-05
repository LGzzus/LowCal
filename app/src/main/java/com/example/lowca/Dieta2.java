package com.example.lowca;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Dieta2 extends AppCompatActivity {
     Bundle recibirDatos;
     String [] datosRecibidos;
    private TextView tvNombreDieta,tvTotalCalorias,tvInfoDieta;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    String userUid;
    Button btnAsignarDieta;
    Boolean estadoBoton;
    private ProgressDialog mDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dieta2);

        tvNombreDieta=(TextView) findViewById(R.id.tvNombreDieta);
        tvTotalCalorias=(TextView) findViewById(R.id.tvTotalCalorias);
        tvInfoDieta=(TextView)findViewById(R.id.tvInfoDieta);
        btnAsignarDieta=(Button) findViewById(R.id.btnAsignarDieta);
        String calorias;

        recibirDatos= getIntent().getExtras();
        mAuth=FirebaseAuth.getInstance();
        mDialog= new ProgressDialog(this);

        db = FirebaseFirestore.getInstance();
        estadoBoton=true;


        String infoDieta;
        if(recibirDatos!=null) {
            datosRecibidos = recibirDatos.getStringArray("keyDatos");

        }else{
            Toast.makeText(this,"Bundle nulo",Toast.LENGTH_LONG).show();
        }

        tvNombreDieta.setText(tvNombreDieta.getText().toString()+ datosRecibidos[0]);
        tvTotalCalorias.setText(tvTotalCalorias.getText().toString()+datosRecibidos[1]);

        infoDieta=datosRecibidos[2];
        if(infoDieta.equals("dieta1")){
            String string=getString(R.string.dieta1);
            tvInfoDieta.setText(string);
            //Convertir las calorias a entero
            calorias=datosRecibidos[1];
            int numCalorias=Integer.valueOf(calorias);
            System.out.println("\n********CALORIAS:  "+numCalorias+"*************");
        }else if (infoDieta.equals("dieta2")){

            String string=getString(R.string.dieta2);
            tvInfoDieta.setText(string);
            //Convertir las calorias a entero
            calorias=datosRecibidos[1];
            int numCalorias=Integer.valueOf(calorias);
            System.out.println("\n********CALORIAS:  "+numCalorias+"*************");
        }else if(infoDieta.equals("dieta3")){
            String string=getString(R.string.dieta3);
            tvInfoDieta.setText(string);
            //Convertir las calorias a entero
            calorias=datosRecibidos[1];
            int numCalorias=Integer.valueOf(calorias);
            System.out.println("\n********CALORIAS:  "+numCalorias+"*************");
        }else{
            Toast.makeText(this,"No hay información",Toast.LENGTH_LONG).show();

        }


        btnAsignarDieta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userUid = mAuth.getCurrentUser().getUid();
                CollectionReference parentCollectionRef=db.collection("account");
                DocumentReference documentRef= parentCollectionRef.document(userUid);
                CollectionReference subCollectionRef = documentRef.collection("dieta");
                Map<String, Object> tipo_dieta = new HashMap<>();
                Map<String, Object> calorias_usuario = new HashMap<>();
                tipo_dieta.put("tipo_dieta",datosRecibidos[0]);
                tipo_dieta.put("calorias_usuario",datosRecibidos[1]);

                subCollectionRef.add(tipo_dieta)
                        .addOnSuccessListener(documentReference -> {
                            // Documento agregado exitosamente a la subcolección
                            mDialog.setMessage("Asignado dieta...");
                            mDialog.setCanceledOnTouchOutside(false);
                            mDialog.show();
                            Toast.makeText(Dieta2.this,"Dieta asignada",Toast.LENGTH_LONG).show();
                            mDialog.dismiss();

                        })
                        .addOnFailureListener(e -> {
                            // Error al agregar el documento a la subcolección

                        });
                //btnAsignarDieta.setEnabled(false);


            }

        });

    }

}