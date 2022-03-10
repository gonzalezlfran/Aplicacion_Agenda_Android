package com.franciscogonzalez_agenda_online;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Pantalla_Carga extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_carga);

        int tiempo = 3000;   // 3 segundos

        // El objeto Handler permite ejecutar lineas de codigo en el metodo run() despues de un tiempo determinado

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Pantalla_Carga.this, MainActivity.class));
                finish();    //se destruye al llegar al MainActivity
            }
        },tiempo);
    }
}