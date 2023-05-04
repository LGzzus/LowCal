package com.example.lowca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class datos1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos1);
    }
    public void datos2Pantalla(View v){
        Intent intent = new Intent(this,datos2.class);
        startActivity(intent);
    }
}