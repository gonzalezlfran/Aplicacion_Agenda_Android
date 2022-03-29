package com.franciscogonzalez_agenda_online.ListarNotas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.franciscogonzalez_agenda_online.Objetos.Nota;
import com.franciscogonzalez_agenda_online.R;
import com.franciscogonzalez_agenda_online.ViewHolder.ViewHolder_Nota;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

public class Listar_Notas extends AppCompatActivity {

    RecyclerView recyclerViewNotas;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference Base_de_Datos;

    //Esto para poder modificar la forma de listar las notas
    LinearLayoutManager linearLayoutManager;

    // Con esto se monitorean los eventos de la base de datos, atentos a posibles cambios en las notas. Es decir, se queda escuchando las notas en la base de datos.
    FirebaseRecyclerAdapter<Nota, ViewHolder_Nota>  firebaseRecyclerAdapter;
    FirebaseRecyclerOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_notas);

        //Action Bar
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Mis Notas");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);



        //Inicializacion

        recyclerViewNotas = findViewById(R.id.recyclerViewNotas);
        recyclerViewNotas.setHasFixedSize(true); // para que el recyclerViewNotas adapte su tamano a las listas que se posean.

        firebaseDatabase = FirebaseDatabase.getInstance();
        Base_de_Datos = firebaseDatabase.getReference("Notas Guardadas");

        ListarNotasUsuarios();

    }

    private void ListarNotasUsuarios(){
        options = new FirebaseRecyclerOptions.Builder<Nota>().setQuery(Base_de_Datos, Nota.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Nota, ViewHolder_Nota>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Nota viewHolder_nota, int position, @NonNull Nota nota) {
                viewHolder_nota.SetearDatos(getApplicationContext(),nota.getId(), nota.getUid_usuario(), nota.getCorreo_usuario(), nota.getFecha_hora_actual(), nota.getTitulo(), nota.getDescripcion(), nota.getFecha_nota(), nota.getEstado());
            }

            @NonNull
            @Override
            public ViewHolder_Nota onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota,parent,false);
                ViewHolder_Nota viewHolder_nota = new ViewHolder_Nota(view);
                viewHolder_nota.SetOnClickListener(new ViewHolder_Nota.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(Listar_Notas.this, "on item click", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toast.makeText(Listar_Notas.this, "on item long click", Toast.LENGTH_SHORT).show();

                    }
                });
                return viewHolder_nota;
            }
        };

        linearLayoutManager = new LinearLayoutManager(Listar_Notas.this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true); //para que se liste desde el ultimo registro al primero
        linearLayoutManager.setStackFromEnd(true);

        recyclerViewNotas.setLayoutManager(linearLayoutManager);
        recyclerViewNotas.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter != null){
            firebaseRecyclerAdapter.startListening();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
