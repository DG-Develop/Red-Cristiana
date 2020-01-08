package com.david.redcristianauno.Historico;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.david.redcristianauno.POJOs.HistoricoSemanal;
import com.david.redcristianauno.R;
import com.david.redcristianauno.adapters.adaptador_historico_semanal;
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


public class HistoricoSemanalFragment extends Fragment {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<HistoricoSemanal> listDatos;

    private RecyclerView rc;
    private adaptador_historico_semanal adaptador_historico_semanal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historico_semanal, container, false);

        rc = view.findViewById(R.id.rcDatosSemanales);
        rc.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));

        listDatos = new ArrayList<>();

        inicializarFirebase();

        //listarSemanal();

        crearLista();

        return view;
    }

    private void listarSemanal() {
        databaseReference.child("Historico").orderByChild("id_historico").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listDatos.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    HistoricoSemanal hs = snapshot.getValue(HistoricoSemanal.class);

                    System.out.println("Ninos " +  hs.getTotal_ninos());
                    System.out.println("Ofrenda " +  hs.getTotal_ofrenda());
                    listDatos.add(new HistoricoSemanal(hs.getId_historico(),hs.getTotal_asistencia(),hs.getTotal_invitados(),
                            hs.getTotal_ninos(),hs.getTotal_ofrenda(),hs.getFecha()));
                }

                adaptador_historico_semanal = new adaptador_historico_semanal(getContext(), listDatos);
                rc.setAdapter(adaptador_historico_semanal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Hace la busqueda de todos los historicos semanales y los muestra en un ReciclerView
    private void crearLista(){
        db.collection("Historico Celulas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot docunment : task.getResult()){
                                HistoricoSemanal hs = docunment.toObject(HistoricoSemanal.class);
                                listDatos.add(new HistoricoSemanal(hs.getTotal_asistencia(),hs.getTotal_invitados(),
                                        hs.getTotal_ninos(), hs.getTotal_ofrenda(), hs.getFecha()));
                            }
                            adaptador_historico_semanal = new adaptador_historico_semanal(getContext(), listDatos);
                            rc.setAdapter(adaptador_historico_semanal);
                        }
                    }
                });
    }

    public void inicializarFirebase(){
        FirebaseApp.initializeApp(getActivity());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }


}
