package com.david.redcristianauno.presentation.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.david.redcristianauno.R
import com.david.redcristianauno.data.network.FirebaseService
import com.david.redcristianauno.presentation.ui.UtilUI.SnackBarMD
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.item_history_daily.*


class ForgotPasswordActivity : AppCompatActivity() {
    private val firebaseService = FirebaseService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        toolbarForgotPassword.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back)
        toolbarForgotPassword.setNavigationOnClickListener {
            finish()
        }
    }

    fun send(view: View){
        val email = etEmailForgot.text.toString().trim()

        if(!TextUtils.isEmpty(email)){
            firebaseService.firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        rlBaseForgotPass.visibility = View.VISIBLE
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }else{
                        rlBaseForgotPass.visibility = View.GONE
                    }
                }.addOnCanceledListener {
                    SnackBarMD.getSBNormal(view, "Se ha cancelado el envio")
                    rlBaseForgotPass.visibility = View.GONE
                }.addOnFailureListener {e ->
                    SnackBarMD.getSBNormal(view, "Error al enviar el email")
                    rlBaseForgotPass.visibility = View.GONE
                    Log.i("ERRORActivity", "Error: ${e.message}")
                }
        }
    }
}