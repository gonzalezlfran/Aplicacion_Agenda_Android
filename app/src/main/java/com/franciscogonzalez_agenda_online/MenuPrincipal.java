package com.franciscogonzalez_agenda_online;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.franciscogonzalez_agenda_online.AgregarNota.Agregar_Nota;
import com.franciscogonzalez_agenda_online.ListarNotas.Listar_Notas;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuPrincipal extends AppCompatActivity {

    Button AgregarNota, ListarNotas,CerrarSesion;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    TextView Uid_Usuario, NombrePrincipal, CorreoPrincipal;

    DatabaseReference Usuarios;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Agenda Online");

        Uid_Usuario = findViewById(R.id.Uid_Usuario);
        NombrePrincipal = findViewById(R.id.NombrePrincipal);
        CorreoPrincipal = findViewById(R.id.CorreoPrincipal);

        Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");

        AgregarNota = findViewById(R.id.AgregarNota);
        ListarNotas = findViewById(R.id.ListarNotas);
        CerrarSesion = findViewById(R.id.CerrarSesion);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();


        //Botones
        AgregarNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Pasarle los datos del usuario a la vista de AgregarNota (Por medio del intent)
                String uid = Uid_Usuario.getText().toString();
                String correo= CorreoPrincipal.getText().toString();
                Intent intent = new Intent(MenuPrincipal.this, Agregar_Nota.class);
                intent.putExtra("Uid", uid);
                intent.putExtra("Correo", correo);
                startActivity(intent);

                Toast.makeText(MenuPrincipal.this, "Agregar Nota", Toast.LENGTH_SHORT).show();
            }
        });

        ListarNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuPrincipal.this, Listar_Notas.class));
                Toast.makeText(MenuPrincipal.this, "Listar Notas", Toast.LENGTH_SHORT).show();
            }
        });


        CargaDatos();
        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SalirAplicacion();
            }
        });
    }

    private void ComprobarInicioSesion(){
        if(user != null){
            CargaDatos();
        }
        else{
            //Si no inició sesion lo manda a la pantalla principal
            startActivity( new Intent(MenuPrincipal.this, MainActivity.class));
            finish();
        }
    }


    @Override
    protected void onStart() {
        ComprobarInicioSesion();
        super.onStart();
    }

    private void CargaDatos(){
        Usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Si el usuario existe
                if (snapshot.exists()){
                    String uid = ""+ snapshot.child("uid").getValue();
                    String nombre = "" + snapshot.child("nombre").getValue();  //Sin las comillas anteriores no funciona, porque seguramente lo que traiga no es un String
                    String correo = "" +snapshot.child("correo").getValue();

                    Uid_Usuario.setText(uid);
                    NombrePrincipal.setText(nombre);
                    CorreoPrincipal.setText(correo);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void SalirAplicacion() {
        firebaseAuth.signOut();
        startActivity(new Intent(MenuPrincipal.this, MainActivity.class));
        Toast.makeText(this, "Cerraste sesion con éxito", Toast.LENGTH_SHORT).show();

    }
}