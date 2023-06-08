package com.example.lowca;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class datos2 extends AppCompatActivity {
    TextInputEditText etPesoObjetivo;
    TextInputLayout tilPesoObjetivo;
    Spinner spinnerDatos;
    RadioButton rbHombre,rbMujer;
    String[] datos1; String[]datosDos;
    Bundle recibirDatos1,recibirDatos2;
    String nombre, correo, password,peso,estatura,nacido,userUid;

    Button btnSiguiente;

    int pesoActual,pesoObjetivo2, estatura2;
    public FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos2);
        FirebaseApp.initializeApp(this);
        etPesoObjetivo=(TextInputEditText) findViewById(R.id.etPesoObjetivo);
        tilPesoObjetivo = (TextInputLayout) findViewById(R.id.TILPesoO);
        rbHombre=(RadioButton) findViewById(R.id.radioBtnHombre);
        rbMujer=(RadioButton) findViewById(R.id.radioBtnMujer);
        spinnerDatos=(Spinner) findViewById(R.id.spinnerDatos);
        btnSiguiente = (Button) findViewById(R.id.btnSiguiente);

        //El peso objetivo solo acepta dos decimas
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
        etPesoObjetivo.setFilters(new InputFilter[]{decimalFilter});

        String[] opciones={"Sedentaria","Activa"};
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,
                opciones);
        spinnerDatos.setAdapter(adapter);
        recibirDatos1=getIntent().getExtras();
        recibirDatos2=getIntent().getExtras();
        datos1=recibirDatos1.getStringArray("keyDatos");
        btnSiguiente.setOnClickListener(v -> {
            try {
                if(cargarCuenta()){
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

                    //cabiar datos a int
                    pesoActual = Integer.parseInt(peso);
                    estatura2 = Integer.parseInt(estatura);

                    Map<String,Object>user=new HashMap<>();
                    Map<String,Object>account=new HashMap<>();
                    Map<String,Object>antropometric_dates=new HashMap<>();
                    user.put("name",nombre);
                    account.put("mail",correo);
                    account.put("password",password);

                    //String nivelAcF;
                    String genero;
                    String pesoObjetivo=etPesoObjetivo.getText().toString();
                    pesoObjetivo2 = Integer.parseInt(pesoObjetivo);
                    String nivelAcF= spinnerDatos.getSelectedItem().toString();
                    double caloriasBasales=0;

                    antropometric_dates.put("weight", pesoActual);
                    antropometric_dates.put("height",estatura2);
                    antropometric_dates.put("target_weight",pesoObjetivo2);
                    antropometric_dates.put("birth_date", nacido);
                    antropometric_dates.put("physical_activity_lever",nivelAcF);
                    antropometric_dates.put("calculated_calories",caloriasBasales );


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

                    Intent intent=new Intent(datos2.this, MainActivity.class);

                    startActivity(intent);
                }else{
                }
            }catch (Exception e){

            }
        });

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


    public boolean cargarCuenta(){
        boolean retorno = true;
        String pesoObjetivotxt = etPesoObjetivo.getText().toString();
        if (pesoObjetivotxt.isEmpty()){
            tilPesoObjetivo.setError("Ingrese su peso Objetivo");
            retorno = false;
        }else{
            tilPesoObjetivo.setErrorEnabled(false);
        }
        return retorno;
    }

    public void atras(View view){
        this.finish();
    }
}
