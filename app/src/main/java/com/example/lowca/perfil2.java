package com.example.lowca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class perfil2 extends AppCompatActivity {
    private FirebaseAuth mAuth;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();

    }
    public void atras(View view){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    public void cerrarSesion(View view){
        mAuth.signOut();
        Toast.makeText(perfil2.this,"Cerrando sesion",Toast.LENGTH_LONG).show();
        startActivity(new Intent(perfil2.this,MenuInicial.class));
        perfil2.this.finish();

    }
}
