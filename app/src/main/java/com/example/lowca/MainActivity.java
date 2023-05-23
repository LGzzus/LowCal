package com.example.lowca;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
Inicio inicio = new Inicio();
Coach coach = new Coach();
Agregar_mas add = new Agregar_mas();
Dieta dieta = new Dieta();
Perfil perfil = new Perfil();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView nav = findViewById(R.id.botom_navigacion);
        nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(inicio);




    }
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.Inicio:
                    loadFragment(inicio);
                    return true;
                case R.id.Coach:
                    loadFragment(coach);
                    return true;
                case R.id.Agmas:
                    loadFragment(add);
                    return true;
                case R.id.Dieta:
                    loadFragment(dieta);
                    return true;
                case R.id.Perfil:
                    loadFragment(perfil);
                    return true;
            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment){
        FragmentTransaction trancision = getSupportFragmentManager().beginTransaction();
        trancision.replace(R.id.frame_container,fragment);
        trancision.commit();
    }



}