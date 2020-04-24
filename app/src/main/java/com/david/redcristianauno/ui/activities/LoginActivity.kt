package com.david.redcristianauno.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.david.redcristianauno.*
import com.david.redcristianauno.Firestore.LeerDatos
import com.david.redcristianauno.network.FirebaseService
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
   // private lateinit var etEmail: EditText
    private lateinit var rbSession: RadioButton
    private  var firebaseService = FirebaseService()
    private  var isActivatedRadioButton = false

    private val l = LeerDatos()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (Preferences.obtenerPreferencesBoolean(
                this,
                Preferences.PREFENCE_ESTADO_BUTTON_SESION
            )
        ) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        rbSession = findViewById(R.id.rdSession_loginActivity)

        isActivatedRadioButton = rbSession!!.isChecked
        rbSession!!.setOnClickListener {
            if (isActivatedRadioButton) {
                rbSession!!.isChecked = false
            }
            isActivatedRadioButton = rbSession!!.isChecked
        }
        val l = LeerDatos()
    }

    fun enterokayMain(view: View?) {
        //val password = etPassword!!.text.toString().trim { it <= ' ' }
        val email = etEmailLogin.text.toString().trim{ it <= ' '}.toLowerCase()
        val password = etPasswordLogin.text.toString().trim{it <= ' '}.toLowerCase()

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            rlBaseLogin.visibility = View.VISIBLE
            firebaseService.firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Preferences.savePreferenceBoolean(
                            this,
                            rbSession.isChecked,
                            Preferences.PREFENCE_ESTADO_BUTTON_SESION
                        )
                        Preferences.savePreferenceString(
                            this,
                            email,
                            Preferences.PREFERENCES_USUARIO_LOGIN
                        )
                        l.preferencesUsuarios(email, this)
                        actionMain()
                    }
                }.addOnFailureListener(OnFailureListener { e ->
                    if (e is FirebaseAuthInvalidUserException) {
                        Toast.makeText(this, "Usuario Invalido", Toast.LENGTH_SHORT)
                            .show()
                        rlBaseLogin.visibility = View.INVISIBLE
                    } else if (e is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(this, "Contraseña invalida", Toast.LENGTH_SHORT)
                            .show()
                        rlBaseLogin.visibility = View.INVISIBLE
                    }
                })
        }else{
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun actionMain(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    fun actionRegister(view: View?) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}