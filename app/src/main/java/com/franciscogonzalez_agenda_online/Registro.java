package com.franciscogonzalez_agenda_online;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registro extends AppCompatActivity {

    EditText Nombre_editar, Correo_editar,Password_editar,ConfirmarPassword_editar;
    Button RegistrarUsuario;
    TextView TengoCuenta_txt;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    String nombre = "", correo = "", password = "" , confirmarPassword = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("Registrar");

        // para dejarle al usuario retroceder
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //-----------------Inicialización
        Nombre_editar = findViewById((R.id.Nombre_editar));
        Correo_editar = findViewById((R.id.Correo_editar));
        Password_editar = findViewById((R.id.Password_editar));
        ConfirmarPassword_editar = findViewById((R.id.ConfirmarPassword_editar));
        RegistrarUsuario = findViewById((R.id.RegistrarUsuario));
        TengoCuenta_txt = findViewById((R.id.TengoCuenta_txt));

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Registro.this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);  //Con esto no finalizará si el usuario presione en cualquier otra parte, sino que lo hará al finalizar.

        //----------------Eventos para el boton de "Registrar" y para el text field de "Ya tengo una cuenta"

        RegistrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidarDatos();
            }
        });

        TengoCuenta_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Registro.this, Login.class));
            }
        });

    }

    private void ValidarDatos(){
        nombre = Nombre_editar.getText().toString();
        correo = Correo_editar.getText().toString();
        password = Password_editar.getText().toString();
        confirmarPassword = ConfirmarPassword_editar.getText().toString();

        if (TextUtils.isEmpty(nombre)){
            Toast.makeText(this, "Ingrese nombre", Toast.LENGTH_SHORT).show();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Ingrese correo", Toast.LENGTH_SHORT).show();
        }
            else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Ingrese la contraseña", Toast.LENGTH_SHORT).show();
        }
                else if(TextUtils.isEmpty(confirmarPassword)) {
                    Toast.makeText(this, "Confirme la contraseña", Toast.LENGTH_SHORT).show();
                }
                    else if (!password.equals(confirmarPassword)){
                            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            CrearCuenta();
                        }
            }

    private void CrearCuenta() {
        progressDialog.setMessage("Creando su cuenta...");
        progressDialog.show();

        //Creacion de usuario en Firebase
        firebaseAuth.createUserWithEmailAndPassword(correo, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        GuardarInformacion();

                    }
                }).addOnFailureListener(new OnFailureListener() {  //en caso de fallo
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Registro.this, ""+ e.getCause().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void GuardarInformacion() {
        progressDialog.setMessage("Guardando su información");
        progressDialog.dismiss();

        //Obtener identificacion de usuario actual
        String uid = firebaseAuth.getUid();  // se obtiene un id del usuario actual

        HashMap<String,String> Datos = new HashMap<>();
        Datos.put("uid", uid);
        Datos.put("correo", correo);
        Datos.put("nombre", nombre);
        Datos.put("password", password);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios"); //nombre que tendra la base de datos (Usuarios)
        //va a listar los datos ordenador por uid
        databaseReference.child(uid).setValue(Datos)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(Registro.this, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Registro.this, MenuPrincipal.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Registro.this, ""+ e.getCause().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // si se presiona la flecha hacia atras se manda a la actividad anterior
        return super.onSupportNavigateUp();
    }
}




