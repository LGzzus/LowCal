package com.example.lowca;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.lowca.Adaptadores.ListViewAlimentosAdapter;
import com.example.lowca.Adaptadores.ListViewEjerciciosAdapter;
import com.example.lowca.Models.Alimentos;
import com.example.lowca.Models.Ejercicios;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Agregar_mas#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Agregar_mas extends Fragment {
    View view;
    private ArrayList<Alimentos> listAlimentos = new ArrayList<Alimentos>();
    private ArrayList<Ejercicios> listEjercicios = new ArrayList<Ejercicios>();
    ArrayAdapter<Alimentos> arrayAdapterPersona;
    private ListViewAlimentosAdapter listViewAlimentosAdapter;
    private ListViewEjerciciosAdapter listViewEjerciciosAdapter;
    LinearLayout linearLayoutAgregarComida;

    ListView listViewAlimentos, listViewEjericicios;
    Spinner spinnerCalorias, spinnerAlimento, spinnerCantidad, spinnerEjercicio, spinnerMinutos;
    Button btnAgregarAlimento,btnAgregarEjercicio, btnAlimentacionMas , btnEjercicioMas;
    //Se usara para que se identique el objeto
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    public String userId;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Agregar_mas() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Agregar_mas.
     */
    // TODO: Rename and change types and number of parameters
    public static Agregar_mas newInstance(String param1, String param2) {
        Agregar_mas fragment = new Agregar_mas();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_agregar_mas, container, false);
        spinnerCalorias = (Spinner) view.findViewById(R.id.spinnerCalorias);
        spinnerAlimento = (Spinner) view.findViewById(R.id.spinnerAlimento);
        spinnerCantidad = (Spinner) view.findViewById(R.id.spinnerCantidad);

        spinnerEjercicio = (Spinner) view.findViewById(R.id.spinnerEjercicio);
        spinnerMinutos = (Spinner) view.findViewById(R.id.spinnerMinutos);

        btnAgregarAlimento = (Button) view.findViewById(R.id.btnAgregarComida);

        listViewAlimentos = view.findViewById(R.id.listViewAlimentos);
        listViewEjericicios = view.findViewById(R.id.listViewEjercicios);

        btnAlimentacionMas = view.findViewById(R.id.btnAlimentacionMas);
        btnEjercicioMas = view.findViewById(R.id.btnEjercicioMas);

        listViewAlimentosAdapter = new ListViewAlimentosAdapter(getActivity(), listAlimentos);
        listViewAlimentos.setAdapter(listViewAlimentosAdapter);

        listViewEjerciciosAdapter = new ListViewEjerciciosAdapter(getActivity(), listEjercicios);
        listViewEjericicios.setAdapter(listViewEjerciciosAdapter);

        btnAlimentacionMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarAlimento(view);
            }
        });
        btnEjercicioMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                agregarEjercicio(view);
            }
        });
        inicializarFirebase();
        listarAlimentos();
        listarEjercicios();
        listViewAlimentos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                mostrarDialogoBorrarAlimento(position);
                return true;
            }
        });
        listViewEjericicios.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                mostrarDialogoBorrarEjercicio(position);
                return false;
            }
        });
        return view;
    }

    private void inicializarFirebase(){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }
    private void listarAlimentos(){
        try {
            userId = mAuth.getCurrentUser().getUid();
            DocumentReference acountRef = db.collection("account").document(userId);
            CollectionReference alimentosRef = acountRef.collection("eat");
            alimentosRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    listAlimentos.clear(); // Limpiar la lista actual de alimentos
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Obtener los datos de cada documento y agregarlos a la lista
                        Alimentos alimento = document.toObject(Alimentos.class);
                        alimento.setId(document.getId());
                        alimento.setAlimento(document.getString("eat"));
                        alimento.setCalorias(document.getString("calories")+" cal");
                        alimento.setCantidad(document.getString("amount"));
                        listAlimentos.add(alimento);
                        listViewAlimentosAdapter.notifyDataSetChanged();
                    }


                } else {
                    // Manejar el caso de error
                    System.out.println("Error en el momento de mostar la lista de alimentos");
                }
            });
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public void agregarAlimento(View view){
        Context context = getContext();
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(
                context
        );
        View mView = getLayoutInflater().inflate(R.layout.agregar_comida,null);
        Button btnAgregarComida = (Button) mView.findViewById(R.id.btnAgregarComida);
        //Spinner spinnerCaloria = mView.findViewById(R.id.spinnerCalorias);
        Spinner spinnerAlimento = mView.findViewById(R.id.spinnerAlimento);
        Spinner spinnerCantidad = mView.findViewById(R.id.spinnerCantidad);
        
        spinnerAlimento.setSelection(0);
        spinnerCantidad.setSelection(0);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        btnAgregarComida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userId = mAuth.getCurrentUser().getUid();
                //String calorias = spinnerCaloria.getSelectedItem().toString();
                String alimentos = spinnerAlimento.getSelectedItem().toString();
                String cantidades = spinnerCantidad.getSelectedItem().toString();
                int cantidadOperacion = Integer.parseInt(cantidades);
                int calorias;
                if(alimentos.equals("Plato de Arroz")){
                    calorias = cantidadOperacion * 130;
                } else if (alimentos.equals("Pieza de pollo")) {
                    calorias = cantidadOperacion * 176;
                } else if (alimentos.equals("Tazón de avena con canela y edulcorante")) {
                    calorias = cantidadOperacion * 170;
                } else if (alimentos.equals("Taza de fresas")) {
                    calorias = cantidadOperacion * 50;
                } else if (alimentos.equals("Puñado de almendras (10 almendras)")) {
                    calorias = cantidadOperacion * 75;
                } else if (alimentos.equals("Ensalada de pollo a la parrilla con vegetales mixtos y vinagreta baja en grasa")) {
                    calorias = cantidadOperacion * 250;
                } else if (alimentos.equals("Sopa de verduras casera con una porción de pollo o pescado a la plancha")) {
                    calorias = cantidadOperacion * 250;
                } else if (alimentos.equals("Palitos de zanahoria y apio con hummus")) {
                    calorias = cantidadOperacion * 130;
                } else if (alimentos.equals("Yogur griego bajo en grasa con semillas de chía")) {
                    calorias = cantidadOperacion * 150;
                } else if (alimentos.equals("Pechuga de pollo al horno con brócoli al vapor y media taza de arroz integral")) {
                    calorias = cantidadOperacion * 350;
                } else if (alimentos.equals("Rodajas de pepino con salsa de yogur bajo en grasa y hierbas")) {
                    calorias = cantidadOperacion * 70;
                } else if (alimentos.equals("Una porción de gelatina sin azúcar")) {
                    calorias = cantidadOperacion * 15;
                } else if (alimentos.equals("Tortilla de claras de huevo con espinacas y queso bajo en grasa")) {
                    calorias = cantidadOperacion * 200;
                } else if (alimentos.equals("Pechuga de pollo a la parrilla con espárragos y aguacate")) {
                    calorias = cantidadOperacion * 350;
                } else if (alimentos.equals("Salmón al horno con brócoli al vapor y ensalada verde")) {
                    calorias = cantidadOperacion * 400;
                } else if (alimentos.equals("Rodajas de pepino con salsa de yogur bajo en grasa")) {
                    calorias = cantidadOperacion * 100;
                } else if (alimentos.equals("Batido de proteínas vegetales con espinacas, plátano y leche de almendras")) {
                    calorias = cantidadOperacion * 300;
                } else if (alimentos.equals("Ensalada de garbanzos con vegetales, aceite de oliva y limón")) {
                    calorias = cantidadOperacion * 400;
                } else if (alimentos.equals("Manzana con mantequilla de almendras")) {
                    calorias = cantidadOperacion * 200;
                } else if (alimentos.equals("Tofu salteado con vegetales y arroz integral")) {
                    calorias = cantidadOperacion * 500;
                } else if (alimentos.equals("Palitos de apio con hummus")) {
                    calorias = cantidadOperacion * 200;
                } else
                    //Areglar Array
                    if (alimentos.equals("Manzana")) {
                    calorias = cantidadOperacion * 52;
                } else if (alimentos.equals("Piña")) {
                    calorias = cantidadOperacion * 55;
                } else if (alimentos.equals("Albaricoque")) {
                    calorias = cantidadOperacion * 43;
                } else if (alimentos.equals("Pera")) {
                    calorias = cantidadOperacion * 55;
                } else if (alimentos.equals("Plátano")) {
                    calorias = cantidadOperacion * 88;
                } else if (alimentos.equals("Arándanos")) {
                    calorias = cantidadOperacion * 35;
                } else if (alimentos.equals("Naranja sanguina")) {
                    calorias = cantidadOperacion * 45;
                } else if (alimentos.equals("Moras")) {
                    calorias = cantidadOperacion * 43;
                } else if (alimentos.equals("Arándanos rojos")) {
                    calorias = cantidadOperacion * 46;
                } else if (alimentos.equals("Fresas")) {
                    calorias = cantidadOperacion * 32;
                } else if (alimentos.equals("Higo")) {
                    calorias = cantidadOperacion * 107;
                } else if (alimentos.equals("Pomelo")) {
                    calorias = cantidadOperacion * 50;
                } else if (alimentos.equals("Granada")) {
                    calorias = cantidadOperacion * 74;
                } else if (alimentos.equals("Escaramujo")) {
                    calorias = cantidadOperacion * 162;
                } else if (alimentos.equals("Melón")) {
                    calorias = cantidadOperacion * 54;
                } else if (alimentos.equals("Frambuesas")) {
                    calorias = cantidadOperacion * 36;
                } else if (alimentos.equals("Jengibre")) {
                    calorias = cantidadOperacion * 80;
                } else if (alimentos.equals("Kiwi")) {
                    calorias = cantidadOperacion * 51;
                } else if (alimentos.equals("Cerezas")) {
                    calorias = cantidadOperacion * 50;
                } else {
                    // Alimento no reconocido
                    calorias = 0;
                }
                String caloriasString = String.valueOf(calorias);
                String alimento = alimentos;
                String cantidad = cantidades;
                Alimentos alimentoss = new Alimentos();
                alimentoss.setAlimento(alimento);
                alimentoss.setCaloria(caloriasString);
                alimentoss.setCantidad(cantidad);
                alimentoss.setFechaRegistro(getFechaNormal(getFechaMilisegundos()));
                DocumentReference acountRef = db.collection("account").document(userId);
                CollectionReference alimentosRef = acountRef.collection("eat");
                DocumentReference nuevoAlimentoRef = alimentosRef.document();
                Map<String,Object> alimentosDb=new HashMap<>();
                alimentosDb.put("calories",caloriasString);
                alimentosDb.put("eat",alimento);
                alimentosDb.put("amount",cantidad);
                //alimentosDb.put("day",1);
                nuevoAlimentoRef.set(alimentosDb).addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                listAlimentos.add(alimentoss);
                                dialog.dismiss();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        listarAlimentos();
                                    }
                                }, 100);
                                //Toast.makeText(Agregar_mas.this,"El alimento se agrego correctamente",Toast.LENGTH_SHORT).show();
                            }
                        }
                ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Ocurrio un error");
                    }
                });

            }
        });
    }


    public long getFechaMilisegundos(){
        Calendar calendar = Calendar.getInstance();
        long timepoUnix = calendar.getTimeInMillis();
        return timepoUnix;
    }
    public String getFechaNormal(long fechaMilisegundos){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GTM-5"));
        String fecha = sdf.format(fechaMilisegundos);
        return fecha;
    }
    private void mostrarDialogoBorrarAlimento(int position) {
        //listViewAlimentosAdapter.notifyDataSetChanged();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("¿Desea borrar este registro?")
                .setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        borrarRegistroAlimento(position);

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void borrarRegistroAlimento(int position) {
        userId = mAuth.getCurrentUser().getUid();
        DocumentReference acountRef = db.collection("account").document(userId);
        CollectionReference alimentosRef = acountRef.collection("eat");
        // Obtener la referencia al documento específico que deseas eliminar
        //DocumentReference alimentoRef = alimentosRef.document(listViewAlimentosAdapter.getItem(position).getId());
        DocumentReference alimentoRef = alimentosRef.document(listAlimentos.get(position).getId());
        //DocumentReference alimentoRef = alimentosRef.document(arrayAdapterPersona.getItem(position).getId());
        alimentoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                // Borrado exitoso, ahora puedes eliminar el registro de la lista
                // Eliminar el registro de la lista
                listAlimentos.remove(position);
                // Notificar al adaptador que los datos han cambiado
                listViewAlimentosAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Manejar el caso de error en el borrado
                System.out.println("Error al borrar el registro: " + e.getMessage());
            }
        });

    }

    //Inicia la funcionalidad y logica de los Ejercicios
    private void listarEjercicios(){
        try {
            userId = mAuth.getCurrentUser().getUid();
            DocumentReference acountRef = db.collection("account").document(userId);
            CollectionReference ejerciciosRef = acountRef.collection("exercise");
            ejerciciosRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    listEjercicios.clear(); // Limpiar la lista actual de alimentos
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Obtener los datos de cada documento y agregarlos a la lista
                        Ejercicios ejercicio = document.toObject(Ejercicios.class);
                        ejercicio.setId(document.getId());
                        ejercicio.setEjercicio(document.getString("name_exercise"));
                        ejercicio.setMinutos(document.getString("minutes")+ " min.");
                        ejercicio.setCaloriasQuemadas(document.getString("calories_ex")+ " cal");
                        listEjercicios.add(ejercicio);
                        listViewEjerciciosAdapter.notifyDataSetChanged();
                    }


                } else {
                    // Manejar el caso de error
                    System.out.println("Error en el momento de mostar la lista de ejercicios");
                }
            });
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public void agregarEjercicio(View view){
        Context context = getContext();
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(
                context
        );
        View mView = getLayoutInflater().inflate(R.layout.agregar_ejercicio,null);
        Button btnAgregarEjercicio = (Button) mView.findViewById(R.id.btnAgregarEjericio);
        Spinner spinnerEjercicio = mView.findViewById(R.id.spinnerEjercicio);
        Spinner spinnerMinutos = mView.findViewById(R.id.spinnerMinutos);

        spinnerEjercicio.setSelection(0);
        spinnerMinutos.setSelection(0);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        btnAgregarEjercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userId = mAuth.getCurrentUser().getUid();
                //String calorias = spinnerCaloria.getSelectedItem().toString();
                String ejercicios = spinnerEjercicio.getSelectedItem().toString();
                String minutos = spinnerMinutos.getSelectedItem().toString();
                int cantidadOperacion = Integer.parseInt(minutos);
                int caloriasQuemadas;
                if (ejercicios.equals("Caminata ligera")) {
                    caloriasQuemadas = cantidadOperacion * 4;
                } else if (ejercicios.equals("Caminata rápida")) {
                    caloriasQuemadas = cantidadOperacion * 6;
                } else if (ejercicios.equals("Correr a 8 km/h")) {
                    caloriasQuemadas = cantidadOperacion * 10;
                } else if (ejercicios.equals("Correr a 10 km/h")) {
                    caloriasQuemadas = cantidadOperacion * 12;
                } else if (ejercicios.equals("Ciclismo ligero")) {
                    caloriasQuemadas = cantidadOperacion * 6;
                } else if (ejercicios.equals("Ciclismo moderado")) {
                    caloriasQuemadas = cantidadOperacion * 8;
                } else if (ejercicios.equals("Ciclismo intenso")) {
                    caloriasQuemadas = cantidadOperacion * 10;
                } else if (ejercicios.equals("Natación ligera")) {
                    caloriasQuemadas = cantidadOperacion * 7;
                } else if (ejercicios.equals("Natación moderada")) {
                    caloriasQuemadas = cantidadOperacion * 10;
                } else if (ejercicios.equals("Natación intensa")) {
                    caloriasQuemadas = cantidadOperacion * 12;
                } else if (ejercicios.equals("Saltar la cuerda")) {
                    caloriasQuemadas = cantidadOperacion * 10;
                } else if (ejercicios.equals("Aeróbicos de bajo impacto")) {
                    caloriasQuemadas = cantidadOperacion * 5;
                } else if (ejercicios.equals("Aeróbicos de alto impacto")) {
                    caloriasQuemadas = cantidadOperacion * 7;
                } else if (ejercicios.equals("Levantamiento de pesas (ligero)")) {
                    caloriasQuemadas = cantidadOperacion * 4;
                } else if (ejercicios.equals("Levantamiento de pesas (intenso)")) {
                    caloriasQuemadas = cantidadOperacion * 6;
                } else {
                    caloriasQuemadas = 0;
                }
                String caloriasString = String.valueOf(caloriasQuemadas);
                String ejericio = ejercicios;
                String cantidad = minutos;
                Ejercicios ejercicioss = new Ejercicios();
                ejercicioss.setEjercicio(ejericio);
                ejercicioss.setCaloriasQuemadas(caloriasString);
                ejercicioss.setMinutos(minutos);
                ejercicioss.setFechaRegistro(getFechaNormal(getFechaMilisegundos()));
                DocumentReference acountRef = db.collection("account").document(userId);
                CollectionReference ejerciciosRef = acountRef.collection("exercise");
                DocumentReference nuevoEjercicioRef = ejerciciosRef.document();
                Map<String,Object> ejerciciosDb=new HashMap<>();
                ejerciciosDb.put("calories_ex",caloriasString);
                ejerciciosDb.put("name_exercise",ejericio);
                ejerciciosDb.put("minutes",cantidad);
                //alimentosDb.put("day",1);
                nuevoEjercicioRef.set(ejerciciosDb).addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                listEjercicios.add(ejercicioss);
                                dialog.dismiss();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        listarEjercicios();
                                    }
                                }, 100);
                            }
                        }
                ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Ocurrio un error");
                    }
                });

            }
        });
    }



    private void mostrarDialogoBorrarEjercicio(int position) {
        //listViewAlimentosAdapter.notifyDataSetChanged();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("¿Desea borrar este registro?")
                .setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        borrarRegistroEjercicio(position);

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void borrarRegistroEjercicio(int position) {
        userId = mAuth.getCurrentUser().getUid();
        DocumentReference acountRef = db.collection("account").document(userId);
        CollectionReference ejerciciosRef = acountRef.collection("exercise");
        // Obtener la referencia al documento específico que deseas eliminar
        //DocumentReference alimentoRef = alimentosRef.document(listViewAlimentosAdapter.getItem(position).getId());
        DocumentReference ejercicioRef = ejerciciosRef.document(listEjercicios.get(position).getId());
        //DocumentReference alimentoRef = alimentosRef.document(arrayAdapterPersona.getItem(position).getId());
        ejercicioRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                // Borrado exitoso, ahora puedes eliminar el registro de la lista
                // Eliminar el registro de la lista
                listEjercicios.remove(position);
                // Notificar al adaptador que los datos han cambiado
                listViewEjerciciosAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Manejar el caso de error en el borrado
                System.out.println("Error al borrar el registro: " + e.getMessage());
            }
        });

    }

}