package com.example.lowca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class perfil extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);

    }
    public void atras(View view){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
