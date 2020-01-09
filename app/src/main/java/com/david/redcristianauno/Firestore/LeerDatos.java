package com.david.redcristianauno.Firestore;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.david.redcristianauno.POJOs.HistoricoSemanal;
import com.david.redcristianauno.POJOs.RegistroCelula;
import com.david.redcristianauno.POJOs.RegistroSubred;
import com.david.redcristianauno.POJOs.Usuario;
import com.david.redcristianauno.POJOs.Usuarios;
import com.david.redcristianauno.Preferences;
import com.david.redcristianauno.R;
import com.david.redcristianauno.adapters.adaptador_historico_semanal;
import com.david.redcristianauno.adapters.adaptador_permisos;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


public class LeerDatos {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference mDocRef;
    static Map<String, Object> datos;
    public ArrayList<Usuarios> lisDatos =  new ArrayList<>();

    public adaptador_permisos adaptador_permisos;

    public LeerDatos(){
    }

    //Obtengo solo los nombres de los estados para llenar el Spinner de Estados del RegisterActivity de manera ordenada
    public void leerColeccionEstados(final Spinner spEstado, final Context context){
        db.collection("estados").orderBy("nombre_estado")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    List<String> listaEstados = new ArrayList<>();
                    ArrayAdapter<String> arrayAdapterEstados;

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()){

                            for (QueryDocumentSnapshot document : task.getResult()){
                                datos = document.getData();
                                for (Map.Entry m : datos.entrySet()){
                                    listaEstados.add(String.valueOf(m.getValue()));
                                    arrayAdapterEstados = new ArrayAdapter<>(context, R.layout.spinner_item_david, listaEstados);
                                    spEstado.setAdapter(arrayAdapterEstados);
                                }
                            }
                        }else{
                            Log.d("NoQuery", "Error consigguiendo los documentos: ", task.getException());
                        }
                    }
                });
    }

    //Filtro el nombre de estado seleccionado para conseguir su id del ResgisterActivity
    public void leerEstados(final String nombre_estado, final Spinner spMunicipio, final Context context){
        db.collection("estados")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()){
                                datos = document.getData();
                                for (Map.Entry m : datos.entrySet()){
                                    if (nombre_estado.equals(String.valueOf(m.getValue()))){
                                        leerSubcoleccionMunicipios(document.getId(), spMunicipio, context);
                                    }
                                }
                            }
                        }
                    }
                });
    }

    //Muestro los municipios de manera ordenada consiguiendo el ID del estado y poniendolo en un Spinner del RegisterActivity
    public void leerSubcoleccionMunicipios(String id, final Spinner spMuncipio, final Context context){
        db.collection("estados").document(id).collection("Municipios").orderBy("nombre_municipio")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    List<String> listaMuncipios = new ArrayList<>();
                    ArrayAdapter<String> arrayAdapterMuncipios;
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                datos = document.getData();
                                for (Map.Entry m : datos.entrySet()){
                                    listaMuncipios.add(String.valueOf(m.getValue()));
                                    arrayAdapterMuncipios = new ArrayAdapter<>(context, R.layout.spinner_item_david, listaMuncipios);
                                    spMuncipio.setAdapter(arrayAdapterMuncipios);
                                }
                            }
                        }
                    }
                });
    }

    //Obtengo el nombre del usuario para PrincipalActivity
    public void datosSesion(final TextView nombre, final String correo,
                            final Menu menu, final NavigationView navigationView, final Context context){
        db.collection("usuarios")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                datos = document.getData();
                                Usuarios u = document.toObject(Usuarios.class);
                                for (Map.Entry m : datos.entrySet()){
                                    if(correo.equals(m.getValue())){
                                        nombre.setText(u.getNombre());
                                        visualiza(u.getTipo_permiso(), menu, navigationView, context);
                                    }
                                }
                            }
                        }
                    }
                });
    }

    //Visualizo o oculto los menus del Principal Activity
    private void visualiza(String tipo_permiso, Menu menu, NavigationView navigationView, Context context) {
        switch (tipo_permiso){
            case "Normal":
                menu = navigationView.getMenu();
                MenuItem visible = menu.findItem(R.id.nav_gallery);
                visible.setVisible(false);
                MenuItem visible2 = menu.findItem(R.id.nav_share);
                visible2.setVisible(false);
                MenuItem visible3 = menu.findItem(R.id.nav_slideshow);
                visible3.setVisible(false);
                MenuItem visible4 = menu.findItem(R.id.crear);
                visible4.setVisible(false);
                MenuItem visible5 = menu.findItem(R.id.noticias);
                visible5.setVisible(false);
                break;
            case "Lideres Celula":
                menu = navigationView.getMenu();
                MenuItem visible6 = menu.findItem(R.id.nav_gallery);
                visible6.setVisible(false);
                MenuItem visible7 = menu.findItem(R.id.nav_share);
                visible7.setVisible(false);
                MenuItem visible8 = menu.findItem(R.id.crear);
                visible8.setVisible(false);
                MenuItem visible9 = menu.findItem(R.id.noticias);
                visible9.setVisible(false);
                break;
            case "Subred":
                menu = navigationView.getMenu();
                MenuItem visible10 = menu.findItem(R.id.nav_share);
                visible10.setVisible(false);
                MenuItem visible11 = menu.findItem(R.id.crear);
                visible11.setVisible(false);
                MenuItem visible12 = menu.findItem(R.id.noticias);
                visible12.setVisible(false);
                break;
            case "Red":
                menu = navigationView.getMenu();
                MenuItem visible13 = menu.findItem(R.id.nav_share);
                visible13.setVisible(false);
                break;
            case "Super Usuario":
                Toast.makeText(context, "Super Usuario activo", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    //Guarda el Id del usuario en Preferences del LoginActivity
    public void preferencesUsuarios(final String email, final Context context){
        db.collection("usuarios")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                Usuarios u = document.toObject(Usuarios.class);
                                if(email.equals(u.getCorreo())){
                                    Log.d("Result", document.getId());
                                    Preferences.savePreferenceString(context, document.getId(), Preferences.PREFERENCES_ID_USUARIO);
                                }
                            }
                        }
                    }
                });
    }


    //Busco el usuario con el Id para crear un registro de celula en RegistroCelulaFragment
    public void leerUsuarioRegistroCelula(final RegistroCelula rc, final String id){
        mDocRef = db.collection("usuarios").document(id);
        mDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    private InsertarDatos inda = new InsertarDatos();
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();

                            if(documentSnapshot.exists()){
                                Usuarios u = documentSnapshot.toObject(Usuarios.class);

                                if (id.equals(documentSnapshot.getId())) {
                                    rc.setNombre_usuario(u.getNombre());

                                    inda.crearRegistroCelula(rc);
                                }else{
                                    Log.d("Result", "No se encontraron coincidencias con el ID");
                                }
                            }else{
                                Log.d("Result","No se encontro el documento");
                            }
                        }else{
                            Log.d("Result", "Fallo al encontrar la tarea asignada", task.getException());
                        }
                    }
                });
    }

    //Busco el usuario con el Id para crear un registro de Subred en RegistroSubredFragment
    public void leerUsuarioRegistroSubred(final RegistroSubred rs, final String id){
        mDocRef = db.collection("usuarios").document(id);
        mDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            private InsertarDatos inda = new InsertarDatos();
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();

                    if(documentSnapshot.exists()){
                        Usuarios u = documentSnapshot.toObject(Usuarios.class);

                        if (id.equals(documentSnapshot.getId())) {
                            rs.setNombre_usuario(u.getNombre());

                            inda.crearRegistroSubred(rs);
                        }else{
                            Log.d("Result", "No se encontraron coincidencias con el ID");
                        }
                    }else{
                        Log.d("Result","No se encontro el documento");
                    }
                }else{
                    Log.d("Result", "Fallo al encontrar la tarea asignada", task.getException());
                }
            }
        });
    }

    public void obtenerDatosColeccion(){
        db.collection("usuarios")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    Map<String, Object> Datos;
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()){
                            Datos = document.getData();
                            for (Map.Entry m : Datos.entrySet()){
                                Log.d("Result", String.valueOf(m.getKey()) + m.getValue());
                            }
                            Log.d("Query", document.getId() + " => " +  document.getData());
                        }
                    }else{
                        Log.d("NoQuery", "Error consigguiendo los documentos: ", task.getException());
                    }
                }
            });
    }
}
