package com.example.lowca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CrearCuenta extends AppCompatActivity {
    EditText etCorreo,etPassword, etNombre;
    String[] datos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);
        etNombre=(EditText) findViewById(R.id.etNombre);
        etCorreo=(EditText) findViewById(R.id.etCorreo);
        etPassword=(EditText) findViewById(R.id.etPassword);

    }
    public void datosPantalla(View v){
        String nombre=etNombre.getText().toString();
        String correo=etCorreo.getText().toString();
        String contraseña=etPassword.getText().toString();
        datos= new String[]{nombre,correo,contraseña};
      if(nombre.isEmpty()){
          Toast.makeText(this,"Ingrese nombre",Toast.LENGTH_LONG).show();
      } else
        if (correo.isEmpty() || !correo.contains("@")) {
            Toast.makeText(this,"Correo no valido",Toast.LENGTH_LONG).show();
        }else
        if (contraseña.isEmpty() || contraseña.length() < 6) {

            Toast.makeText(this,"Contraseña minima de 6 caracteres",Toast.LENGTH_LONG).show();
        } else {
            Bundle pasarDatos = new Bundle();
            pasarDatos.putStringArray("keyDatos", datos);
            Intent intent = new Intent(this, datos1.class);
            intent.putExtras(pasarDatos);
            startActivity(intent);
        }
    }
    public void atras(View view){
        this.finish();
    }
}