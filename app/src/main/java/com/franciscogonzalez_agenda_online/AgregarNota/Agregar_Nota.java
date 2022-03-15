package com.franciscogonzalez_agenda_online.AgregarNota;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.franciscogonzalez_agenda_online.Objetos.Nota;
import com.franciscogonzalez_agenda_online.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Agregar_Nota extends AppCompatActivity {

    TextView Uid_Usuario, Correo_Usuario, Fecha_Hora_Actual, Fecha, Estado;
    EditText Titulo, Descripcion;

    Button Boton_Calendario, Boton_GuardarNota_BD;

    int dia, mes, anio;

    DatabaseReference BD_Firebase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_nota);

        //-------------Inicializacion Variables
        Uid_Usuario = findViewById(R.id.Uid_Usuario);
        Correo_Usuario = findViewById(R.id.Correo_Usuario);
        Fecha_Hora_Actual = findViewById(R.id.Fecha_Hora_Actual);
        Fecha = findViewById(R.id.Fecha);
        Estado = findViewById(R.id.Estado);

        Titulo = findViewById(R.id.Titulo);
        Descripcion = findViewById(R.id.Descripcion);
        Boton_Calendario= findViewById(R.id.Boton_Calendario);
        Boton_GuardarNota_BD = findViewById(R.id.Boton_GuardarNota_BD);


        BD_Firebase = FirebaseDatabase.getInstance().getReference();


        //-------------

        RecuperarDatos();
        Obtener_Fecha_Hora_Actual();


        //------------------------------Calendario al presionar el boton. (de Stackoverflow.com)
        Boton_Calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendario = Calendar.getInstance();
                dia = calendario.get(Calendar.DAY_OF_MONTH);
                mes = calendario.get(Calendar.MONTH);
                anio = calendario.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Agregar_Nota.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int AnioSeleccionado, int MesSeleccionado, int DiaSeleccionado) {

                        String diaFormateado, mesFormateado;

                        //OBTENER DIA
                        if (DiaSeleccionado < 10){
                            diaFormateado = "0"+String.valueOf(DiaSeleccionado);
                            // Antes: 9/11/2022 -  Ahora 09/11/2022
                        }else {
                            diaFormateado = String.valueOf(DiaSeleccionado);
                            //Ejemplo 13/08/2022
                        }

                        //OBTENER EL MES
                        int Mes = MesSeleccionado + 1;

                        if (Mes < 10){
                            mesFormateado = "0"+String.valueOf(Mes);
                            // Antes: 09/8/2022 -  Ahora 09/08/2022
                        }else {
                            mesFormateado = String.valueOf(Mes);
                            //Ejemplo 13/10/2022 - 13/11/2022 - 13/12/2022

                        }

                        //Setear fecha en TextView
                        Fecha.setText(diaFormateado + "/" + mesFormateado + "/"+ AnioSeleccionado);

                    }
                },anio,mes,dia);
                datePickerDialog.show();

            }
        });
        //------------------------------ Fin calendario

        Boton_GuardarNota_BD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AgregarNota();
            }
        });


    }

    // Obtener la fecha y hora actual del dispositivo (probar codigo que se encontró)
    private void Obtener_Fecha_Hora_Actual(){
        String Fecha_hora = new SimpleDateFormat("dd-MM-yyyy/HH:mm:ss a", Locale.getDefault()).format(System.currentTimeMillis());
        Fecha_Hora_Actual.setText(Fecha_hora);
    }

   private void AgregarNota(){

        //Obtener los datos
       String uid_usuario = Uid_Usuario.getText().toString();
       String correo_usuario = Correo_Usuario.getText().toString();
       String fecha_hora_actual = Fecha_Hora_Actual.getText().toString();
       String titulo = Titulo.getText().toString();
       String descripcion = Descripcion.getText().toString();
       String fecha = Fecha.getText().toString();
       String estado = Estado.getText().toString();

       //Validar los datos
       if (!uid_usuario.equals("") && !correo_usuario.equals("") && !fecha_hora_actual.equals("") && !titulo.equals("") && !descripcion.equals("") && !fecha.equals("") && !estado.equals("")){
           //Para el identificador de una nota (para guardarlo en la base de datos) se puede utilizar la fecha que siempre sera unica, sumado con el correo por ejemplo para que se entienda mejor
           Nota nota = new Nota(correo_usuario + "(" + fecha_hora_actual + ")", uid_usuario,correo_usuario,fecha_hora_actual,titulo,descripcion,fecha,estado);

           String nota_usuario = BD_Firebase.push().getKey();
           //Nombre para base de datos
           String Nombre_BD = "Notas Guardadas";

           BD_Firebase.child(Nombre_BD).child(nota_usuario).setValue(nota);
           Toast.makeText(Agregar_Nota.this, "La nota se ha agregado exitosamente", Toast.LENGTH_SHORT);

           onBackPressed(); //para que luego de guardar nos dirija al menu principal otra vez


       }
       else
       {
           Toast.makeText(Agregar_Nota.this, "Debe llenar todos los campos", Toast.LENGTH_SHORT);
       }


   }


    // Recupera los datos del usuario enviados en el Intent desde la actividad MenuPrincipal
    private void RecuperarDatos(){
        String uid_recuperado = getIntent().getStringExtra("Uid");
        String correo_recuperado= getIntent().getStringExtra("Correo");
        Uid_Usuario.setText(uid_recuperado);
        Correo_Usuario.setText(correo_recuperado);
    }
}