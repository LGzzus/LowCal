package com.example.lowca;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Inicio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Inicio extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View vista;
    TextView tvCaloriasDieta,tvCaloriasBasales,txtViewCaloriasDieta, textViewConsumidasGraf, textViewReservadasGraf;
    ProgressBar progressBarReservadas, progressBarConsumidas;


    //TextView tvCaloriasDieta,tvCaloriasBasales;
    TextView tvProgressRecomendadas;

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    String userUid;
    long edad,hoy;
    double mb;
    int calBal , totalConsumidas;

    ProgressBar progressBarRecomendadas;

    public Inicio() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Inicio.
     */
    // TODO: Rename and change types and number of parameters
    public static Inicio newInstance(String param1, String param2) {
        Inicio fragment = new Inicio();
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
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_inicio, container, false);
        tvCaloriasDieta=vista.findViewById(R.id.tvReservadas);
        tvCaloriasBasales=vista.findViewById(R.id.tvCaloriasBasales);

        txtViewCaloriasDieta= vista.findViewById(R.id.tvCaloriasDieta);

        textViewConsumidasGraf = vista.findViewById(R.id.textViewConsumidasGraf);
        textViewReservadasGraf = vista.findViewById(R.id.textViewReservadasGraf);
        progressBarConsumidas = vista.findViewById(R.id.progressBarConsumidas);
        progressBarReservadas = vista.findViewById(R.id.progressBarRecomendadas);

        tvProgressRecomendadas=vista.findViewById(R.id.tvProgressRecomendadas);
        progressBarRecomendadas=vista.findViewById(R.id.progressBarRecomendadas);

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        userUid = mAuth.getCurrentUser().getUid();

        CollectionReference parentCollectionRef = db.collection("account");
        DocumentReference documentRef = parentCollectionRef.document(userUid);
        CollectionReference subCollectionRef = documentRef.collection("dieta");
        Query query = subCollectionRef.orderBy("hora_registro", Query.Direction.DESCENDING).limit(3);

        graficar();


        /*subCollectionRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        //String documentId=documentSnapshot.getId();

                        String dato = documentSnapshot.getString("calorias_usuario");
                        // Realiza las operaciones necesarias con los datos obtenidos de la subcolección
                        String  cal= dato;
                        System.out.println("********Calorias tablero: "+cal+"**********");


                        tvCaloriasDieta.setText(dato+" kcal");

                    }
                })
                .addOnFailureListener(e -> {
                    // Maneja el error en caso de que la lectura de la subcolección falle
                });*/

        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        if (!querySnapshot.isEmpty()) {
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                            String documentoId = documentSnapshot.getId();
                            // Obtén los datos del documento
                            Map<String, Object> datos = documentSnapshot.getData();
                            // Accede a los campos específicos
                            String calorias = (String) datos.get("calorias_usuario");


                            tvCaloriasDieta.setText(calorias+" kcal");
                        } else {
                            // No se encontraron documentos en la colección
                            Log.d("TAG", "No se encontraron documentos en la colección");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Ocurrió un error al obtener los documentos
                        Log.w("TAG", "Error al obtener los documentos", e);
                    }
                });



        /*

            Calcula el metabolismo basal (MB):

            Para hombres: MB = 66 + (13.75 x peso en kg) + (5 x altura en cm) - (6.75 x edad en años).
            Para mujeres: MB = 655 + (9.56 x peso en kg) + (1.85 x altura en cm) - (4.68 x edad en años).

            Determina el nivel de actividad física:

            Sedentario (poco o ningún ejercicio): MB x 1.2.
            Moderadamente activo (ejercicio moderado de 3-5 días por semana): MB x 1.55.

       */
        DocumentReference documentReferencia=db.collection("antropometric_dates").document(userUid);
        documentReferencia.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // El documento existe y contiene datos
                        String genero = documentSnapshot.getString("gender");
                        Double altura= documentSnapshot.getDouble("height");
                        Double peso= documentSnapshot.getDouble("weight");
                        String nivelActividad=documentSnapshot.getString("physical_activity_lever");
                        String nacido=documentSnapshot.getString("birth_date");
                        Double pesoObjetivo=documentSnapshot.getDouble("target_weight");
                        Double caloriasBasales=documentSnapshot.getDouble("calculated_calories");
                        int calBal=Double.valueOf(caloriasBasales).intValue();
                        tvCaloriasBasales.setText(String.valueOf(calBal));

                        //System.out.println("******NAcido: "+nacido+"************");
                        //Obtener la fecha actual
                        hoy = System.currentTimeMillis();

                        //System.out.println("****Ahora: "+ahora+"*******");
                        Date fechaActual = new Date(hoy);
                        // System.out.println("****FechaActual: "+fechaActual+"*******");
                        // Crea un formato para mostrar la fecha
                        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                        // System.out.println("****FormatoFecha: "+formatoFecha+"*******");
                        // Convierte la fecha actual al formato deseado
                        String fechaFormateada = formatoFecha.format(fechaActual);
                        //System.out.println("****FechaFormateada: "+fechaFormateada+"*******");

                        // Imprime la fecha formateada
                        System.out.println("Fecha actual: " + fechaFormateada);

                        //Obtener formato de la fecha del usuaario
                        // String fechaString = "31/12/2022";

                        long milisegundos = 0;
                        long milisegundosEdad=0;

                        try {
                            // SimpleDateFormat formatoFecha1 = new SimpleDateFormat("dd/MM/yyyy");
                            java.util.Date fecha = formatoFecha.parse(nacido);
                            milisegundos = fecha.getTime();
                            System.out.println("Fecha Formato usuario: " + milisegundos);
                            milisegundosEdad= hoy-milisegundos;
                            long milisegundosPorAnio = 365 * 24 * 60 * 60 * 1000L;
                            edad = milisegundosEdad / milisegundosPorAnio;
                            System.out.println("******Edad: "+edad+"**********");

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        //Aplicamos la formula
                        /**

                         Calcula el metabolismo basal (MB):

                         Para hombres: MB = 66 + (13.75 x peso en kg) + (5 x altura en cm) - (6.75 x edad en años).
                         Para mujeres: MB = 655 + (9.56 x peso en kg) + (1.85 x altura en cm) - (4.68 x edad en años).

                         Determina el nivel de actividad física:

                         Sedentario (poco o ningún ejercicio): MB x 1.2.
                         Moderadamente activo (ejercicio moderado de 3-5 días por semana): MB x 1.55.

                         */
                        //double mb;
                        //Hombre
                        if(genero.equals("Hombre")){
                            mb=66+(13.75*peso)+(5*altura)-(6.75*edad);
                            System.out.println("****Calorias recomendadas: "+mb);
                            if(pesoObjetivo>peso){
                                mb=mb+(mb*0.15);
                                System.out.println("*****Calorias para perder ganar: "+mb);
                            }else if(pesoObjetivo<peso){
                                mb=mb-(mb*0.12);

                                System.out.println("*****Calorias para perder peso: "+mb);
                            }
                            if(nivelActividad.equals("Sedentaria")){
                                mb=mb*1.2;

                                System.out.println("****Calorias recomendadas segun el nivel de " +
                                        "acividad: "+mb);
                            }else if(nivelActividad.equals("Activa")){
                                mb=mb*1.55;
                                System.out.println("****Calorias recomendadas segun el nivel de " +
                                        "acividad: "+mb);
                            }

                            String cal= String.valueOf(mb);
                            int basales=Integer.parseInt(cal);
                            progressBarRecomendadas.setProgress(100);
                            tvProgressRecomendadas.setText(basales+" kcal Recomendadas");
                            progressBarRecomendadas.setVisibility(View.VISIBLE);


                            DocumentReference doc = db.collection("antropometric_dates").document(userUid);
                            Map<String, Object> campoNuevo = new HashMap<>();
                            campoNuevo.put("calculated_calories", mb);
                            // Actualiza el documento con el campo nuevo
                            doc.update(campoNuevo)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // La actualización fue exitosa
                                            Log.d("TAG", "Campo nuevo agregado correctamente");


                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Ocurrió un error al actualizar el documento
                                            Log.w("TAG", "Error al agregar el campo nuevo", e);
                                        }
                                    });





                            //Mujer
                        }else if(genero.equals("Mujer")){
                            mb=66+(9.56*peso)+(1.85*altura)-(4.68*edad);
                            System.out.println("****Calorias recomendadas: "+mb);
                            if(pesoObjetivo>peso){
                                mb=mb+(mb*0.15);
                                System.out.println("*****Calorias para perder ganar: "+mb);
                            }else if(pesoObjetivo<peso){
                                mb=mb-(mb*0.15);

                                System.out.println("*****Calorias para perder peso: "+mb);
                            }
                            if(nivelActividad.equals("Sedentaria")){
                                mb=mb*1.2;

                                System.out.println("****Calorias recomendadas segun el nivel de " +
                                        "acividad: "+mb);
                            }else if(nivelActividad.equals("Activa")){
                                mb=mb*1.55;
                                System.out.println("****Calorias recomendadas segun el nivel de " +
                                        "acividad: "+mb);
                            }



                            int calBasales = Double.valueOf(mb).intValue();
                            tvCaloriasBasales.setText(String.valueOf(calBasales));

                            progressBarRecomendadas.setProgress(100);
                            tvProgressRecomendadas.setText(calBasales+" kcal recomendadas");
                            progressBarRecomendadas.setVisibility(View.VISIBLE);
                            DocumentReference doc = db.collection("antropometric_dates").document(userUid);
                            Map<String, Object> campoNuevo = new HashMap<>();
                            campoNuevo.put("calculated_calories", mb);
                            // Actualiza el documento con el campo nuevo
                            doc.update(campoNuevo)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // La actualización fue exitosa
                                            Log.d("TAG", "Campo nuevo agregado correctamente");

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Ocurrió un error al actualizar el documento
                                            Log.w("TAG", "Error al agregar el campo nuevo", e);
                                        }
                                    });
                        }else{
                            System.out.println("Error");
                        }




                        // Realiza las operaciones necesarias con los datos del documento
                    } else {
                        // El documento no existe
                    }
                })
                .addOnFailureListener(e -> {
                    // Maneja el error en caso de que la lectura del documento falle
                });
        // Obtén la colección "dieta" del usuario actual

        BarChart barChart = vista.findViewById(R.id.barChart);
        barChart.getDescription().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        cargarYMostrarGrafico(barChart);


        return vista;
        //return inflater.inflate(R.layout.fragment_inicio, container, false);




    }

    private void actualizarProgresoConsumidas(int caloriasConsumidas, double caloriasBasales) {
        int progress = (int) ((caloriasConsumidas * 100) / caloriasBasales);
        progressBarConsumidas.setProgress(progress);
    }
    private void graficar(){
        try {
            String userId = mAuth.getCurrentUser().getUid();
            String userIdPrefix = userId.substring(0, 4);
            CollectionReference comidaCollectionRef = db.collection("eat");
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int monthOfYear = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            String fecha = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
            Query query = comidaCollectionRef.whereGreaterThanOrEqualTo(FieldPath.documentId(), userIdPrefix)
                    .whereEqualTo("date", fecha)
                    .whereLessThan(FieldPath.documentId(), userIdPrefix + "\uf8ff");
            // Calorias consumidas
            // Consulta todos los documentos en la colección "dieta"
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful())
                {
                     totalConsumidas = 0;
                    // Recorre cada documento en la colección
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Obtiene el valor del campo "calories" como un entero
                            String caloriesString = document.getString("calories");
                            int calories = Integer.parseInt(caloriesString);
                            // Suma las calorías al total
                            totalConsumidas += calories;
                    }
                    txtViewCaloriasDieta.setText(String.valueOf(totalConsumidas) + " kcal");
                    textViewConsumidasGraf.setText(String.valueOf(totalConsumidas) + " kcal Consumidas");

                    actualizarProgresoConsumidas(totalConsumidas,calBal);
                    DocumentReference basalesDocumentRef = db.collection("antropometric_dates").document(userUid);
                    basalesDocumentRef.get()
                        .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                // Obtén el primer documento de la colección "basales"
                                documentSnapshot.getDouble("calculated_calories");
                                Double caloriasBasales = documentSnapshot.getDouble("calculated_calories");
                                calBal = Double.valueOf(caloriasBasales).intValue();
                                    int progressConsumidas = (totalConsumidas * 100) / calBal;
                                    // Establece el porcentaje de progreso en el ProgressBar de consumidas
                                    progressBarConsumidas.setProgress(progressConsumidas);

                            } else {
                                // No se encontraron documentos en la colección "basales"
                                //Me falta arreglar esto
                                Toast.makeText(getContext(), "No se encontraron calorías basales para graficar", Toast.LENGTH_SHORT).show();
                            }

                        })
                            .addOnFailureListener(e -> {
                                    // Maneja el error en caso de que la lectura del documento falle

                    });
            }else {
                    // El documento no existe
                }
                    })
                    .addOnFailureListener(e -> {
                        // Maneja el error en caso de que la lectura del documento falle
                    });
    } catch (Exception e){
            System.out.println(e);
        }




}
    private void cargarYMostrarGrafico(BarChart barChart) {
        Calendar calendar = Calendar.getInstance();
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

// Calcula el índice del domingo como 1 y el sábado como 7
        int sundayIndex = 1;
        int saturdayIndex = 7;

// Calcula el índice del día actual (resta 1 porque los índices en la lista de entradas comienzan en 0)
        int todayIndex = currentDayOfWeek - 1;

// Calcula el índice del día anterior al domingo de la semana actual
        int startDayIndex = todayIndex - (currentDayOfWeek - sundayIndex);

// Si el índice es negativo, ajusta para volver a la semana pasada
        if (startDayIndex < 0) {
            startDayIndex += 7;
        }

        List<QueryDocumentSnapshot> documentsThisWeek = new ArrayList<>();
        for (QueryDocumentSnapshot document : documentsThisWeek) {
            // Obtiene la fecha del documento y extrae el día de la semana
            String userId = mAuth.getCurrentUser().getUid();
            String userIdPrefix = userId.substring(0, 4); // Obtener los primeros 4 caracteres del userId
            Calendar calendario = Calendar.getInstance();
            int year = calendario.get(Calendar.YEAR);
            int monthOfYear = calendario.get(Calendar.MONTH);
            int dayOfMonth = calendario.get(Calendar.DAY_OF_MONTH);
            String fecha = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);

            String dateString = document.getString("date"); // Asegúrate de tener el campo de fecha en tu documento
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            try {
                Date date = Date.valueOf(dateString);
                calendar.setTime(date);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                // Si el día de la semana está dentro del rango de domingo a sábado, agrega el documento
                if (dayOfWeek >= sundayIndex && dayOfWeek <= saturdayIndex) {
                    documentsThisWeek.add(document);
                }
            } catch (Exception e) {
                // Manejo de la excepción, podrías mostrar un mensaje de error o realizar alguna acción apropiada
                System.out.println(e);
                e.printStackTrace(); // Opcional: Puedes quitar esta línea si no deseas imprimir el stack trace
            }
        }

        ArrayList<BarEntry> entries = new ArrayList<>();

        // Aquí debes cargar los datos de calorías basales y consumidas por día en "entries"
        // Puedes utilizar un bucle para agregar cada entrada

        for (int i = sundayIndex; i <= saturdayIndex; i++) {
            List<QueryDocumentSnapshot> documentsForDay = new ArrayList<>();
            for (QueryDocumentSnapshot document : documentsThisWeek) {
                //SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                try {
                    String dateString = document.getString("date"); // Asegúrate de tener el campo de fecha en tu documento
                    Date date = Date.valueOf(dateString);
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    // Si el día de la semana coincide con "i", agrega el documento a documentsForDay
                    if (dayOfWeek == i) {
                        documentsForDay.add(document);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
                // Parsea la fecha del documento y obtén el día de la semana
                // Si el día de la semana coincide con "i", agrega el documento a documentsForDay
                int totalConsumidasDia = 0;
                // Calcula las calorías consumidas y las calorías basales para ese día
                for (QueryDocumentSnapshot doc : documentsForDay) {
                    // Parsea las calorías consumidas y las calorías basales desde los documentos y suma a los totales
                    String calConsumidasStr = doc.getString("calories");
                    int calConsumidasDia = Integer.parseInt(calConsumidasStr);
                    totalConsumidasDia += calConsumidasDia;
                }
                // Luego, calcula las calorías consumidas y las calorías basales para ese día
                entries.add(new BarEntry(i, totalConsumidasDia));
                entries.add(new BarEntry(i + 0.2f , calBal ));

            }
        }
        entries.add(new BarEntry(0,calBal));

        BarDataSet barDataSet = new BarDataSet(entries, "Calorías");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.4f);

        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.invalidate();
    }
}

