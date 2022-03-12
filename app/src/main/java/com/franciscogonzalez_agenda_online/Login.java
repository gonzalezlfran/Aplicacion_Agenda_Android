package com.franciscogonzalez_agenda_online;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText CorreoLogin, PasswordLogin;
    Button boton_logeo;
    TextView NuevoUsuario_txt;

    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;

    String correo = "", password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("Login");

        // para dejarle al usuario retroceder
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        CorreoLogin = findViewById(R.id.CorreoLogin);
        PasswordLogin = findViewById(R.id.PasswordLogin);
        boton_logeo = findViewById(R.id.boton_logeo);
        NuevoUsuario_txt = findViewById(R.id.UsuarioNuevo_txt);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);
        
        boton_logeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidarDatos();
            }
        });

        NuevoUsuario_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Registro.class));
            }
        });
    }




    private void ValidarDatos() {
        correo = CorreoLogin.getText().toString();
        password = PasswordLogin.getText().toString();

        //chequea que lo ingresado tenga un '@' y finalice en '.com'
        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            Toast.makeText(this, "Correo inválido", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Ingrese la contraseña", Toast.LENGTH_SHORT).show();
        }
            else{
                LogearUsuario();
        }
    }

    private void LogearUsuario() {
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(correo,password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    startActivity(new Intent(Login.this, MenuPrincipal.class));
                    Toast.makeText(Login.this, "Bienvenido(a): "+ user.getEmail(), Toast.LENGTH_SHORT).show();

                    finish();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, "Verifique que el correo y contraseña sean correctos", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this,"" + e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // si se presiona la flecha hacia atras se manda a la actividad anterior
        return super.onSupportNavigateUp();
    }
}