package com.example.lowca.perfil.Controller;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.lowca.model.DatosAnt;
import com.example.lowca.perfil.Model.PerfilModel;
import com.example.lowca.perfil.Model.modelDieta;
import com.example.lowca.perfil.View.Perfil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PerfilController {
    private Perfil perfilF;
    public PerfilModel perfilModel;
    public modelDieta modelDieta;
    public FirebaseAuth mAuth;
    public String desea,userUID;
    DatosAnt datosA = new DatosAnt();

    public PerfilController(Perfil perfil) {
        this.perfilF = perfil;
        modelDieta = new modelDieta();
        perfilModel = new PerfilModel();

    }
    public void loadPerfilData(String UserUID){
        loadAccount(UserUID);
        loadUser(UserUID);
        loadDatesAnthropomethics(UserUID);
        extractDieta(UserUID);
        userUID = UserUID;
    }
    public void loadAccount(String UserUID){
        DocumentReference account = FirebaseFirestore.getInstance().collection("account").document(UserUID);
        account.addSnapshotListener((documentSnapshot,e)->{
            if(e != null){

            }
            if(documentSnapshot != null && documentSnapshot.exists()){
                String mail = documentSnapshot.getString("mail");
                perfilModel.setCorre(mail);
                perfilF.viewData(perfilModel);
            }
        });
    }
    public void loadUser(String UserUID){
        DocumentReference account = FirebaseFirestore.getInstance().collection("user").document(UserUID);
        account.addSnapshotListener((documentSnapshot,e)->{
            if(e != null){

            }
            if(documentSnapshot != null && documentSnapshot.exists()){
                String name = documentSnapshot.getString("name");
                perfilModel.setName(name);
                perfilF.viewData(perfilModel);
            }
        });
    }
    public void loadDatesAnthropomethics(String UserUID){
        DocumentReference account = FirebaseFirestore.getInstance().collection("antropometric_dates").document(UserUID);
        account.addSnapshotListener((documentSnapshot,e)->{
            if(e != null){

            }
            if(documentSnapshot != null && documentSnapshot.exists()){
                String FechaND = documentSnapshot.getString("birth_date");
                String GeneroD = documentSnapshot.getString("gender");
                String EstaturaD = String.valueOf(documentSnapshot.getLong("height"));
                String PesoAD = String.valueOf(documentSnapshot.getLong("weight").floatValue());
                String PesoOD = String.valueOf(documentSnapshot.getLong("target_weight"));
                String ActividadD = documentSnapshot.getString("physical_activity_lever");
                perfilModel.setBirthDate(FechaND);
                perfilModel.setGender(GeneroD);
                perfilModel.setHeight(EstaturaD);
                perfilModel.setWeight(PesoAD);
                perfilModel.setPhysical_activity(ActividadD);
                perfilModel.setTargetWeight(PesoOD);
                perfilF.viewData(perfilModel);
            }
        });
    }
    public void editarPerfil(String gender, String phyAct){
        String[] generos={"Mujer","Hombre"};
        List<String> genderList = Arrays.asList(generos);
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(perfilF.main,android.R.layout.simple_spinner_item,
                generos);
        perfilF.spinnerGen.setAdapter(adapter);

        int positiongender = genderList.indexOf(gender);
        perfilF.spinnerGen.setSelection(positiongender);

        String[] opciones={"Sedentaria","Moderada","Activa"};
        List<String> actList = Arrays.asList(opciones);
        ArrayAdapter<String> adapter2= new ArrayAdapter<String>(perfilF.main,android.R.layout.simple_spinner_item,
                opciones);
        perfilF.spinnerAct.setAdapter(adapter2);

        int positionAct = actList.indexOf(phyAct);
        perfilF.spinnerAct.setSelection(positionAct);

        perfilF.editarDatos.setVisibility(View.INVISIBLE);
        perfilF.guardarDatos.setVisibility(View.VISIBLE);
        perfilF.etGenero.setVisibility(View.INVISIBLE);
        perfilF.spinnerGen.setVisibility(View.VISIBLE);
        perfilF.etActividadF.setVisibility(View.INVISIBLE);
        perfilF.spinnerAct.setVisibility(View.VISIBLE);
        perfilF.etFechan.setEnabled(true);
        perfilF.etEstatura.setEnabled(true);
        perfilF.etPesoA.setEnabled(true);
        perfilF.etPesoO.setEnabled(true);
    }
    public void saveDataNew (String userUID){
        try {
            if(validar()){
                DatosAnt datosA = new DatosAnt();
                datosA.setFechaN(perfilF.etFechan.getText().toString());
                datosA.setGenero(perfilF.spinnerGen.getSelectedItem().toString());
                datosA.setEstatura(Integer.parseInt(perfilF.etEstatura.getText().toString()));
                datosA.setPesoO(Float.parseFloat((perfilF.etPesoO.getText().toString())));
                Float pesoA = Float.parseFloat(perfilF.etPesoA.getText().toString());
                datosA.setActividadF(perfilF.spinnerAct.getSelectedItem().toString());
                Map<String, Object> dat = new HashMap<>();
                dat.put("birth_date",datosA.getFechaN());
                dat.put("gender", datosA.getGenero());
                dat.put("height", datosA.getEstatura());
                dat.put("weight",pesoA);
                dat.put("physical_activity_lever",datosA.getActividadF());
                dat.put("target_weight",datosA.getPesoO());

                FirebaseFirestore.getInstance().collection("antropometric_dates").document(userUID).update(dat);
                Toast.makeText(perfilF.main,"Actualizando",Toast.LENGTH_LONG).show();
                perfilF.editarDatos.setVisibility(View.VISIBLE);
                perfilF.guardarDatos.setVisibility(View.INVISIBLE);
                perfilF.etGenero.setVisibility(View.VISIBLE);
                perfilF.spinnerGen.setVisibility(View.INVISIBLE);
                perfilF.etActividadF.setVisibility(View.VISIBLE);
                perfilF.spinnerAct.setVisibility(View.INVISIBLE);
                perfilF.etFechan.setEnabled(false);
                perfilF.etGenero.setEnabled(false);
                perfilF.etEstatura.setEnabled(false);
                perfilF.etPesoA.setEnabled(false);
                perfilF.etActividadF.setEnabled(false);
                perfilF.etPesoO.setEnabled(false);
            }
        }catch (Exception e){

        }
    }
    public boolean validar(){
        boolean retorno = true;
        String FechaNac = datosA.getFechaN();
        String Genero = datosA.getGenero();
        String Estatura = perfilF.etEstatura.getText().toString();
        String PesoAc = perfilF.etPesoA.getText().toString();
        String ActividadFis = datosA.getActividadF();
        /*if(etFechan.getText()==null){
            etFechan.setError("Llena el campo");
            retorno = false;
        }*/
        if (Estatura.isEmpty()){
            perfilF.etEstatura.setError("Ingresa tu estatura correcta");
            retorno = false;
        }
        if (PesoAc.isEmpty()){
            perfilF.etPesoA.setError("Ingresa tu Peso correcto");
            retorno = false;
        }
        return retorno;
    }
    public void createPDF(){
        try {
            String carpeta = "/infos2";
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + carpeta;
            lowAndUp(perfilModel.getWeight(),perfilModel.getTargetWeight());
            File dir = new File(path);
            if(!dir.exists()){
                dir.mkdir();
                Toast.makeText(perfilF.main,"Carpeta Creada",Toast.LENGTH_SHORT).show();
            }
            // get Date of day
            Date fecha = new Date();
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String salida = df.format(fecha);
            File archivo = new File(dir, "Info_Calorias.pdf");
            FileOutputStream fos = new FileOutputStream(archivo);

            Document document = new Document();
            PdfWriter.getInstance(document, fos);

            document.open();
            //document title
            Paragraph titulo = new Paragraph("Informe de alimentacion \n\n", FontFactory.getFont("Arial",22, Font.BOLD, BaseColor.BLACK));
            titulo.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(titulo);
            //Date
            Paragraph date = new Paragraph("Fecha: "+salida);
            date.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(date);
            //user
            Paragraph data = new Paragraph("Nombre: "+perfilModel.getName()+"\n\n" +
                    "Edad: "+perfilModel.getBirthDate()+
                    "\n\nPeso Actual: "+perfilModel.getWeight()+
                    "\n\nPeso objetivo: "+perfilModel.getTargetWeight()+
                    "\n\nRequiere: "+desea+"\n\n");
            document.add(data);
            //table
            PdfPTable table = new PdfPTable(2);
            table.addCell("");
            table.addCell("Tipo de dieta: " + modelDieta.getTipo());
            document.add(table);

            //table information diet
            PdfPTable tableDiet = new PdfPTable(3);
            tableDiet.addCell("Desayuno: "+modelDieta.getDesayuno());
            tableDiet.addCell("Comida: "+modelDieta.getAlmuerzo());
            tableDiet.addCell("Cena: "+modelDieta.getCena());
            document.add(tableDiet);

            //table totalcalories
            PdfPTable tableTotalCalories = new PdfPTable(1);

            PdfPCell cell = new PdfPCell();
            Paragraph paragraph = new Paragraph("Total de calorias: "+modelDieta.getTotal());
            paragraph.setAlignment(Element.ALIGN_RIGHT);
            cell.addElement(paragraph);
            tableTotalCalories.addCell(cell
            );
            document.add(tableTotalCalories);

            document.close();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void extractDieta(String userUID){
        DocumentReference dieta = FirebaseFirestore.getInstance().collection("dieta_asignada").document(userUID);
        dieta.addSnapshotListener((documentSnapshot,e)->{
            if(e != null){

            }
            if(documentSnapshot != null && documentSnapshot.exists()){
                String num = documentSnapshot.getString("id_dieta");
                String type = documentSnapshot.getString("tipo");
                modelDieta.setNumero(num);
                modelDieta.setTipo(type);
                extracInfoDiet();
            }
        });
    }
    public void extracInfoDiet(){
        DocumentReference dietainfo = FirebaseFirestore.getInstance().collection("dieta").document(modelDieta.getNumero());
        dietainfo.addSnapshotListener((documentSnapshot,e)->{
            if(e != null){

            }
            if(documentSnapshot != null && documentSnapshot.exists()){
                modelDieta.setAlmuerzo(documentSnapshot.getString("almuerzo"));
                modelDieta.setDesayuno(documentSnapshot.getString("desayuno"));
                modelDieta.setCena(documentSnapshot.getString("cena"));
                modelDieta.setTotal(String.valueOf(documentSnapshot.getLong("calorias").floatValue()));

            }
        });
    }
    public void lowAndUp(String pesoA,String pesoO){
        double pesoAct = Float.parseFloat(pesoA);
        double pesoObj = Float.parseFloat(pesoO);
        if(pesoAct > pesoObj)desea="Bajar de peso";
        else desea="Subir de peso";
        Log.d("dieta",""+modelDieta.getNumero());
    }

}
