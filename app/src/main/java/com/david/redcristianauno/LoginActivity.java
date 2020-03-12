package com.david.redcristianauno;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.david.redcristianauno.Firestore.LeerDatos;
import com.david.redcristianauno.POJOs.Usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {
    private EditText txtCorreo, txtPassword;
    private RadioButton rbSesion;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private boolean isActivatedRadioButton;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //private TextView txt_titulo;
    //private Typeface script;

    private LeerDatos l = new LeerDatos();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        if(Preferences.obtenerPreferencesBoolean(this,Preferences.PREFENCE_ESTADO_BUTTON_SESION)){
            Intent i = new Intent(LoginActivity.this, PrincipalActivity.class);
            startActivity(i);
            finish();
        }

        firebaseAuth = FirebaseAuth.getInstance();

        txtCorreo = (EditText) findViewById(R.id.txtCorreoLogin);
        txtPassword = (EditText) findViewById(R.id.txtPasswordLogin);
        rbSesion = (RadioButton) findViewById(R.id.sesion);
        //txt_titulo = findViewById(R.id.txt_titulo);

        //String fuente = "Fonts/OpenSans-BoldItalic.ttf";

        //this.script = Typeface.createFromAsset(getAssets(), fuente);
        //txt_titulo.setTypeface(script);

        progressDialog = new ProgressDialog(this);

        isActivatedRadioButton = rbSesion.isChecked();

        rbSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isActivatedRadioButton){
                    rbSesion.setChecked(false);
                }
                isActivatedRadioButton = rbSesion.isChecked();
            }
        });

        LeerDatos l = new LeerDatos();

    }

    public void ingresar(View view){
        final String email = txtCorreo.getText().toString().trim().toLowerCase();
        String password = txtPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Ingresa un usuario", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Ingresa una contraseña", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Realizando Consulta...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Preferences.savePreferenceBoolean(LoginActivity.this, rbSesion.isChecked(),Preferences.PREFENCE_ESTADO_BUTTON_SESION);
                    Preferences.savePreferenceString(LoginActivity.this,email,Preferences.PREFERENCES_USUARIO_LOGIN);
                    l.preferencesUsuarios(email, LoginActivity.this);

                    //buscarSubredUsuario(Preferences.obtenerPreferencesString(LoginActivity.this, Preferences.PREFERENCES_ID_USUARIO));
                    Intent i = new Intent(LoginActivity.this, PrincipalActivity.class);
                    startActivity(i);
                    finish();
                }
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof FirebaseAuthInvalidUserException){
                    Toast.makeText(LoginActivity.this, "Usuario Invalido", Toast.LENGTH_SHORT).show();
                    return;
                }else if(e instanceof  FirebaseAuthInvalidCredentialsException){
                    Toast.makeText(LoginActivity.this, "Contraseña invalida", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }

    public void registrar(View view){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    /*public void buscarSubredUsuario(final String id_usuario){
        db.collection("usuarios")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Usuarios u = document.toObject(Usuarios.class);
                                if (id_usuario.equals(document.getId()) && u.getSubred() == null){
                                    Intent i = new Intent(LoginActivity.this, UnirCrearRedActivity.class);
                                    startActivity(i);
                                    finish();
                                }else{
                                    Intent i = new Intent(LoginActivity.this, PrincipalActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        }
                    }
                });
    }*/

}
