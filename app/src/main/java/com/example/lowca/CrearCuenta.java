package com.example.lowca;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CrearCuenta extends AppCompatActivity {
    TextInputEditText etCorreo,etPassword, etNombre;
    TextInputLayout tlNombre,tlCorreo, tlContraseña;
    Button btnEntrar;
    String[] datos;
    public FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);
        etNombre= (TextInputEditText) findViewById(R.id.etNombre);
        etCorreo=(TextInputEditText) findViewById(R.id.etCorreo);
        etPassword=(TextInputEditText) findViewById(R.id.etPassword);
        tlNombre = (TextInputLayout) findViewById(R.id.TIL1);
        tlCorreo = (TextInputLayout) findViewById(R.id.TIL2);
        tlContraseña = (TextInputLayout) findViewById(R.id.TIl3);
        btnEntrar = (Button) findViewById(R.id.btnEntrar);


        mAuth=FirebaseAuth.getInstance();
        btnEntrar.setOnClickListener(v -> {
            try {
                if(validar()){
                    String nombre=etNombre.getText().toString();
                    String correo=etCorreo.getText().toString();
                    String contraseña=etPassword.getText().toString();
                    datos= new String[]{nombre,correo,contraseña};
                    mAuth.createUserWithEmailAndPassword(correo,contraseña).
                            addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Log.d(TAG,"createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                    }
                                    else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(CrearCuenta.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                        // updateUI(null);
                                    }
                                }
                            });
                    Bundle pasarDatos = new Bundle();
                    pasarDatos.putStringArray("keyDatos", datos);
                    Intent intent = new Intent(this, datos1.class);
                    intent.putExtras(pasarDatos);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this,"LLene Todo",Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
            }
        });
        etNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tlNombre.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etCorreo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                tlCorreo.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tlContraseña.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public boolean validar(){
        boolean retorno = true;
        String nombre=etNombre.getText().toString();
        String correo=etCorreo.getText().toString();
        String contraseña=etPassword.getText().toString();
        if(nombre.isEmpty() && correo.isEmpty() && contraseña.isEmpty()){
                tlNombre.setError("Escribe tu nombre");
                tlCorreo.setError("Escribe tu correo");
                tlContraseña.setError("Escribe tu contraseña");
        }
        if(nombre.isEmpty()){
                tlNombre.setError("Escribe tu nombre");
        }else {
            tlNombre.setErrorEnabled(false);
        }
        if (correo.isEmpty() || !correo.contains("@")) {
            tlCorreo.setError("Escribe tu correo");
        }else{
            tlCorreo.setErrorEnabled(false);
        }
        if (contraseña.isEmpty() || contraseña.length() < 6) {
            tlContraseña.setError("Escribe tu contraseña");
        }else{
            tlContraseña.setErrorEnabled(false);
        }
        return retorno;
    }
    public void atras(View view){
        this.finish();
    }
}