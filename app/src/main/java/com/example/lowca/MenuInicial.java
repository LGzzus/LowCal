package com.example.lowca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MenuInicial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicial);
    }
    public void iniciaSesion (View v){
        Intent intent = new Intent(this,IniciarSesion.class);
        startActivity(intent);
    }
    public void crearCuenta (View v){
        Intent intent = new Intent(this,CrearCuenta.class);
        startActivity(intent);
    }
}