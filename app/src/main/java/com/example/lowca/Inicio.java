package com.example.lowca;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.Tasks;

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
import java.util.Set;
import java.util.TreeSet;

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
        obtenerDietaAsignada();


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
                        tvCaloriasBasales.setText(String.valueOf(calBal)+" Kcal");

                        //Obtener la fecha actual
                        hoy = System.currentTimeMillis();

                        Date fechaActual = new Date(hoy);

                        // Crea un formato para mostrar la fecha
                        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

                        // Convierte la fecha actual al formato deseado
                        String fechaFormateada = formatoFecha.format(fechaActual);

                        // Imprime la fecha formateada
                        System.out.println("Fecha actual: " + fechaFormateada);

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
                            //int basales=(int) mb;
                            //String cal= String.valueOf(mb);




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
        cargarYMostrarGraficoBarras(barChart);


        return vista;
        //return inflater.inflate(R.layout.fragment_inicio, container, false);




    }

    private void actualizarProgresoConsumidas(int caloriasConsumidas, double caloriasBasales) {
        int progress = (int) ((caloriasConsumidas * 100) / caloriasBasales);
        progressBarConsumidas.setProgress(progress);
    }
  
    public void obtenerDietaAsignada(){
        DocumentReference userDocRef = db.collection("dieta_asignada").document(userUid);

        userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                       String calorias= document.getString("calorias");

                        tvCaloriasDieta.setText(calorias);

                    } else {
                        // El documento no existe
                        Log.d("Firestore", "El documento no existe");
                    }
                } else {
                    // Error al obtener el documento
                    Log.e("Firestore", "Error al obtener el documento", task.getException());
                }
            }
        });




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
    private void cargarYMostrarGraficoBarras(BarChart barChart) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        java.util.Date currentDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, -6);
        java.util.Date lastWeek = calendar.getTime();

        Map<String, Integer> caloriasPorDia = new HashMap<>();

        CollectionReference eatCollectionRef = db.collection("eat");
        String userId = mAuth.getCurrentUser().getUid();
        String userIdPrefix = userId.substring(0, 4);

        eatCollectionRef.whereGreaterThanOrEqualTo(FieldPath.documentId(), userIdPrefix)
                .whereLessThan(FieldPath.documentId(), userIdPrefix + "\uf8ff")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String fecha = document.getString("date");
                        String caloriasString = document.getString("calories");
                        int calorias = Integer.parseInt(caloriasString);
                        try {
                            java.util.Date date = dateFormat.parse(fecha);
                            if (date.after(lastWeek) && date.before(currentDate)) {
                                // Si la fecha está en el rango, suma las calorías
                                if (caloriasPorDia.containsKey(fecha)) {
                                    int caloriasExistente = caloriasPorDia.get(fecha);
                                    caloriasPorDia.put(fecha, caloriasExistente + calorias);
                                } else {
                                    caloriasPorDia.put(fecha, calorias);
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    ArrayList<BarEntry> entries = new ArrayList<>();
                    ArrayList<String> labels = new ArrayList<>();
                    int index = 0;
                    for (Map.Entry<String, Integer> entry : caloriasPorDia.entrySet()) {
                        String fecha = entry.getKey();
                        int calorias = entry.getValue();
                        entries.add(new BarEntry(index, calorias));
                        labels.add(fecha);
                        index++;
                    }

                        BarDataSet barDataSet = new BarDataSet(entries, "Calorías");
                        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                        barDataSet.setValueTextSize(12f);

                        BarData barData = new BarData(barDataSet);
                        barData.setBarWidth(0.8f);

                        ViewGroup.LayoutParams layoutParams = barChart.getLayoutParams();
                        layoutParams.height = 800;
  
                        barChart.setLayoutParams(layoutParams);
                        barChart.setData(barData);
                        barChart.setFitBars(true);
                        barChart.invalidate();

                        XAxis xAxis = barChart.getXAxis();
                        xAxis.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {
                                int intValue = (int) value;
                                if (intValue >= 0 && intValue < labels.size()) {
                                    String fecha = labels.get(intValue);
                                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM", Locale.getDefault());
                                    try {
                                        java.util.Date date = inputFormat.parse(fecha);
                                        return outputFormat.format(date);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                                return "";
                            }
                        });
                    })
                    .addOnFailureListener(e -> {
                        System.out.println("error al graficar con barras" +e);
                    });
    }
}

