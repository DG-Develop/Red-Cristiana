package com.david.redcristianauno.presentation.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.david.redcristianauno.*
import com.david.redcristianauno.data.network.FirebaseService
import com.david.redcristianauno.presentation.ui.UtilUI.SnackBarMD
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private  var firebaseService = FirebaseService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun enterokayMain(view: View?) {
        val email = etEmailLogin.text.toString().trim{ it <= ' '}.toLowerCase()
        val password = etPasswordLogin.text.toString().trim{it <= ' '}.toLowerCase()

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            rlBaseLogin.visibility = View.VISIBLE
            firebaseService.firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        actionMain()
                    }
                }.addOnFailureListener{ e ->
                    if (e is FirebaseAuthInvalidUserException) {
                        val snack = SnackBarMD.getSBIndefinite(view!!, "Usuario Invalido")
                        SnackBarMD.showSBWithMargin(snack, 32, 32)
                        rlBaseLogin.visibility = View.INVISIBLE
                    } else if (e is FirebaseAuthInvalidCredentialsException) {
                        val snack = SnackBarMD.getSBIndefinite(view!!, "Contraseña invalida")
                        SnackBarMD.showSBWithMargin(snack, 32, 32)
                        rlBaseLogin.visibility = View.INVISIBLE
                    }
                }
        }else{
            val snack = SnackBarMD.getSBIndefinite(view!!, "Completa todos los campos")
            SnackBarMD.showSBWithMargin(snack, 32, 32)
        }
    }

    private fun actionMain(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    fun actionRegister(view: View?) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    override fun onStart() {
        super.onStart()
        if (firebaseService.firebaseAuth.currentUser != null){
            actionMain()
            finish()
        }
    }
}