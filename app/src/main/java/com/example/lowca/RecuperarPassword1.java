package com.example.lowca;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarPassword1 extends AppCompatActivity {

    EditText etCorreo;
    String email="";
    private FirebaseAuth mAuth;
    private ProgressDialog mDialog;
    TextInputLayout tlCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_password1);
        etCorreo=(EditText) findViewById(R.id.etCorreoPass);
        tlCorreo = (TextInputLayout) findViewById(R.id.TIL4);
        mAuth=FirebaseAuth.getInstance();
        mDialog= new ProgressDialog(this);

    }
    public void enviarComprobante(View view){
        email=etCorreo.getText().toString();
        if(!email.isEmpty()){
            if(!email.contains("@")){
                tlCorreo.setError("Escribe tu correo correctamente");
            }else{
            mDialog.setMessage("Espere un momento...");
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
            resetPassword();
            }
        }
        else{

                    tlCorreo.setError("Llena este campo");
                    //Toast.makeText(RecuperarPassword1.this,"Ingrese su correo", Toast.LENGTH_LONG).show();


        }


    }
    private void resetPassword(){

        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){


                    Toast.makeText(RecuperarPassword1.this,"Corre enviado...", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(RecuperarPassword1.this,"No se pudo enviar el correo ", Toast.LENGTH_LONG).show();
                }
                mDialog.dismiss();
            }
        });

    }


    public void atras(View v) {
        Intent i=new Intent(this,IniciarSesion.class);
        startActivity(i);
        this.finish();
    }
    public  void aceptar(View view){
        boolean retorno = true;
        String correo=etCorreo.getText().toString();

        if (correo.isEmpty()){
            tlCorreo.setError("Llena este campo");
            retorno =false;
        }else{
            if(!email.contains("@")){
                tlCorreo.setError("Escribe tu correo correctamente");
            }else {

                tlCorreo.setErrorEnabled(false);
                Intent i = new Intent(this, IniciarSesion.class);
                startActivity(i);
                this.finish();
            }

        }


        //return retorno;
    }

}