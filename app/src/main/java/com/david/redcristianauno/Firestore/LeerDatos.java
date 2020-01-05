package com.david.redcristianauno.Firestore;

import com.david.redcristianauno.POJOs.Estado;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class LeerDatos {
    private DocumentReference mDocRef;

    public LeerDatos(){
    }

    public void leerEstados(){
        mDocRef  = FirebaseFirestore.getInstance().document("estados");
        mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists()){
                    Estado e = documentSnapshot.toObject(Estado.class);

                }
            }
        });
    }
}
