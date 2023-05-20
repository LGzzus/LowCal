package com.example.lowca;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class IniciarSesion extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText etEmail, etPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
        etEmail = (EditText) findViewById(R.id.editTextTextEmailAddress);

        etPass = (EditText) findViewById(R.id.editTextTextPassword);

        mAuth = FirebaseAuth.getInstance();


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
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void red(View v){

        String email = etEmail.getText().toString();
        String password = etPass.getText().toString();

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Ingrese sus datos",Toast.LENGTH_LONG).show();
        }else {
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

        }





        /*Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);*/
    }
    public void recuperarPassword(View view){
        Intent intent= new Intent(this, RecuperarPassword1.class);
        startActivity(intent);
    }
}