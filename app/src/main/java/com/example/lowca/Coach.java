package com.example.lowca;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lowca.Adaptadores.ListViewRutinasAdapter;
import com.example.lowca.Models.Rutinas;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Coach#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Coach extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Variables de entorno
    View vista;
    private ArrayList<Rutinas> listaRutinas = new ArrayList<>();
    ListViewRutinasAdapter listViewRutinasAdapter;
    ListView listViewRutinas;

    TextView txtAreas;
    TextView txtCalQuemadas;
    TextView txtDiaAsignado;
    TextView txtNombre;
    TextView txtNombreEje1;
    TextView txtNombreEje2;
    TextView txtNombreEje3;
    TextView txtRepeEje1;
    TextView txtRepeEje2;
    TextView txtRepeEje3;
    TextView txtSetsEje1;
    TextView txtSetsEje2;
    TextView txtSetsEje3;

    // TODO: Variables de conexion
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    public String userUid;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Coach() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Coach.
     */
    // TODO: Rename and change types and number of parameters
    public static Coach newInstance(String param1, String param2) {
        Coach fragment = new Coach();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_coach, container, false);
        listViewRutinas = vista.findViewById(R.id.listaRutinas);

        txtAreas = vista.findViewById(R.id.txtAreas);
        txtCalQuemadas = vista.findViewById(R.id.txtCalQuemadas);
        txtDiaAsignado = vista.findViewById(R.id.txtDiaAsignado);
        txtNombre = vista.findViewById(R.id.txtNombre);
        txtNombreEje1 = vista.findViewById(R.id.txtNombreEje1);
        txtNombreEje2 = vista.findViewById(R.id.txtNombreEje2);
        txtNombreEje3 = vista.findViewById(R.id.txtNombreEje3);
        txtRepeEje1 = vista.findViewById(R.id.txtRepeEje1);
        txtRepeEje2 = vista.findViewById(R.id.txtRepeEje2);
        txtRepeEje3 = vista.findViewById(R.id.txtRepeEje3);
        txtSetsEje1 = vista.findViewById(R.id.txtSetsEje1);
        txtSetsEje2 = vista.findViewById(R.id.txtSetsEje2);
        txtSetsEje3 = vista.findViewById(R.id.txtSetsEje3);

        inicializarFirebase();
        listarRutinas();
        listarRutinaElejida();

        listViewRutinas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                // Obtén el elemento seleccionado de la lista
                Rutinas selectedItem = (Rutinas) parent.getItemAtPosition(position);
                // Accede a las propiedades del elemento seleccionado y guárdalas en variables
                String areas = selectedItem.getAreas();
                String calQuemadas = selectedItem.getCalQuemadas();
                String diaAsignado = selectedItem.getDiaAsignado();
                String nombre = selectedItem.getNombre();
                String nombreEje1 = selectedItem.getNombreEje1();
                String nombreEje2 = selectedItem.getNombreEje2();
                String nombreEje3 = selectedItem.getNombreEje3();
                String repeEje1 = selectedItem.getRepeEje1();
                String repeEje2 = selectedItem.getRepeEje2();
                String repeEje3 = selectedItem.getRepeEje3();
                String setsEje1 = selectedItem.getSetsEje1();
                String setsEje2 = selectedItem.getSetsEje2();
                String setsEje3 = selectedItem.getSetsEje3();
                // Construye el mensaje para mostrar en el cuadro de diálogo
                builder.setMessage("¿Elejir rutina "+nombre+"?");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (nombre.equals(txtNombre.getText())){
                            AlertDialog.Builder builder3 = new AlertDialog.Builder(getContext());
                            builder3.setMessage("No puedes elejir una rutina que ya está asignada");
                            builder3.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {}
                            });
                            builder3.show();
                        }
                        else {
                            // Aquí puedes realizar acciones adicionales si el usuario hace clic en "Aceptar"
                            txtAreas.setText(areas);
                            txtCalQuemadas.setText(calQuemadas);
                            txtDiaAsignado.setText(diaAsignado);
                            txtNombre.setText(nombre);
                            txtNombreEje1.setText(nombreEje1);
                            txtNombreEje2.setText(nombreEje2);
                            txtNombreEje3.setText(nombreEje3);
                            txtRepeEje1.setText(repeEje1);
                            txtRepeEje2.setText(repeEje2);
                            txtRepeEje3.setText(repeEje3);
                            txtSetsEje1.setText(setsEje1);
                            txtSetsEje2.setText(setsEje2);
                            txtSetsEje3.setText(setsEje3);
                            //Se mandan a guardar los datoss de rutina
                            Map<String, Object> data = new HashMap<>();
                            data.put("areas", areas);
                            data.put("cal_quemadas", calQuemadas);
                            data.put("dia_asignado", diaAsignado);
                            data.put("nombre", nombre);
                            data.put("nombre_ejercicio1", nombreEje1);
                            data.put("nombre_ejercicio2", nombreEje2);
                            data.put("nombre_ejercicio3", nombreEje3);
                            data.put("repe_ejercicio1", repeEje1);
                            data.put("repe_ejercicio2", repeEje2);
                            data.put("repe_ejercicio3", repeEje3);
                            data.put("sets_ejercicio1", setsEje1);
                            data.put("sets_ejercicio2", setsEje2);
                            data.put("sets_ejercicio3", setsEje3);
                            // Agrega los datos a Firestore con el ID de documento personalizado
                            userUid = mAuth.getCurrentUser().getUid();
                            db.collection("rutina_elejida").document(userUid).set(data)
                                    .addOnSuccessListener(aVoid -> {
                                        // Éxito
                                        AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                                        builder2.setMessage("REGISTRADO");
                                        builder2.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {}
                                        });
                                        builder2.show();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Error
                                    });
                        }
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                builder.show();
            }
        });

        return vista;
    }

    private void inicializarFirebase(){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    private void listarRutinas(){
        try {
            CollectionReference rutinasRef = db.collection("rutina").document().getParent();
            rutinasRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    listaRutinas.clear(); // Limpiar la lista actual de alimentos
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Obtener los datos de cada documento y agregarlos a la lista
                        Rutinas rutina = document.toObject(Rutinas.class);
                        rutina.setAreas(document.getString("areas"));
                        rutina.setCalQuemadas(document.getString("cal_quemadas"));
                        rutina.setDiaAsignado(document.getString("dia_asignado"));
                        rutina.setNombre(document.getString("nombre"));
                        rutina.setNombreEje1(document.getString("nombre_ejercicio1"));
                        rutina.setNombreEje2(document.getString("nombre_ejercicio2"));
                        rutina.setNombreEje3(document.getString("nombre_ejercicio3"));
                        rutina.setRepeEje1(document.getString("repe_ejercicio1"));
                        rutina.setRepeEje2(document.getString("repe_ejercicio2"));
                        rutina.setRepeEje3(document.getString("repe_ejercicio3"));
                        rutina.setSetsEje1(document.getString("sets_ejercicio1"));
                        rutina.setSetsEje2(document.getString("sets_ejercicio2"));
                        rutina.setSetsEje3(document.getString("sets_ejercicio3"));

                        listaRutinas.add(rutina);
                    }
                    // Crear el adaptador para el ListView y asignarlo
                    listViewRutinasAdapter = new ListViewRutinasAdapter(getActivity(), listaRutinas);
                    listViewRutinas.setAdapter(listViewRutinasAdapter);
                } else {
                    // Manejar el caso de error
                    //System.out.println("Error en el momento de mostar las rutinas");
                    Toast.makeText(this.getContext(), "Error al mostrar rutinas", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            Toast.makeText(this.getContext(), "Error: "+e, Toast.LENGTH_SHORT).show();
            //System.out.println(e);
        }
    }

    private void listarRutinaElejida(){
        userUid = mAuth.getCurrentUser().getUid();
        try {
            DocumentReference documentReference = db.collection("rutina_elejida")
                    .document(userUid);
            // Obtiene los datos del documento
            documentReference.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // El documento existe, puedes obtener sus datos
                            String areas = documentSnapshot.getString("areas");
                            String calQuemadas = documentSnapshot.getString("cal_quemadas");
                            String diaAsignado = documentSnapshot.getString("dia_asignado");
                            String nombre = documentSnapshot.getString("nombre");
                            String nombreEje1 = documentSnapshot.getString("nombre_ejercicio1");
                            String nombreEje2 = documentSnapshot.getString("nombre_ejercicio2");
                            String nombreEje3 = documentSnapshot.getString("nombre_ejercicio3");
                            String repeEje1 = documentSnapshot.getString("repe_ejercicio1");
                            String repeEje2 = documentSnapshot.getString("repe_ejercicio2");
                            String repeEje3 = documentSnapshot.getString("repe_ejercicio3");
                            String setsEje1 = documentSnapshot.getString("sets_ejercicio1");
                            String setsEje2 = documentSnapshot.getString("sets_ejercicio2");
                            String setsEje3 = documentSnapshot.getString("sets_ejercicio3");

                            txtAreas.setText(areas);
                            txtCalQuemadas.setText(calQuemadas);
                            txtDiaAsignado.setText(diaAsignado);
                            txtNombre.setText(nombre);
                            txtNombreEje1.setText(nombreEje1);
                            txtNombreEje2.setText(nombreEje2);
                            txtNombreEje3.setText(nombreEje3);
                            txtRepeEje1.setText(repeEje1);
                            txtRepeEje2.setText(repeEje2);
                            txtRepeEje3.setText(repeEje3);
                            txtSetsEje1.setText(setsEje1);
                            txtSetsEje2.setText(setsEje2);
                            txtSetsEje3.setText(setsEje3);
                        } else {
                            txtAreas.setText("Elije una rutina");
                            txtCalQuemadas.setText("");
                            txtDiaAsignado.setText("");
                            txtNombre.setText("");
                            txtNombreEje1.setText("");
                            txtNombreEje2.setText("");
                            txtNombreEje3.setText("");
                            txtRepeEje1.setText("");
                            txtRepeEje2.setText("");
                            txtRepeEje3.setText("");
                            txtSetsEje1.setText("");
                            txtSetsEje2.setText("");
                            txtSetsEje3.setText("");
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Error al intentar obtener el documento
                    });
        } catch (Exception e){
            Toast.makeText(this.getContext(), "Error: "+e, Toast.LENGTH_SHORT).show();
            //System.out.println(e);
        }
    }
}