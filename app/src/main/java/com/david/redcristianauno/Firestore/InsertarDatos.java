package com.david.redcristianauno.Firestore;

import com.david.redcristianauno.POJOs.Usuarios;
import com.google.firebase.firestore.FirebaseFirestore;

public class InsertarDatos {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public InsertarDatos(){

    }

    public void crearUsuario(Usuarios usuario){
        db.collection("usuarios").document().set(usuario);
    }

}
