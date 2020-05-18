package com.david.redcristianauno.presentation.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.david.redcristianauno.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CreateConfigurationDialogFragment extends Fragment {

    private EditText txtCrearSubred, txtCrearCelula, txtCrearRed;
    private Button btnCrearSubred, btnCrearCelula, btnCrearRed, btnCrearTodo;
    private Spinner spinnerlistsubred;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<String> listaSubred = new ArrayList<String>();
    private ArrayAdapter<String> arrayAdapterSubred;

    public static String correo_usuario = "";

    public String id_usuario;
    public String id_subred;

   // private InsertarDatos inda = new InsertarDatos();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_configuration_dialog, container, false);

        txtCrearSubred = view.findViewById(R.id.txtCrearSubred);
        txtCrearCelula = view.findViewById(R.id.txtCrearCelula);
        txtCrearRed = view.findViewById(R.id.txtCrearRed);
        btnCrearSubred = view.findViewById(R.id.btncrearSubred);
        btnCrearCelula = view.findViewById(R.id.btncrearCelula);
        btnCrearRed = view.findViewById(R.id.btncrearRed);
        btnCrearTodo = view.findViewById(R.id.btnCrearTodo);
        spinnerlistsubred = view.findViewById(R.id.spinnerlistsubred);

        inicializarFirebase();

        listarSubred();



        btnCrearRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtCrearRed.getText().toString().trim().isEmpty()){
                    Toast.makeText(getContext(),"Por favor escriba un nombre", Toast.LENGTH_SHORT).show();
                }else{
                    Calendar c = Calendar.getInstance();
                    c.get(Calendar.DAY_OF_MONTH);
                    c.get(Calendar.MONTH);
                    c.get(Calendar.YEAR);
                    String fecha = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());


                }
            }
        });

        btnCrearSubred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtCrearSubred.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Por favor escriba un nombre", Toast.LENGTH_SHORT).show();
                }else{
                    Calendar c = Calendar.getInstance();
                    c.get(Calendar.DAY_OF_MONTH);
                    c.get(Calendar.MONTH);
                    c.get(Calendar.YEAR);
                    String fecha = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());


                }

            }
        });



        btnCrearCelula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtCrearCelula.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Por favor escriba un nombre", Toast.LENGTH_SHORT).show();
                }else{

                    Calendar c = Calendar.getInstance();
                    c.get(Calendar.DAY_OF_MONTH);
                    c.get(Calendar.MONTH);
                    c.get(Calendar.YEAR);
                    String fecha = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());


                }

            }
        });

        return view;
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
                            //Subred s = snapshot.getValue(Subred.class);
                            //String nombre_subred = s.getNombre_subred();
                            //listaSubred.add(nombre_subred);

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
