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
import com.david.redcristianauno.POJOs.Usuarios;
import com.david.redcristianauno.R;
import com.david.redcristianauno.adapters.adaptador_usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class UserGeneralFragment extends Fragment {
    private RecyclerView rc;
    private ArrayList<Usuarios> lisDatos;
    private adaptador_usuarios adaptador_usuarios;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FloatingActionButton fb;

    private StringBuffer sb= null;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        //listarUsuarios();
        crearListaUsuarios();

        adaptador_usuarios = new adaptador_usuarios(getContext(),crearListaUsuarios());
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb = new StringBuffer();

                for(Usuarios u : adaptador_usuarios.checkedDatos){
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

    public ArrayList<Usuarios> listarUsuarios(){

        databaseReference.child("Usuario").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lisDatos.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Usuario u = snapshot.getValue(Usuario.class);
                    String nombre = u.getNombre();
                    String correo = u.getCorreo();

                    //lisDatos.add(new Usuario(nombre, correo));

                    //daptador_usuarios = new adaptador_usuarios(getContext(),lisDatos);
                    rc.setAdapter(adaptador_usuarios);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return lisDatos;
    }

    public ArrayList<Usuarios> crearListaUsuarios(){
        db.collection("usuarios")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        lisDatos.clear();
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Usuarios u = document.toObject(Usuarios.class);
                                lisDatos.add(new Usuarios(u.getNombre(), u.getCorreo()));
                                adaptador_usuarios = new adaptador_usuarios(getContext(), lisDatos);
                                rc.setAdapter(adaptador_usuarios);
                            }
                        }

                    }
                });
        return  lisDatos;
    }


}
