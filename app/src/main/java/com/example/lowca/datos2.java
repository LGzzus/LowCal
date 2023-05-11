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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    String nombre, correo, password,peso,estatura,nacido;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos2);

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

         nombre=datos1[0];
         correo=datos1[1];
         password=datos1[2];
        //System.out.println("Nombre: "+nombre);

        datosDos=recibirDatos2.getStringArray("keyDatos2");
         peso=datosDos[0];
         estatura=datosDos[1];
        nacido=datosDos[2];

        mAuth=FirebaseAuth.getInstance();
        //db=FirebaseFirestore.getInstance();
         db = FirebaseFirestore.getInstance();



    }

    public void cargarCuenta(View view){

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


        mAuth.createUserWithEmailAndPassword(correo,password).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Log.d(TAG,"createUserWithEmail:success");
                            FirebaseUser user =mAuth.getCurrentUser();


                        }
                        else {
                                // If sign in fails, display a message to the user.

                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(datos2.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                // updateUI(null);
                            }
                    }
                });

        db.collection("account")
                .add(account).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                });
        db.collection("user")
                .add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID:" + documentReference.getId());
                    }
                });
        db.collection("antropometric_dates")
                .add(antropometric_dates).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID:" + documentReference.getId());
                    }
                });
        /*System.out.println("Nombre: "+nombre+" Correo: "+correo+" Contraseña: "+contraseña);
        System.out.println("Peso: "+peso+" Estatura"+ estatura+" Nacido: "+nacido);*/

        Intent intent=new Intent(datos2.this, perfil.class);

        startActivity(intent);

    }

    public void atras(View view){
        this.finish();
    }


}