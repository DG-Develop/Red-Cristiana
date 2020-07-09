package com.david.redcristianauno.presentation.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.david.redcristianauno.*
import com.david.redcristianauno.data.network.FirebaseService
import com.david.redcristianauno.data.network.UserRepositoryImpl
import com.david.redcristianauno.domain.ProfileUseCaseImpl
import com.david.redcristianauno.presentation.objectsUtils.SnackBarMD
import com.david.redcristianauno.presentation.viewmodel.ProfileViewModel
import com.david.redcristianauno.presentation.viewmodel.ProfileViewModelFactory
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private  var firebaseService = FirebaseService()
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ProfileViewModelFactory(ProfileUseCaseImpl(UserRepositoryImpl()))
        ).get(ProfileViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        observedViewModel()
    }

    fun enterokayMain(view: View?) {
        val email = etEmailLogin.text.toString().trim()
        val password = etPasswordLogin.text.toString().trim()

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            rlBaseLogin.visibility = View.VISIBLE
            firebaseService.firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        viewModel.refresh()
                    }
                }.addOnFailureListener{ e ->
                    if (e is FirebaseAuthInvalidUserException) {
                       SnackBarMD.getSBIndefinite(view!!, "Usuario Invalido")
                        rlBaseLogin.visibility = View.INVISIBLE
                    } else if (e is FirebaseAuthInvalidCredentialsException) {
                       SnackBarMD.getSBIndefinite(view!!, "Contraseña invalida")
                        rlBaseLogin.visibility = View.INVISIBLE
                    }
                }
        }else{
          SnackBarMD.getSBIndefinite(view!!, "Completa todos los campos")
        }
    }

    private fun actionMain(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    fun actionRegister(view: View?) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    fun actionForgotPassword(view: View?) {
        startActivity(Intent(this, ForgotPasswordActivity::class.java))
    }

    private fun observedViewModel() {
        viewModel.userData.observe(this, Observer { user ->
            if (user.iglesia_references != null){
                actionMain()
            }else{
                actionJoinOrInvite()
            }
        })
    }

    private fun actionJoinOrInvite() {
        startActivity(Intent(this, JoinOrInviteActivity::class.java))
        finish()
    }

    override fun onStart() {
        super.onStart()
        if (firebaseService.firebaseAuth.currentUser != null){
            actionMain()
            finish()
        }
    }

    companion object{
        private const val TAG = "LoginInfo"
    }
}