package com.david.redcristianauno.Historico;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.david.redcristianauno.Clases.HistoricoDatosDiariosCard;
import com.david.redcristianauno.DatePickerFragment;
import com.david.redcristianauno.POJOs.Celula;
import com.david.redcristianauno.POJOs.RegistroCelula;
import com.david.redcristianauno.POJOs.Subred;
import com.david.redcristianauno.POJOs.Usuario;
import com.david.redcristianauno.R;
import com.david.redcristianauno.adapters.adaptador_historico;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoricoDiarioFragment extends Fragment  implements DatePickerDialog.OnDateSetListener {
    public static EditText etFecha;
    private Button btnMostrarDia;
    private ImageView btnFecha;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private ArrayList<HistoricoDatosDiariosCard> listDatos;

    private HistoricoDatosDiariosCard hddc;

    private RecyclerView rc;
    private com.david.redcristianauno.adapters.adaptador_historico adaptador_historico;

    private String nombre_card;
    private String nombre_celula_card;
    private String nombre_sured_card;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historico_diario, container, false);

        inicializarFirebase();

        etFecha = (EditText) view.findViewById(R.id.txt_fecha_historico);
        btnMostrarDia = view.findViewById(R.id.btnbuscar);
        btnFecha = (ImageView) view.findViewById(R.id.ib_fecha_historico);

        rc = (RecyclerView) view.findViewById(R.id.rcDatosDiarios);
        rc.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));

        hddc = new HistoricoDatosDiariosCard();

        listDatos = new ArrayList<>();
        etFecha.setFocusable(false);


        btnMostrarDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mostrarLista(etFecha.getText().toString());

            }
        });

        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFechaHistorico();

            }
        });

        return view;
    }

    public void verRegistro(final String fecha){
        databaseReference.child("Registro Celula").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listDatos.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RegistroCelula rgc = snapshot.getValue(RegistroCelula.class);

                    if(fecha.equals(rgc.getFecha_celula())) {

                        hddc.setNombre_subred_card(getNombre_sured_card());
                        hddc.setAsistencia_card(rgc.getAsistencia_celula());
                        hddc.setInvitados_card(rgc.getInvitados_celula());
                        hddc.setNinos_card(rgc.getNinos_celula());
                        hddc.setOfrenda_card(rgc.getOfrenda_celula());

                        verUsuario(rgc.getId_usuario(),hddc.getAsistencia_card(), hddc.getInvitados_card(), hddc.getNinos_card(),
                                hddc.getOfrenda_card(),rgc.getId_celula());

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public String getNombre_card() {
        return nombre_card;
    }

    public void setNombre_card(String nombre_card) {
        this.nombre_card = nombre_card;
    }


    public void verUsuario(final String id_usuario, final int asistencia, final int invitados, final int ninos, final double ofrenda,
                           final String id_celula){

        databaseReference.child("Usuario").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Usuario u = snapshot.getValue(Usuario.class);

                    if (id_usuario.equals(u.getId_usuario())){
                        setNombre_card(u.getNombre());
                        verSubred(u.getId_subred(), getNombre_card(), asistencia, invitados, ninos, ofrenda, id_celula);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public String getNombre_celula_card() {
        return nombre_celula_card;
    }

    public void setNombre_celula_card(String nombre_celula_card) {
        this.nombre_celula_card = nombre_celula_card;
    }

    public void verCelula(final String id_celula, final String nombre, final int asistencia, final int invitados, final int ninos, final double ofrenda,
                          final String nombre_subred){
        databaseReference.child("Celula").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Celula c = snapshot.getValue(Celula.class);

                    if(id_celula.equals(c.getId_celula())){
                        setNombre_celula_card(c.getNombre_celula());


                        listDatos.add(new HistoricoDatosDiariosCard(nombre, asistencia, invitados,
                                ninos, ofrenda, getNombre_celula_card(), nombre_subred));

                    }
                }
                adaptador_historico = new adaptador_historico(getContext(),listDatos);
                rc.setAdapter(adaptador_historico);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public String getNombre_sured_card() {
        return nombre_sured_card;
    }

    public void setNombre_sured_card(String nombre_sured_card) {
        this.nombre_sured_card = nombre_sured_card;
    }

    public void verSubred(final String id_subred, final String nombre, final int asistencia, final int invitados, final int ninos, final double ofrenda,
                          final String id_celula){
        databaseReference.child("Subred").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Subred sr = snapshot.getValue(Subred.class);

                    if(id_subred.equals(sr.getId_subred())){
                       setNombre_sured_card(sr.getNombre_subred());


                       verCelula(id_celula, nombre, asistencia,invitados, ninos, ofrenda,getNombre_sured_card());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void setFecha(String fecha){
        etFecha.setText(fecha);
    }

    public void mostrarLista(String fecha){
        verRegistro(fecha);
    }

    public void inicializarFirebase(){
        FirebaseApp.initializeApp(getActivity());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
    public void getFechaHistorico(){
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getActivity().getSupportFragmentManager(),"date picker");

        mostrarLista(etFecha.getText().toString());
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        etFecha.setFocusable(false);
    }

}
