package com.example.lowca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class datos1 extends AppCompatActivity {
    EditText etPeso,etEstatura,etNacido;
    String[] datos1;
    String[] datosDos;
    Bundle recibirDatos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos1);
        etPeso=(EditText) findViewById(R.id.etPeso);
        etEstatura=(EditText)findViewById(R.id.etEstatura);
        etNacido=(EditText)findViewById(R.id.etNacido);

        /*Bundle recibirDatos= getIntent().getExtras();
        datos1=recibirDatos.getStringArray("keyDatos");*/
        recibirDatos= getIntent().getExtras();
    }
    public void datos2Pantalla(View v){


        datos1=recibirDatos.getStringArray("keyDatos");
        /*String nombre=datos1[0];
        String correo=datos1[1];
        String contraseña=datos1[2];
        System.out.println("Nombre: "+nombre+"Correo"+correo+"Contraseña"+contraseña);*/

        String peso=etPeso.getText().toString();
        String estatura=etEstatura.getText().toString();
        String nacido=etNacido.getText().toString();
        datosDos= new String[]{peso,estatura,nacido};
        Bundle pasarDatos= new Bundle();
        Bundle pasarDatos2= new Bundle();
        pasarDatos.putStringArray("keyDatos",datos1);
        pasarDatos2.putStringArray("keyDatos2",datosDos);

       Intent intent = new Intent(this,datos2.class);
        intent.putExtras(pasarDatos);
        intent.putExtras(pasarDatos2);
        startActivity(intent);
    }
    public void atras(View view){
        this.finish();
    }
}