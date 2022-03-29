package com.franciscogonzalez_agenda_online.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.franciscogonzalez_agenda_online.R;


public class ViewHolder_Nota extends RecyclerView.ViewHolder {

    View mView;
    private ViewHolder_Nota.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view, int position); /* Se ejecuta al presionar el item*/
        void onItemLongClick(View view, int position);/* Se ejecuta al mantener presionado el item*/
    }



    public void SetOnClickListener(ViewHolder_Nota.ClickListener clickListener){
        mClickListener = clickListener;
    }

    public ViewHolder_Nota(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        /* Eventos*/

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getAdapterPosition());
                return false;
            }
        });
    }


    public void SetearDatos(Context context ,String id_nota, String uid_usuario, String correo_usuario, String fecha_hora_registro, String titulo, String descripcion, String fecha_nota, String estado){


        //Declarar las vistas
        TextView Id_nota_Item,Uid_Usuario_Item, Correo_Usuario_Item, Fecha_Hora_Actual_Item, Titulo_Item,Descripcion_Item,Estado_Item, Fecha_Item;

        //Establecer conexion con el item
        Id_nota_Item = mView.findViewById(R.id.Id_nota_Item);
        Uid_Usuario_Item = mView.findViewById(R.id.Uid_Usuario_Item);
        Correo_Usuario_Item = mView.findViewById(R.id.Correo_Usuario_Item);
        Fecha_Hora_Actual_Item = mView.findViewById(R.id.Fecha_Hora_Actual_Item);
        Titulo_Item = mView.findViewById(R.id.Titulo_Item);
        Descripcion_Item= mView.findViewById(R.id.Descripcion_Item);
        Estado_Item = mView.findViewById(R.id.Estado_Item);
        Fecha_Item = mView.findViewById(R.id.Fecha_Item);

        //Setear info dentro del item
        Id_nota_Item.setText(id_nota);
        Uid_Usuario_Item.setText(uid_usuario);
        Correo_Usuario_Item.setText(correo_usuario);
        Fecha_Hora_Actual_Item.setText(fecha_hora_registro);
        Titulo_Item.setText(titulo);
        Descripcion_Item.setText(descripcion);
        Estado_Item.setText(estado);
        Fecha_Item.setText(fecha_nota);






    }
}
