package com.example.lowca;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class IniciarSesion extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextInputEditText etCorreo, etPassword;
    TextInputLayout tlCorreo, tlContraseña;
    boolean logueado;

    Button Ingresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
        etCorreo = (TextInputEditText) findViewById(R.id.etCorreo);

        etPassword = (TextInputEditText) findViewById(R.id.etPassword);

        tlCorreo = (TextInputLayout) findViewById(R.id.TIL2);
        tlContraseña = (TextInputLayout) findViewById(R.id.TIl3);
        Ingresa = (Button) findViewById(R.id.btnIngresar);
        mAuth = FirebaseAuth.getInstance();

        Ingresa.setOnClickListener(v -> {
            try {
                if(validar()){
                    String email = etCorreo.getText().toString();
                    String password = etPassword.getText().toString();
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        //updateUI(user);
                                }
                            }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    finish();
                                    Intent i = new Intent(IniciarSesion.this, MainActivity.class);
                                    startActivity(i);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialogAlert();
                                }
                            });
                }

            }catch (Exception e){

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

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }
    public void atras(View v){
        Intent intent=new Intent(this,MenuInicial.class);
        startActivity(intent);
    }

   /* public void red(View v){

        String email = etCorreo.getText().toString();
        String password = etPassword.getText().toString();

            Toast.makeText(this,"Ingrese sus datos",Toast.LENGTH_LONG).show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                //updateUI(user);
                                Intent i = new Intent(IniciarSesion.this, MainActivity.class);
                                startActivity(i);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(IniciarSesion.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    });
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }*/
    public void recuperarPassword(View view){
        Intent intent= new Intent(this, RecuperarPassword1.class);
        startActivity(intent);
    }
    public boolean validar(){
        boolean retorno = true;
        String correo=etCorreo.getText().toString();
        String contraseña=etPassword.getText().toString();
        if (correo.isEmpty()){
            tlCorreo.setError("Escribe tu correo");
            retorno =false;
        }else{
            if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                tlCorreo.setError("Correo Incorrecto, Verifiquelo");
                retorno =false;
            }else{
                tlCorreo.setErrorEnabled(false);
            }
        }
        if (contraseña.isEmpty()) {
            tlContraseña.setError("Escribe tu contraseña correctamente");
            retorno =false;
        }else {
            if (contraseña.length() < 6) {
                tlContraseña.setError("Tu contraseña debe tener al menos 6 caracteres");
                retorno = false;
            } else {
                tlContraseña.setErrorEnabled(false);
            }
        }

        return retorno;
    }
    private void dialogAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Iniciar Sesion")
                .setMessage("Su correo electronico o contraseña son incorrectos, intente nuevamente.")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        builder.show();
    }
}