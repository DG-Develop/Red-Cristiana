package com.david.redcristianauno;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.david.redcristianauno.POJOs.Celula;
import com.david.redcristianauno.POJOs.Subred;
import com.david.redcristianauno.POJOs.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class SubredFragment extends Fragment {

    private EditText txtCrearSubred, txtCrearCelula;
    private Button btnCrearSubred, btnCrearCelula;
    private Spinner spinnerlistsubred;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<String> listaSubred = new ArrayList<String>();
    private ArrayAdapter<String> arrayAdapterSubred;

    public static String correo_usuario = "";

    public String id_usuario;
    public String id_subred;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subred, container, false);

        txtCrearSubred = view.findViewById(R.id.txtCrearSubred);
        txtCrearCelula = view.findViewById(R.id.txtCrearCelula);
        btnCrearSubred = view.findViewById(R.id.btncrearSubred);
        btnCrearCelula = view.findViewById(R.id.btncrearCelula);
        spinnerlistsubred = view.findViewById(R.id.spinnerlistsubred);

        inicializarFirebase();

        listarSubred();

        correo_usuario = Preferences.obtenerPreferencesString(getActivity(), Preferences.PREFERENCES_USUARIO_LOGIN);

        verUsuario(correo_usuario);

        btnCrearSubred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtCrearSubred.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Por favor escriba un nombre", Toast.LENGTH_SHORT).show();
                }else{
                    //verUsuario(correo_usuario);
                    existeSubred(txtCrearSubred.getText().toString().trim());
                }

            }
        });



        btnCrearCelula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verSubred(spinnerlistsubred.getSelectedItem().toString());

                if(txtCrearCelula.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Por favor escriba un nombre", Toast.LENGTH_SHORT).show();
                }else{
                    //verUsuario(correo_usuario);

                    existeCelula(txtCrearCelula.getText().toString().trim());
                }

            }
        });

        return view;
    }


    private void existeCelula(final String nombre_celula) {
        databaseReference.child("Celula").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean bandera = true;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Celula sr = snapshot.getValue(Celula.class);

                    if(nombre_celula.equals(sr.getNombre_celula())){
                        bandera = false;
                        Toast.makeText(getActivity(), "Ya existe una subred con ese nombre", Toast.LENGTH_SHORT).show();
                    }
                }
                if (bandera){
                    registrarCelula();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void registrarCelula() {
        Calendar c = Calendar.getInstance();
        c.get(Calendar.DAY_OF_MONTH);
        c.get(Calendar.MONTH);
        c.get(Calendar.YEAR);
        String fecha = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        Celula cl = new Celula();

        cl.setId_celula(UUID.randomUUID().toString());
        cl.setNombre_celula(txtCrearCelula.getText().toString().trim());
        cl.setId_subred(getId_subred());
        cl.setId_usuario(getId_usuario());
        cl.setFecha(fecha);

        databaseReference.child("Celula").child(cl.getId_celula()).setValue(cl);
        Toast.makeText(getActivity(), "Se agrego correctamente", Toast.LENGTH_SHORT).show();

    }

    public String getId_subred() {
        return id_subred;
    }

    public void setId_subred(String id_subred) {
        this.id_subred = id_subred;
    }

    private void verSubred(final String nombre_subred) {
        databaseReference.child("Subred").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Subred sr = snapshot.getValue(Subred.class);

                    if(nombre_subred.equals(sr.getNombre_subred())){
                        setId_subred(sr.getId_subred());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void existeSubred(final String nombre_subred){
        databaseReference.child("Subred").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean bandera = true;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Subred sr = snapshot.getValue(Subred.class);

                    if(nombre_subred.equals(sr.getNombre_subred())) {
                        bandera = false;
                        Toast.makeText(getActivity(), "Ya existe una subred con ese nombre", Toast.LENGTH_SHORT).show();
                    }
                }
                if (bandera){
                    registrarSubred();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public void verUsuario(final String correo_usuario){
        databaseReference.child("Usuario").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Usuario u = snapshot.getValue(Usuario.class);

                    if(correo_usuario.equals(u.getCorreo())){
                        setId_usuario(u.getId_usuario());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void registrarSubred(){
        Calendar c = Calendar.getInstance();
        c.get(Calendar.DAY_OF_MONTH);
        c.get(Calendar.MONTH);
        c.get(Calendar.YEAR);
        String fecha = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        Subred sr = new Subred();

        sr.setId_subred(UUID.randomUUID().toString());
        sr.setNombre_subred(txtCrearSubred.getText().toString().trim());
        sr.setId_red(" ");
        sr.setId_usuario(getId_usuario());
        sr.setFecha(fecha);

        databaseReference.child("Subred").child(sr.getId_subred()).setValue(sr);
        Toast.makeText(getActivity(), "Se agrego correctamente", Toast.LENGTH_SHORT).show();
    }

    public void listarSubred(){
        databaseReference.child("Subred").orderByChild("nombre_subred").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaSubred.clear();
                for (final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    databaseReference.child("Subred").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Subred s = snapshot.getValue(Subred.class);
                            String nombre_subred = s.getNombre_subred();
                            listaSubred.add(nombre_subred);

                            arrayAdapterSubred = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_david,listaSubred);
                            spinnerlistsubred.setAdapter(arrayAdapterSubred);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    public void inicializarFirebase(){
        FirebaseApp.initializeApp(getActivity());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }
}
