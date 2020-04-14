package com.david.redcristianauno.Firestore;


import android.util.Log;

import androidx.annotation.NonNull;

import com.david.redcristianauno.POJOs.Usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ActualizaDatos {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference mDocRef;

    //Actualizo los permisos con la busqueda del correo en las Fragment de la carpeta FragmentsSuperusuario
    public void actualizaPermiso(final String correo, final String permiso){
        db.collection("usuarios")
        .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Usuarios u = document.toObject(Usuarios.class);
                        if (correo.equals(u.getCorreo())){
                            Log.d("Result", document.getId());
                            mDocRef= db.collection("usuarios").document(document.getId());

                                mDocRef
                                    .update("tipo_permiso", permiso)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("Result", "DocumentSnapshot successfully updated!");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("Result", "Error updating document", e);
                                    }
                            });
                        }
                    }
                }
            }
        });

    }

    /*public void actualizar(ViewPager viewPager, SeccionesAdapter adapter){
        viewPager.setOffscreenPageLimit(2);
        UserGeneralFragment secc1;
        NormalFragment secc2;
        LideresCelulaFragment secc3;
        UserSuberdFragment secc4;
        UserRedFragment secc5;
        SuperUserFragment secc6;

        if (adapter.getItem(0)!=null){
            secc1 = (UserGeneralFragment) adapter.getItem(0);
        }
        if (adapter.getItem(1)!=null){
            secc2 = (NormalFragment) adapter.getItem(0);
        }
        if (adapter.getItem(2)!=null){
            secc3 = (LideresCelulaFragment) adapter.getItem(0);
        }
        if (adapter.getItem(3)!=null){
            secc4 = (UserSuberdFragment) adapter.getItem(0);
        }
        if (adapter.getItem(4)!=null){
            secc5 = (UserRedFragment) adapter.getItem(0);
        }
        if (adapter.getItem(5)!=null){
            secc6 = (SuperUserFragment) adapter.getItem(0);
        }
    }*/
}
