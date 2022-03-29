package com.franciscogonzalez_agenda_online.ListarNotas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.franciscogonzalez_agenda_online.Objetos.Nota;
import com.franciscogonzalez_agenda_online.R;
import com.franciscogonzalez_agenda_online.ViewHolder.ViewHolder_Nota;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Listar_Notas extends AppCompatActivity {

    RecyclerView recyclerViewNotas;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference Base_de_Datos;

    //Esto para poder modificar la forma de listar las notas
    LinearLayoutManager linearLayoutManager;

    // Con esto se monitorean los eventos de la base de datos, atentos a posibles cambios en las notas. Es decir, se queda escuchando las notas en la base de datos.
    FirebaseRecyclerAdapter<Nota, ViewHolder_Nota>  firebaseRecyclerAdapter;
    FirebaseRecyclerOptions options;

    Dialog dialog;


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

        dialog = new Dialog(Listar_Notas.this);


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
                        //Toast.makeText(Listar_Notas.this, "on item long click", Toast.LENGTH_SHORT).show();

                        String id_nota = getItem(position).getId();    //el getId() de Nota
                        //Declarar vista eliminar
                        Button Eliminar;

                        dialog.setContentView(R.layout.eliminar_layout);

                        Eliminar = dialog.findViewById(R.id.Eliminar);

                        Eliminar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                EliminarNota(id_nota);
                                dialog.dismiss();
                            }
                        });
                        dialog.show();

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

    private void EliminarNota(String id_nota) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Listar_Notas.this);
        builder.setTitle("Eliminar nota");
        builder.setMessage("¿Desea eliminar la nota?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Eliminar nota en BD
                Query query = Base_de_Datos.orderByChild("id").equalTo(id_nota);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toast.makeText(Listar_Notas.this, "Nota eliminada", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Listar_Notas.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Listar_Notas.this, "Cancelado por el usuario", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
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
