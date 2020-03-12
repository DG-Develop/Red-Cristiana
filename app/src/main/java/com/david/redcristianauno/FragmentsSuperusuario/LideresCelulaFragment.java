package com.david.redcristianauno.FragmentsSuperusuario;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.david.redcristianauno.Firestore.ActualizaDatos;
import com.david.redcristianauno.POJOs.Usuario;
import com.david.redcristianauno.POJOs.Usuarios;
import com.david.redcristianauno.R;
import com.david.redcristianauno.adapters.adaptador_permisos;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.HashMap;
import java.util.Map;


public class LideresCelulaFragment extends Fragment {
    private RecyclerView rc;
    private ArrayList<Usuarios> lisDatos;
    private adaptador_permisos adaptador_permisos;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FloatingActionButton fbSubred,fbRed,fbSuperUser,fbNormal;
    private FloatingActionsMenu menuBotones;
    private SwipeRefreshLayout swipeRefreshLayout;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ActualizaDatos ad = new ActualizaDatos();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lideres_celula, container, false);
        rc = (RecyclerView) view.findViewById(R.id.rcListNormal);
        rc.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL,false));

        menuBotones = (FloatingActionsMenu) view.findViewById(R.id.grupo_fab);
        fbSubred = (FloatingActionButton) view.findViewById(R.id.fabSubred);
        fbNormal = (FloatingActionButton) view.findViewById(R.id.fabNormal);
        fbRed = (FloatingActionButton) view.findViewById(R.id.fabRed);
        fbSuperUser = (FloatingActionButton) view.findViewById(R.id.fabSuperUser);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.Swipe_normal);

        inicializarFirebase();
        lisDatos = new ArrayList<>();
        //l.crearListaUsuarios(lisDatos, getContext(), rc, "Lideres Celula");


        adaptador_permisos = new adaptador_permisos(getContext(),
                crearListaUsuarios("Lideres Celula"));

        fbNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaptador_permisos.checkedDatos.size()>0){
                    for(Usuarios p : adaptador_permisos.checkedDatos){
                        String correo =  p.getCorreo();
                        ad.actualizaPermiso(correo, "Normal");
                        crearListaUsuarios("Lideres Celula");
                    }
                }else{
                    Toast.makeText(getContext(), "Porfavor seleccione algo", Toast.LENGTH_SHORT).show();
                }
                menuBotones.collapse();
            }
        });

        fbSubred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adaptador_permisos.checkedDatos.size()>0){
                    for(Usuarios p : adaptador_permisos.checkedDatos){
                        String correo =  p.getCorreo();
                        ad.actualizaPermiso(correo, "Subred");
                        crearListaUsuarios("Lideres Celula");
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
                        crearListaUsuarios("Lideres Celula");
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
                        crearListaUsuarios("Lideres Celula");
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
                crearListaUsuarios("Lideres Celula");
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

    public void actualizarPermisos(final String correo, final int permiso){
        databaseReference.child("Usuario").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Usuario p = snapshot.getValue(Usuario.class);

                    Map<String, Object> permisoMap = new HashMap<>();
                    permisoMap.put("id_usuario",p.getId_usuario());
                    permisoMap.put("nombre",p.getNombre());
                    permisoMap.put("apellido_paterno",p.getApellido_paterno());
                    permisoMap.put("apellido_materno",p.getApellido_materno());
                    permisoMap.put("correo",p.getCorreo());
                    permisoMap.put("contraseña",p.getContraseña());
                    permisoMap.put("id_estado",p.getId_estado());
                    permisoMap.put("id_municipio",p.getId_municipio());
                    permisoMap.put("colonia",p.getColonia());
                    permisoMap.put("calle",p.getCalle());
                    permisoMap.put("no_exterior",p.getNo_exterior());
                    permisoMap.put("codigo_postal",p.getCodigo_postal());
                    permisoMap.put("telefono",p.getTelefono());
                    permisoMap.put("id_permiso",permiso);
                    permisoMap.put("id_subred",p.getId_subred());



                    String correoPermiso = p.getCorreo();


                    if(correo.equals(correoPermiso)){
                        databaseReference.child("Usuario").child(p.getId_usuario()).updateChildren(permisoMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Snackbar.make(getView(),"Los datos se han actualizado correctamente", Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(getView(),"Hubo error al tratar de actualzar datos", Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
