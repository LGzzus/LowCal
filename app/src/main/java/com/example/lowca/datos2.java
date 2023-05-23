package com.example.lowca;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class datos2 extends AppCompatActivity {
    EditText etPesoObjetivo;
    Spinner spinnerDatos;
    RadioButton rbHombre,rbMujer;
    String[] datos1; String[]datosDos; //,datos3;
    Bundle recibirDatos1,recibirDatos2;
    String nombre, correo, password,peso,estatura,nacido,userUid;
    public FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos2);
        FirebaseApp.initializeApp(this);
        etPesoObjetivo=(EditText) findViewById(R.id.etPesoObjetivo);
        rbHombre=(RadioButton) findViewById(R.id.radioBtnHombre);
        rbMujer=(RadioButton) findViewById(R.id.radioBtnMujer);
        spinnerDatos=(Spinner) findViewById(R.id.spinnerDatos);
        String[] opciones={"Leve","Moderada","Energica"};
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,
                opciones);
        spinnerDatos.setAdapter(adapter);
        recibirDatos1=getIntent().getExtras();
        recibirDatos2=getIntent().getExtras();
        datos1=recibirDatos1.getStringArray("keyDatos");


        datosDos=recibirDatos2.getStringArray("keyDatos2");


        mAuth=FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        onStart();



    }
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }
    }

    public void cargarCuenta(View view){
        userUid = mAuth.getCurrentUser().getUid();
        System.out.println(userUid);
        //Cuenta
        nombre=datos1[0];
        correo=datos1[1];
        password=datos1[2];
        //Datos 1
        peso=datosDos[0];
        estatura=datosDos[1];
        nacido=datosDos[2];
        Map<String,Object>user=new HashMap<>();
        Map<String,Object>account=new HashMap<>();
        Map<String,Object>antropometric_dates=new HashMap<>();
        user.put("name",nombre);
        account.put("mail",correo);
        account.put("password",password);

        //String nivelAcF;
        String genero;
        String pesoObjetivo=etPesoObjetivo.getText().toString();
        String nivelAcF= spinnerDatos.getSelectedItem().toString();

        antropometric_dates.put("weight", peso);
        antropometric_dates.put("height",estatura);
        antropometric_dates.put("target_weight",pesoObjetivo);
        antropometric_dates.put("birth_date", nacido);
        antropometric_dates.put("physical_activity_lever",nivelAcF);


        if(rbMujer.isChecked()){
            genero="Mujer";
            antropometric_dates.put("gender",genero);
        } else if(rbHombre.isChecked()){
            genero="Hombre";
            antropometric_dates.put("gender",genero);
        }else {
            Toast.makeText(this,"Seleccione sus datos",Toast.LENGTH_LONG).show();
        }



        db.collection("account").document(userUid)
                .set(account).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot written with ID: ");
                    }
                });
        db.collection("user").document(userUid)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot written with ID:");
                    }
                });
        db.collection("antropometric_dates").document(userUid)
                .set(antropometric_dates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot written with ID:");
                    }
                });
        /*System.out.println("Nombre: "+nombre+" Correo: "+correo+" Contraseña: "+contraseña);
        System.out.println("Peso: "+peso+" Estatura"+ estatura+" Nacido: "+nacido);*/

        Intent intent=new Intent(datos2.this, MainActivity.class);

        startActivity(intent);

    }

    public void atras(View view){
        this.finish();
    }


}
