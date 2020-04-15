package com.david.redcristianauno

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.david.redcristianauno.Firestore.LeerDatos
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private var txtCorreo: EditText? = null
    private var txtPassword: EditText? = null
    private var rbSesion: RadioButton? = null
    private var firebaseAuth: FirebaseAuth? = null
    private var progressDialog: ProgressDialog? = null
    private var isActivatedRadioButton = false
    private val db = FirebaseFirestore.getInstance()

    //private TextView txt_titulo;
    //private Typeface script;
    private val l = LeerDatos()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (Preferences.obtenerPreferencesBoolean(
                this,
                Preferences.PREFENCE_ESTADO_BUTTON_SESION
            )
        ) {
            val i = Intent(this@LoginActivity, PrincipalActivity::class.java)
            startActivity(i)
            finish()
        }
        firebaseAuth = FirebaseAuth.getInstance()
        txtCorreo = findViewById<View>(R.id.txtCorreoLogin) as EditText
        txtPassword = findViewById<View>(R.id.txtPasswordLogin) as EditText
        rbSesion = findViewById<View>(R.id.sesion) as RadioButton
        //txt_titulo = findViewById(R.id.txt_titulo);

        //String fuente = "Fonts/OpenSans-BoldItalic.ttf";

        //this.script = Typeface.createFromAsset(getAssets(), fuente);
        //txt_titulo.setTypeface(script);
        progressDialog = ProgressDialog(this)
        isActivatedRadioButton = rbSesion!!.isChecked
        rbSesion!!.setOnClickListener {
            if (isActivatedRadioButton) {
                rbSesion!!.isChecked = false
            }
            isActivatedRadioButton = rbSesion!!.isChecked
        }
        val l = LeerDatos()
    }

    fun ingresar(view: View?) {
        val email = txtCorreo!!.text.toString().trim { it <= ' ' }.toLowerCase()
        val password = txtPassword!!.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Ingresa un usuario", Toast.LENGTH_SHORT).show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Ingresa una contraseña", Toast.LENGTH_SHORT).show()
            return
        }
        progressDialog!!.setMessage("Realizando Consulta...")
        progressDialog!!.show()
        firebaseAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Preferences.savePreferenceBoolean(
                        this@LoginActivity,
                        rbSesion!!.isChecked,
                        Preferences.PREFENCE_ESTADO_BUTTON_SESION
                    )
                    Preferences.savePreferenceString(
                        this@LoginActivity,
                        email,
                        Preferences.PREFERENCES_USUARIO_LOGIN
                    )
                    l.preferencesUsuarios(email, this@LoginActivity)

                    //buscarSubredUsuario(Preferences.obtenerPreferencesString(LoginActivity.this, Preferences.PREFERENCES_ID_USUARIO));
                    val i = Intent(this@LoginActivity, PrincipalActivity::class.java)
                    startActivity(i)
                    finish()
                }
                progressDialog!!.dismiss()
            }.addOnFailureListener(OnFailureListener { e ->
                if (e is FirebaseAuthInvalidUserException) {
                    Toast.makeText(this@LoginActivity, "Usuario Invalido", Toast.LENGTH_SHORT)
                        .show()
                    return@OnFailureListener
                } else if (e is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(this@LoginActivity, "Contraseña invalida", Toast.LENGTH_SHORT)
                        .show()
                    return@OnFailureListener
                }
            })
    }

    fun registrar(view: View?) {
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
    } /*public void buscarSubredUsuario(final String id_usuario){
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