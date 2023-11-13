package com.example.lowca;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.lowca.dieta.view.Dieta;
import com.example.lowca.perfil.View.Perfil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
Inicio inicio = new Inicio();
Coach coach = new Coach();
Agregar_mas add = new Agregar_mas();
Dieta dieta = new Dieta();
Perfil perfil = new Perfil();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView nav = findViewById(R.id.botom_navigacion);
        nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(inicio);
        //CollectionReference exercises_dbCollection = db.collection("excercises_db");
        /*
        CollectionReference foodCollection = db.collection("food");

        agregarAlimento(foodCollection, "Puñado de almendras (10 almendras)", 75);
        agregarAlimento(foodCollection, "Ensalada de pollo a la parrilla con vegetales mixtos y vinagreta baja en grasa", 250);
        agregarAlimento(foodCollection, "Sopa de verduras casera con una porción de pollo o pescado a la plancha", 250);
        agregarAlimento(foodCollection, "Palitos de zanahoria y apio con hummus", 130);
        agregarAlimento(foodCollection, "Yogur griego bajo en grasa con semillas de chía", 150);
        agregarAlimento(foodCollection, "Pechuga de pollo al horno con brócoli al vapor y media taza de arroz integral", 350);
        agregarAlimento(foodCollection, "Rodajas de pepino con salsa de yogur bajo en grasa y hierbas", 70);
        agregarAlimento(foodCollection, "Una porción de gelatina sin azúcar", 15);
        agregarAlimento(foodCollection, "Tortilla de claras de huevo con espinacas y queso bajo en grasa", 200);
        agregarAlimento(foodCollection, "Pechuga de pollo a la parrilla con espárragos y aguacate", 350);
        agregarAlimento(foodCollection, "Salmón al horno con brócoli al vapor y ensalada verde", 400);
        agregarAlimento(foodCollection, "Rodajas de pepino con salsa de yogur bajo en grasa", 100);
        agregarAlimento(foodCollection, "Batido de proteínas vegetales con espinacas, plátano y leche de almendras", 300);
        agregarAlimento(foodCollection, "Ensalada de garbanzos con vegetales, aceite de oliva y limón", 400);
        agregarAlimento(foodCollection, "Manzana con mantequilla de almendras", 200);
        agregarAlimento(foodCollection, "Tofu salteado con vegetales y arroz integral", 500);
        agregarAlimento(foodCollection, "Palitos de apio con hummus", 200);
        agregarAlimento(foodCollection, "Manzana", 52);
        agregarAlimento(foodCollection, "Piña", 55);
        agregarAlimento(foodCollection, "Albaricoque", 43);
        agregarAlimento(foodCollection, "Pera", 55);
        agregarAlimento(foodCollection, "Plátano", 88);
        agregarAlimento(foodCollection, "Arándanos", 35);
        agregarAlimento(foodCollection, "Naranja sanguina", 45);
        agregarAlimento(foodCollection, "Moras", 43);
        agregarAlimento(foodCollection, "Arándanos rojos", 46);
        agregarAlimento(foodCollection, "Fresas", 32);
        agregarAlimento(foodCollection, "Higo", 107);
        agregarAlimento(foodCollection, "Pomelo", 50);
        agregarAlimento(foodCollection, "Granada", 74);
        agregarAlimento(foodCollection, "Escaramujo", 162);
        agregarAlimento(foodCollection, "Melón", 54);
        agregarAlimento(foodCollection, "Frambuesas", 36);
        agregarAlimento(foodCollection, "Jengibre", 80);
        agregarAlimento(foodCollection, "Kiwi", 51);
        agregarAlimento(foodCollection, "Cerezas", 50);
        */
        /*
        agregarEjercicio(exercises_dbCollection, "Caminata ligera", 3);
        agregarEjercicio(exercises_dbCollection, "Caminata rápida", 6);
        agregarEjercicio(exercises_dbCollection, "Correr", 10);
        agregarEjercicio(exercises_dbCollection, "Correr", 12);
        agregarEjercicio(exercises_dbCollection, "Ciclismo ligero", 6);
        agregarEjercicio(exercises_dbCollection, "Ciclismo moderado", 8);
        agregarEjercicio(exercises_dbCollection, "Ciclismo intenso", 10);
        agregarEjercicio(exercises_dbCollection, "Natación ligera", 7);
        agregarEjercicio(exercises_dbCollection, "Natación moderada", 10);
        agregarEjercicio(exercises_dbCollection, "Natación intensa", 12);
        agregarEjercicio(exercises_dbCollection, "Saltar la cuerda", 10);
        agregarEjercicio(exercises_dbCollection, "Aeróbicos de bajo impacto", 5);
        agregarEjercicio(exercises_dbCollection, "Aeróbicos de alto impacto", 7);
        agregarEjercicio(exercises_dbCollection, "Levantamiento de pesas (ligero)", 4);
        agregarEjercicio(exercises_dbCollection, "Levantamiento de pesas (intenso)", 6);
         */
    }
    /*
    private void agregarAlimento(CollectionReference collection, String nombre, int caloriasU) {
        Map<String, Object> food = new HashMap<>();
        food.put("name", nombre);
        food.put("caloriesU", caloriasU);
        collection.add(food);
    }
    */
    /*
    private void agregarEjercicio(CollectionReference collection, String nombre, int caloriasU) {
        Map<String, Object> excercises = new HashMap<>();
        excercises.put("name_excercise", nombre);
        excercises.put("caloriesU", caloriasU);
        collection.add(excercises);
    }

     */

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