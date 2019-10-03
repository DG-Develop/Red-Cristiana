package com.david.redcristianauno.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.david.redcristianauno.POJOs.Usuario;
import com.david.redcristianauno.R;
import com.david.redcristianauno.interfaces.ItemClickListener;

import java.util.ArrayList;

public class adaptador_permisos extends RecyclerView.Adapter<adaptador_permisos.ViewHolderDatos>{

    Context c;
    ArrayList<Usuario> lisDatos;
    public ArrayList<Usuario> checkedDatos = new ArrayList<>();

    public adaptador_permisos(Context c, ArrayList<Usuario> lisDatos) {
        this.c = c;
        this.lisDatos = lisDatos;
    }

    @NonNull
    @Override
    public adaptador_permisos.ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.usuarios,null,false);

        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int i) {
        holder.tv_usuarios.setText(lisDatos.get(i).getNombre());
        holder.tv_correo.setText(lisDatos.get(i).getCorreo());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, int posicion) {
                CheckBox ch = (CheckBox) view;

                if(ch.isChecked()){
                    checkedDatos.add(lisDatos.get(posicion));
                }else if(!ch.isChecked()){
                    checkedDatos.remove(lisDatos.get(posicion));
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return lisDatos.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_usuarios, tv_correo;
        CheckBox checkusuarios;


        ItemClickListener itemClickListener;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);

            tv_usuarios = (TextView) itemView.findViewById(R.id.recycler_usuarios);
            tv_correo = (TextView) itemView.findViewById(R.id.recycler_correos);
            checkusuarios = (CheckBox) itemView.findViewById(R.id.checkUsuarios);

            checkusuarios.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener ic){
            this.itemClickListener = ic;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v,getLayoutPosition());
        }


    }
}
