package com.david.redcristianauno.FragmentsSuperusuario;


import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.david.redcristianauno.Firestore.ActualizaDatos;
import com.david.redcristianauno.POJOs.Usuarios;
import com.david.redcristianauno.R;
import com.david.redcristianauno.adapters.adaptador_permisos;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class UserSuberdFragment extends Fragment {
    private RecyclerView rc;
    private ArrayList<Usuarios> lisDatos;
    private com.david.redcristianauno.adapters.adaptador_permisos adaptador_permisos;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FloatingActionButton fbNormal, fbRed,fbSuperUser, fbLiderCelula;
    private FloatingActionsMenu menuBotones;
    private SwipeRefreshLayout swipeRefreshLayout;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ActualizaDatos ad = new ActualizaDatos();

    public void onStart() {
        super.onStart();
        crearListaUsuarios("Subred");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_suberd, container, false);

        rc = (RecyclerView) view.findViewById(R.id.rcListSubredUser);
        rc.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));

        menuBotones = (FloatingActionsMenu) view.findViewById(R.id.grupo_fab);
        fbNormal = (FloatingActionButton) view.findViewById(R.id.fabNormal);
        fbLiderCelula = (FloatingActionButton) view.findViewById(R.id.fabLiderCelula);
        fbRed = (FloatingActionButton) view.findViewById(R.id.fabRed);
        fbSuperUser = (FloatingActionButton) view.findViewById(R.id.fabSuperUser);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.Swipe_subred);

        inicializarFirebase();
        lisDatos = new ArrayList<>();
        //l.crearListaUsuarios(lisDatos, getContext(), rc, "Subred");


        adaptador_permisos = new adaptador_permisos(getContext(),
                crearListaUsuarios("Subred"));

        fbNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaptador_permisos.checkedDatos.size()>0){
                    for(Usuarios p : adaptador_permisos.checkedDatos){
                        String correo =  p.getCorreo();
                        ad.actualizaPermiso(correo, "Normal");
                        crearListaUsuarios("Subred");
                    }
                }else{
                    Toast.makeText(getContext(), "Porfavor seleccione algo", Toast.LENGTH_SHORT).show();
                }
                menuBotones.collapse();
            }
        });
        fbLiderCelula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaptador_permisos.checkedDatos.size()>0){
                    for(Usuarios p : adaptador_permisos.checkedDatos){
                        String correo =  p.getCorreo();
                        ad.actualizaPermiso(correo, "Lideres Celula");
                        crearListaUsuarios("Subred");
                    }
                }else{
                    Toast.makeText(getContext(), "Porfavor seleccione algo", Toast.LENGTH_SHORT).show();
                }
                menuBotones.collapse();
            }
        });

        fbRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaptador_permisos.checkedDatos.size()>0){
                    for(Usuarios p : adaptador_permisos.checkedDatos){
                        String correo =  p.getCorreo();
                        ad.actualizaPermiso(correo, "Red");
                        crearListaUsuarios("Subred");
                    }
                }else{
                    Toast.makeText(getContext(), "Porfavor seleccione algo", Toast.LENGTH_SHORT).show();
                }
                menuBotones.collapse();
            }
        });

        fbSuperUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaptador_permisos.checkedDatos.size()>0){
                    for(Usuarios p : adaptador_permisos.checkedDatos){
                        String correo =  p.getCorreo();
                        ad.actualizaPermiso(correo, "Super Usuario");
                        crearListaUsuarios("Subred");
                    }
                }else{
                    Toast.makeText(getContext(), "Porfavor seleccione algo", Toast.LENGTH_SHORT).show();
                }
                menuBotones.collapse();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                crearListaUsuarios("Subred");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },4000);
            }
        });

        return view;
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(getActivity());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public ArrayList<Usuarios> crearListaUsuarios(final String tipo_permiso){
        db.collection("usuarios")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        lisDatos.clear();
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Usuarios u = document.toObject(Usuarios.class);

                                if (u.getTipo_permiso().equals(tipo_permiso)){
                                    Log.d("Result", u.getNombre());
                                    lisDatos.add(new Usuarios(u.getNombre(), u.getCorreo()));
                                    adaptador_permisos = new adaptador_permisos(getContext(),lisDatos);
                                    rc.setAdapter(adaptador_permisos);
                                }

                            }
                        }
                    }
                });
        return lisDatos;
    }

}
