package com.david.redcristianauno.FragmentsSuperusuario;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.david.redcristianauno.POJOs.Usuario;
import com.david.redcristianauno.R;
import com.david.redcristianauno.adapters.adaptador_usuarios;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class UserGeneralFragment extends Fragment {
    private RecyclerView rc;
    private ArrayList<Usuario> lisDatos;
    private com.david.redcristianauno.adapters.adaptador_usuarios adaptador_usuarios;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FloatingActionButton fb;

    private StringBuffer sb= null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_user_general, container, false);

        rc = (RecyclerView) view.findViewById(R.id.rcList);
        rc.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL,false));
        fb = (FloatingActionButton) view.findViewById(R.id.fbgeneraluser);

        inicializarFirebase();
        lisDatos = new ArrayList<>();
        listarUsuarios();

        adaptador_usuarios = new adaptador_usuarios(getContext(),listarUsuarios());
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb = new StringBuffer();

                for(Usuario u : adaptador_usuarios.checkedDatos){
                    sb.append(u.getNombre());
                    sb.append("\n");
                }
                if(adaptador_usuarios.checkedDatos.size()>0){
                    Toast.makeText(getContext(), sb.toString(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Porfavor seleccione algo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(getActivity());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public ArrayList<Usuario> listarUsuarios(){

        databaseReference.child("Usuario").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lisDatos.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Usuario u = snapshot.getValue(Usuario.class);
                    String nombre = u.getNombre();
                    String correo = u.getCorreo();

                    lisDatos.add(new Usuario(nombre, correo));

                    adaptador_usuarios = new adaptador_usuarios(getContext(),lisDatos);
                    rc.setAdapter(adaptador_usuarios);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return lisDatos;
    }

}
