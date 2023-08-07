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
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

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
    int caloriasAlimentos;

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
        view = inflater.inflate(R.layout.fragment_agregar_mas1, container, false);
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
        cargarAlimentosDisponibles();
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
            String userId = mAuth.getCurrentUser().getUid();
            String userIdPrefix = userId.substring(0, 4); // Obtener los primeros 4 caracteres del userId

            CollectionReference alimentosRef = db.collection("eat");
            Query query = alimentosRef.whereGreaterThanOrEqualTo(FieldPath.documentId(), userIdPrefix)
                    .whereLessThan(FieldPath.documentId(), userIdPrefix + "\uf8ff");

            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    listAlimentos.clear(); // Limpiar la lista actual de alimentos
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Obtener los datos de cada documento y agregarlos a la lista
                        Alimentos alimento = document.toObject(Alimentos.class);
                        alimento.setId(document.getId());
                        alimento.setAlimento(document.getString("eat"));
                        alimento.setCalorias(document.getString("calories") + " cal");
                        alimento.setCantidad(document.getString("amount"));
                        listAlimentos.add(alimento);
                        listViewAlimentosAdapter.notifyDataSetChanged();
                    }
                } else {
                    // Manejar el caso de error
                    System.out.println("Error en el momento de mostrar la lista de alimentos");
                }
            });
        } catch (Exception e) {
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
        ArrayAdapter<String> adapterAlimentos = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listAlimentosDisponibles);
        adapterAlimentos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlimento.setAdapter(adapterAlimentos);

        spinnerAlimento.setSelection(0);
        spinnerCantidad.setSelection(0);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        btnAgregarComida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userId = mAuth.getCurrentUser().getUid();
                String userIdPrefix = userId.substring(0, 4); // Obtener los primeros 4 caracteres del userId
                String alimentos = spinnerAlimento.getSelectedItem().toString();
                String cantidades = spinnerCantidad.getSelectedItem().toString();
                db.collection("food").document();
                obtenerCaloriasAlimento(alimentos, cantidades);
                //int cantidadOperacion = Integer.parseInt(cantidades);
                //int calorias;
                //cantidadOperacion= 1;
                System.out.println(caloriasAlimentos);
                String caloriasString = String.valueOf(caloriasAlimentos);
                String alimento = alimentos;
                String cantidad = cantidades;
                Alimentos alimentoss = new Alimentos();
                alimentoss.setAlimento(alimento);
                alimentoss.setCaloria(caloriasString);
                alimentoss.setCantidad(cantidad);
                long fechaMilisegundos = getFechaMilisegundos();
                String fechaNormal = getFechaNormal(fechaMilisegundos);
                alimentoss.setFechaRegistro(getFechaNormal(getFechaMilisegundos()));
                Map<String,Object> alimentosDb=new HashMap<>();
                alimentosDb.put("calories",caloriasString);
                alimentosDb.put("eat",alimento);
                alimentosDb.put("amount",cantidad);
                alimentosDb.put("date", fechaNormal);
                String documentId = userIdPrefix + "-" + UUID.randomUUID().toString(); // Generar un ID único combinando los primeros 4 caracteres del userId y un identificador aleatorio
                db.collection("eat").document(documentId).set(alimentosDb).addOnSuccessListener(
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
    private ArrayList<String> listAlimentosDisponibles = new ArrayList<>();

    private void cargarAlimentosDisponibles() {
        CollectionReference alimentosRef = db.collection("food");
        alimentosRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listAlimentosDisponibles.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String alimento = document.getString("name");
                    listAlimentosDisponibles.add(alimento);
                }
            } else {
                // Manejar el caso de error
                System.out.println("Error al cargar los alimentos disponibles");
            }
        });
    }

    private void obtenerCaloriasAlimento(String nombreAlimento, String cantidadSeleccionada) {
        CollectionReference alimentosRef = db.collection("food");
        Query query = alimentosRef.whereEqualTo("name", nombreAlimento);
        System.out.println(nombreAlimento);
        System.out.println(cantidadSeleccionada);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String caloriasString = document.getString("caloriesU");
                    if (caloriasString != null) {
                        int caloriasUnidad = Integer.parseInt(caloriasString);
                        int cantidad = Integer.parseInt(cantidadSeleccionada);
                         caloriasAlimentos = caloriasUnidad * cantidad;
                        System.out.println(caloriasAlimentos);
                        // Realizar el resto de las operaciones con las calorías obtenidas
                        // ...

                        return; // Finalizar la función después de obtener las calorías
                    }
                }

                // El alimento seleccionado no se encontró en la base de datos
                // Manejar el caso de error
            } else {
                // Manejar el caso de error en la consulta a la base de datos
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
        CollectionReference alimentosRef = db.collection("eat");
        DocumentReference alimentoRef = alimentosRef.document(listAlimentos.get(position).getId());
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
            CollectionReference ejerciciosRef = db.collection("exercise");
            String userIdPrefix = userId.substring(0, 4); // Obtener los primeros 4 caracteres del userId
            Query query = ejerciciosRef.whereGreaterThanOrEqualTo(FieldPath.documentId(), userIdPrefix)
                    .whereLessThan(FieldPath.documentId(), userIdPrefix + "\uf8ff");
            query.get().addOnCompleteListener(task -> {
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
                String userIdPrefix = userId.substring(0, 4); // Obtener los primeros 4 caracteres del userId
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
                long fechaMilisegundos = getFechaMilisegundos();
                String fechaNormal = getFechaNormal(fechaMilisegundos);
                Map<String,Object> ejerciciosDb=new HashMap<>();
                ejerciciosDb.put("calories_ex",caloriasString);
                ejerciciosDb.put("name_exercise",ejericio);
                ejerciciosDb.put("minutes",cantidad);
                ejerciciosDb.put("date",fechaNormal);
                String documentId = userIdPrefix + "-" + UUID.randomUUID().toString(); // Generar un ID único combinando los primeros 4 caracteres del userId y un identificador aleatorio
                db.collection("exercise").document(documentId).set(ejerciciosDb).addOnSuccessListener(
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
        CollectionReference ejerciciosRef = db.collection("exercise");
        DocumentReference ejercicioRef = ejerciciosRef.document(listEjercicios.get(position).getId());
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