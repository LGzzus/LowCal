package com.example.lowca;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class datos1 extends AppCompatActivity {
    TextInputLayout tilEstatura, tilPeso, tilFechaNa;
    TextInputEditText etPeso,etEstatura,etNacido;
    Button btnContinuar;
    String[] datos1;
    String[] datosDos;
    Bundle recibirDatos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos1);
        etPeso=(TextInputEditText) findViewById(R.id.etPeso);
        etEstatura=(TextInputEditText) findViewById(R.id.etEstatura);
        etNacido=(TextInputEditText) findViewById(R.id.etFechaNacido);
        tilEstatura = (TextInputLayout) findViewById(R.id.TILEstatura);
        tilPeso = (TextInputLayout) findViewById(R.id.TILPeso);
        tilFechaNa = (TextInputLayout) findViewById(R.id.TILFechaN);
        btnContinuar = (Button) findViewById(R.id.btnSiguiente);
        recibirDatos= getIntent().getExtras();
        //Establece que la alturo solo debe ser max de 3 cifras
        InputFilter lengthFilter = new InputFilter.LengthFilter(3);
        etEstatura.setFilters(new InputFilter[]{lengthFilter});
        // Crea un InputFilter para limitar los decimales
        InputFilter decimalFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String inputText = dest.toString();
                String newText = inputText.substring(0, dstart) + source.subSequence(start, end) + inputText.substring(dend);

                // Verifica si el nuevo texto cumple con el formato de dos decimales después del punto
                if (!newText.matches("^\\d+(\\.\\d{0,2})?$")) {
                    return "";
                }

                return null; // Deja pasar el texto ingresado
            }
        };



// Agrega el InputFilter al TextInput
        etPeso.setFilters(new InputFilter[]{decimalFilter});

        btnContinuar.setOnClickListener(v -> {
            try {
                if(validar()){
                    datos1=recibirDatos.getStringArray("keyDatos");
                    String peso=etPeso.getText().toString();
                    String estatura=etEstatura.getText().toString();
                    String nacido=etNacido.getText().toString();
                    datosDos= new String[]{peso,estatura,nacido};
                    Bundle pasarDatos= new Bundle();
                    Bundle pasarDatos2= new Bundle();
                    pasarDatos.putStringArray("keyDatos",datos1);
                    pasarDatos2.putStringArray("keyDatos2",datosDos);

                    Intent intent = new Intent(this,datos2.class);
                    intent.putExtras(pasarDatos);
                    intent.putExtras(pasarDatos2);
                    startActivity(intent);
                }else{
                    Toast.makeText(this,"Llene todo",Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
            }
        });
        etNacido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }
    public void atras(View view){
        this.finish();
    }
    public boolean validar(){
        boolean retorno = true;
        String peso=etPeso.getText().toString();
        String estatura=etEstatura.getText().toString();
        String fechan=etNacido.getText().toString();
        if(peso.isEmpty()){
            tilPeso.setError("Ingresa tu Peso Actual");
        }else {
            tilPeso.setErrorEnabled(false);
        }
        if (estatura.isEmpty() ) {
            tilEstatura.setError("Ingresa tu Estatura");
        }else{
            tilEstatura.setErrorEnabled(false);
        }
        if (fechan.isEmpty()) {
            tilFechaNa.setError("Ingresa tu Fecha de Nacimiento");
            retorno = false;
        }else{
            tilFechaNa.setErrorEnabled(false);
        }
        return retorno;
    }
    public void openDialog(){
        DatePickerDialog fechaNacido = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                etNacido.setText(String.valueOf(day)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year));
            }
        }, 2023, 06, 03);
        fechaNacido.show();
    }
}