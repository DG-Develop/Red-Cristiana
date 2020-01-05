package com.david.redcristianauno;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.david.redcristianauno.Firestore.InsertarDatos;
import com.david.redcristianauno.POJOs.Estado;
import com.david.redcristianauno.POJOs.Municipio;
import com.david.redcristianauno.POJOs.Subred;
import com.david.redcristianauno.POJOs.Usuario;
import com.david.redcristianauno.POJOs.Usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {
    private EditText txtNombre, txtApellidoPaterno, txtApellidoMaterno,
            txtCorreo, txtContraseña,txtColonia,txtCalle,txtNum_ext,txtCodigo_postal, txtTelefono;
    private Spinner spEstado, spMunicipio, spSubred;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    private List<String> listaSubred = new ArrayList<String>();
    private ArrayAdapter<String> arrayAdapterSubred;
    private List<String> listaEstados = new ArrayList<String>();
    private ArrayAdapter<String> arrayAdapterEstados;
    private List<String> listaMunicipios = new ArrayList<String>();
    private ArrayAdapter<String> arrayAdapterMunicipios;

    private int id_estado, id_municipio;

    private String id_subred;

    private InsertarDatos inda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inicializarFirebase();
        firebaseAuth = FirebaseAuth.getInstance();


        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtApellidoPaterno = (EditText) findViewById(R.id.txtApellidoPaterno);
        txtApellidoMaterno = (EditText) findViewById(R.id.txtApellidoMaterno);
        txtCorreo = (EditText) findViewById(R.id.txtCorreo);
        txtContraseña = (EditText) findViewById(R.id.txtContraseña);
        spEstado = (Spinner) findViewById(R.id.spEstado);
        spMunicipio = (Spinner) findViewById(R.id.spMunicipio);
        txtColonia = (EditText) findViewById(R.id.txtColonia);
        txtCalle = (EditText) findViewById(R.id.txtCalle);
        txtNum_ext = (EditText) findViewById(R.id.txtnum_ext);
        txtCodigo_postal = (EditText) findViewById(R.id.txtCodigo_postal);
        txtTelefono = (EditText) findViewById(R.id.txtTelefono);
        spSubred = (Spinner) findViewById(R.id.spSubred);

        /*String[] Estados = {"Aguascalientes","Baja California","Baja California Sur","Campeche","Chiapas","Chihuahua"
        ,"Ciudad de México","Coahuila","Colima","Durango","Guanajuato","Guerrero","Hidalgo","Jalisco","Michoacán"
        ,"Morelos","Nayarit","Nuevo León","Oaxaca","Puebla","Querétaro","Quintana Roo","San Luis Potosí","Sinaloa"
        ,"Sonora","Tabasco","Tamaulipas","Tlaxcala","Veracruz","Yucatán","Zacatecas"};*/

        listarEstados();

        spEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String nombre_estado = spEstado.getSelectedItem().toString();

                verEstados(nombre_estado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listarSubred();

        spSubred.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String nombre_subred = spSubred.getSelectedItem().toString();

                verSubred(nombre_subred);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spMunicipio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String municipio = spMunicipio.getSelectedItem().toString();

                verMuncipio(municipio);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        progressDialog = new ProgressDialog(this);

    }

    private void verMuncipio (final String municipio){
        databaseReference.child("Municipio").orderByChild("nombre_municipio").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Municipio m = snapshot.getValue(Municipio.class);
                    String nombre_muncipio = m.getNombre_municipio();

                    if(nombre_muncipio.equals(municipio)){
                        setMunicipio(m.getId_municipio());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void verSubred(final String nombre_subred){
        databaseReference.child("Subred").orderByChild("nombre_subred").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Subred s = snapshot.getValue(Subred.class);
                    String subred = s.getNombre_subred();

                    if(subred.equals(nombre_subred)){
                        setId_subred(s.getId_subred());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void verEstados(final String nombre_estado) {
        databaseReference.child("Estado").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Estado m = snapshot.getValue(Estado.class);
                    String estado = m.getNombre_estado();

                    if(estado.equals(nombre_estado)){
                        llenarMunicipio(m.getId_estado());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void listarEstados(){
        databaseReference.child("Estado").orderByChild("nombre_municipio").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaEstados.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Estado e = snapshot.getValue(Estado.class);
                    String nombre_estado = e.getNombre_estado();
                    id_estado = e.getId_estado();
                    listaEstados.add(nombre_estado);

                    arrayAdapterEstados = new ArrayAdapter<String>(RegisterActivity.this, R.layout.spinner_item_david, listaEstados);
                    spEstado.setAdapter(arrayAdapterEstados);

                    llenarMunicipio(id_estado);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void listarSubred() {
        databaseReference.child("Subred").orderByChild("nombre_subred").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaSubred.clear();
                listaSubred.add("No tengo Subred");
                for (final DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Subred s = snapshot.getValue(Subred.class);
                    String nombre_subred = s.getNombre_subred();
                    listaSubred.add(nombre_subred);

                    arrayAdapterSubred = new ArrayAdapter<String>(RegisterActivity.this, R.layout.spinner_item_david,listaSubred);
                    spSubred.setAdapter(arrayAdapterSubred);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void llenarMunicipio(final int id_estado_municipio) {
        databaseReference.child("Municipio").orderByChild("nombre_municipio").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaMunicipios.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Municipio m = snapshot.getValue(Municipio.class);
                    id_estado = m.getId_estado();
                    String nombre_municipio = m.getNombre_municipio();

                    if(id_estado_municipio == id_estado){
                        setEstado(id_estado_municipio);
                        listaMunicipios.add(nombre_municipio);
                    }

                    arrayAdapterMunicipios = new ArrayAdapter<String>(RegisterActivity.this, R.layout.spinner_item_david, listaMunicipios);
                    spMunicipio.setAdapter(arrayAdapterMunicipios);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setMunicipio(int id_municipio) {
         this.id_municipio = id_municipio;
    }

    private void setEstado(int id_estado) {
        this.id_estado = id_estado;
    }

    public int getId_estado() {
        return id_estado;
    }

    public int getId_municipio() {
        return id_municipio;
    }

    public String getId_subred() {
        return id_subred;
    }

    public void setId_subred(String id_subred) {
        this.id_subred = id_subred;
    }

    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void registrarUsuarios(View view){
        if(txtNombre.getText().toString().isEmpty()
                || txtApellidoPaterno.getText().toString().isEmpty()
                || txtApellidoMaterno.getText().toString().isEmpty()
                || txtCorreo.getText().toString().isEmpty()
                || txtContraseña.getText().toString().isEmpty()
                || txtColonia.getText().toString().isEmpty()
                || txtCalle.getText().toString().isEmpty()
                || txtNum_ext.getText().toString().isEmpty()
                || txtCodigo_postal.getText().toString().isEmpty()
                || txtTelefono.getText().toString().isEmpty()){

            Toast.makeText(this, "Por favor rellene todos los datos", Toast.LENGTH_SHORT).show();
        }else{
            if(getId_estado() == 0 && getId_municipio() == 0){
                Toast.makeText(this, "Los municipios no estan", Toast.LENGTH_SHORT).show();
            }else{
                procesaUsuario();
            }
        }
    }

    private void crearUsuario() {

        Usuario user = new Usuario();
        user.setId_usuario(UUID.randomUUID().toString());
        user.setNombre( txtNombre.getText().toString().trim());
        user.setApellido_paterno(txtApellidoPaterno.getText().toString().trim());
        user.setApellido_materno(txtApellidoMaterno.getText().toString().trim());
        user.setCorreo(txtCorreo.getText().toString().trim());
        user.setContraseña(txtCorreo.getText().toString().trim());
        user.setId_estado(getId_estado());
        user.setId_municipio(getId_municipio());
        user.setColonia(txtColonia.getText().toString().trim());
        user.setCalle(txtCalle.getText().toString().trim());
        user.setNo_exterior(txtNum_ext.getText().toString().trim());
        user.setCodigo_postal(Integer.parseInt(txtCodigo_postal.getText().toString().trim()));
        user.setTelefono(txtTelefono.getText().toString().trim());
        user.setId_permiso(1);
        user.setId_subred(getId_subred());

        databaseReference.child("Usuario").child(user.getId_usuario()).setValue(user);
        Toast.makeText(this, "Se registro correctamente", Toast.LENGTH_SHORT).show();

        Intent  i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    void crear(){
        Usuarios u = new Usuarios(
                txtNombre.getText().toString().trim(),
                txtApellidoPaterno.getText().toString().trim(),
                txtApellidoMaterno.getText().toString().trim(),
                txtColonia.getText().toString().trim(),
                txtCalle.getText().toString().trim(),
                txtNum_ext.getText().toString().trim(),
                Integer.parseInt(txtCodigo_postal.getText().toString().trim()),
                txtTelefono.getText().toString().trim(),
                txtCorreo.getText().toString().trim(),
                txtCorreo.getText().toString().trim(),
                "Normal"
        );

        inda = new InsertarDatos();

        inda.crearUsuario(u);

        Toast.makeText(this, "Se registro correctamente", Toast.LENGTH_SHORT).show();

        Intent  i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void procesaUsuario() {
            if(txtContraseña.getText().toString().trim().length() < 6){
                Toast.makeText(this, "Introduzca un contraseña mas grande", Toast.LENGTH_SHORT).show();
            }else{
                progressDialog.setMessage("Realizando Consulta...");
                progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(txtCorreo.getText().toString().trim(),txtContraseña.getText().toString().trim()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //crearUsuario();
                            crear();
                        }else{
                            Toast.makeText(RegisterActivity.this, "No se pudo crear usuarioprivado", Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(RegisterActivity.this, "Este correo ya esta en uso", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(RegisterActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

    }
}
