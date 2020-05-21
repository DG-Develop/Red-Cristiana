package com.david.redcristianauno.presentation.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.david.redcristianauno.R
import com.david.redcristianauno.data.model.User
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.data.network.FirebaseService
import com.david.redcristianauno.presentation.ui.UtilUI.SnackBarMD
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register.*
import java.lang.Exception

class RegisterActivity: AppCompatActivity() {
    private  var firebaseService = FirebaseService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        toolbarBackRegister.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back)
        toolbarBackRegister.setNavigationOnClickListener {
            finish()
        }
    }

    fun userRegister(view: View?){
        val names = etNamesResgisterActivity.text.toString().trim{it <= ' '}
        val last_name = etLastNamesRegisterActivity.text.toString().trim{it <= ' '}
        val address = etAddressRegisterActivity.text.toString().trim { it <= ' ' }
        val telephone = etTelephoneRegisterActivity.text.toString().trim { it <= ' '}
        val email = etEmailResgisterActivity.text.toString().trim{it <= ' '}
        val password = etPassResgisterActivity.text.toString().trim{it <= ' '}
        val confirm_password = etConfirmPassResgisterActivity.text.toString().trim{it <= ' '}

        if(!TextUtils.isEmpty(names) &&
            !TextUtils.isEmpty(last_name) &&
            !TextUtils.isEmpty(address) &&
            !TextUtils.isEmpty(telephone) &&
            !TextUtils.isEmpty(email) &&
            !TextUtils.isEmpty(password) &&
            !TextUtils.isEmpty(confirm_password)){
            if (password == confirm_password){
                rlBaseRegister.visibility = View.VISIBLE

                firebaseService.firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this){task ->

                        if(task.isComplete){
                            val user: FirebaseUser? = firebaseService.firebaseAuth.currentUser
                            verifyEmail(user)
                            val id = firebaseService.firebaseAuth.uid.toString()
                            createAccount(names, last_name, address, telephone, id, email, password)
                        }

                    }.addOnFailureListener(OnFailureListener { e ->
                        if(e is FirebaseAuthUserCollisionException){
                            val snack = SnackBarMD.getSBIndefinite(view!!, "Este correo ya esta en uso")
                            SnackBarMD.showSBWithMargin(snack, 32, 32)
                            rlBaseRegister.visibility = View.INVISIBLE
                        }else{
                            Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT)
                                .show()
                            rlBaseRegister.visibility = View.INVISIBLE
                        }
                    })
            }
        }else{
            val snack = SnackBarMD.getSBIndefinite(view!!, "No deje ningun campo vacío")
            SnackBarMD.showSBWithMargin(snack, 32, 32)
        }

    }
    private fun actionLogin(){
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun createAccount(names: String, last_names: String, address: String, telephone: String, id: String, email: String, password: String){
        val user = User()
        user.names = names
        user.last_names = last_names
        user.address = address
        user.telephone = telephone
        user.id = id
        user.email = email
        user.password = password
        user.permission = "Normal"

        firebaseService.setDocumentWithID(user,"users", id, object : Callback<Void>{
            override fun OnSucces(result: Void?) {
                actionLogin()
            }
            override fun onFailure(exception: Exception) {
                showErrorMessage()
            }

        })
    }

    private fun showErrorMessage() {
        val snack = SnackBarMD.getSBIndefinite(findViewById(android.R.id.content), "Error al crear el usuario")
        SnackBarMD.showSBWithMargin(snack, 32, 32)
    }

    private fun verifyEmail(user: FirebaseUser?){
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this){task ->
                if(task.isComplete){
                    val snack = SnackBarMD.getSBIndefinite(findViewById(android.R.id.content), "Email enviado")
                    SnackBarMD.showSBWithMargin(snack, 32, 32)
                }else{
                    val snack = SnackBarMD.getSBIndefinite(findViewById(android.R.id.content), "Error al enviar el email")
                    SnackBarMD.showSBWithMargin(snack, 32, 32)
                }
            }
    }
}