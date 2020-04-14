package com.david.redcristianauno.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.david.redcristianauno.POJOs.HistoricoSemanal;
import com.david.redcristianauno.R;

import java.util.ArrayList;

public class adaptador_historico_semanal  extends RecyclerView.Adapter<adaptador_historico_semanal.ViewHolderDatos>  {

    Context c;
    ArrayList<HistoricoSemanal> listregistro;

    public adaptador_historico_semanal(Context c, ArrayList<HistoricoSemanal> listregistro) {
        this.c = c;
        this.listregistro = listregistro;
    }

    @NonNull
    @Override
    public adaptador_historico_semanal.ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_card_semanal,null,false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adaptador_historico_semanal.ViewHolderDatos holder, int i) {
        holder.txtFecha.setText(listregistro.get(i).getFecha());
        holder.txtAsistencia.setText("Total asistencia: " + String.valueOf(listregistro.get(i).getTotal_asistencia()));
        holder.txtInvitados.setText("Total invitados: " + String.valueOf(listregistro.get(i).getTotal_invitados()));
        holder.txtNinos.setText("Total niños: " + String.valueOf(listregistro.get(i).getTotal_ninos()));
        holder.txtOfrenda.setText("Total: " + String.valueOf(listregistro.get(i).getTotal_ofrenda()));

    }

    @Override
    public int getItemCount() {
        return listregistro.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView txtFecha, txtAsistencia, txtInvitados, txtNinos, txtOfrenda;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);

            txtFecha = itemView.findViewById(R.id.fecha_semanal);
            txtAsistencia = itemView.findViewById(R.id.rc_txt_asistencia_semanal);
            txtInvitados = itemView.findViewById(R.id.rc_txt_invitados_semanal);
            txtNinos = itemView.findViewById(R.id.rc_txt_niños_semanal);
            txtOfrenda = itemView.findViewById(R.id.rc_txt_ofrenda_semanal);
        }
    }
}
