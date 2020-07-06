package com.david.redcristianauno.presentation.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.david.redcristianauno.R
import kotlinx.android.synthetic.main.activity_join_or_invite.*

class JoinOrInviteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_or_invite)

        btnInsideHere.setOnClickListener {
            startActivity(Intent(this, JoinActivity::class.java))
        }

        btnIgnore.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}