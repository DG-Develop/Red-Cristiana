package com.david.redcristianauno.presentation.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.david.redcristianauno.R
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.domain.models.UserDataSource
import com.david.redcristianauno.presentation.objectsUtils.SnackBarMD
import com.david.redcristianauno.presentation.viewmodel.RegisterViewModel
import com.david.redcristianauno.vo.Resource
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_register.*
import java.lang.Exception

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private val registerViewModel by viewModels<RegisterViewModel>()
    private lateinit var user: UserDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        toolbarBackRegister.navigationIcon =
            ContextCompat.getDrawable(this, R.drawable.ic_arrow_back)
        toolbarBackRegister.setNavigationOnClickListener {
            finish()
        }

        setupObservers()
    }

    fun userRegister(view: View?) {
        val names = etNamesResgisterActivity.text.toString().trim { it <= ' ' }
        val last_name = etLastNamesRegisterActivity.text.toString().trim { it <= ' ' }
        val address = etAddressRegisterActivity.text.toString().trim { it <= ' ' }
        val telephone = etTelephoneRegisterActivity.text.toString().trim { it <= ' ' }
        val email = etEmailResgisterActivity.text.toString().trim()
        val password = etPassResgisterActivity.text.toString().trim()
        val confirm_password = etConfirmPassResgisterActivity.text.toString().trim { it <= ' ' }

        if (!TextUtils.isEmpty(names) &&
            !TextUtils.isEmpty(last_name) &&
            !TextUtils.isEmpty(address) &&
            !TextUtils.isEmpty(telephone) &&
            !TextUtils.isEmpty(email) &&
            !TextUtils.isEmpty(password) &&
            !TextUtils.isEmpty(confirm_password)
        ) {
            if (password == confirm_password) {
                registerViewModel.setCredentials(email, password)
                user = UserDataSource(
                    names = names,
                    last_names = last_name,
                    address = address,
                    telephone = telephone,
                    email = email,
                    permission = listOf("Postulado")
                )
            } else {
                SnackBarMD.getSBIndefinite(
                    btn_create_account_registerActivity,
                    "Contraseñas no coinciden"
                )
            }
        } else {
            SnackBarMD.getSBIndefinite(view!!, "No deje ningun campo vacío")
        }

    }

    private fun actionLogin() {
        registerViewModel.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun setupObservers() {
        registerViewModel.createUserAuthFromFirebase().observe(this, Observer { result ->
            when (result) {
                is Resource.Loading -> rlBaseRegister.visibility = View.VISIBLE
                is Resource.Success -> {
                    val id = result.data?.user?.uid.toString()
                    user.id = id
                    registerViewModel.createUserFirestoreFromFirebase(
                        user,
                        object : Callback<Void> {
                            override fun OnSucces(result: Void?) {
                                actionLogin()
                            }

                            override fun onFailure(exception: Exception) {
                                showErrorMessage()
                            }
                        })

                }
                is Resource.Failure -> {
                    if (result.exception is FirebaseAuthUserCollisionException) {
                        SnackBarMD.getSBIndefinite(
                            btn_create_account_registerActivity,
                            "Este correo ya esta en uso"
                        )
                    } else {
                        Toast.makeText(this, result.exception.localizedMessage, Toast.LENGTH_SHORT)
                            .show()
                    }
                    rlBaseRegister.visibility = View.GONE
                }
            }
        })
    }

    private fun showErrorMessage() {
        SnackBarMD.getSBIndefinite(findViewById(android.R.id.content), "Error al crear el usuario")
    }
}