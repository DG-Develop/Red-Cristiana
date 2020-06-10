package com.david.redcristianauno.presentation.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.david.redcristianauno.R
import kotlinx.android.synthetic.main.activity_forgot_password.*


class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        toolbarForgotPassword.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back)
        toolbarForgotPassword.setNavigationOnClickListener {
            finish()
        }
    }
}