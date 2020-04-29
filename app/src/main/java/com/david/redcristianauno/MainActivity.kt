package com.david.redcristianauno

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.david.redcristianauno.network.FirebaseService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val firebaseService = FirebaseService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setActionBar(findViewById(R.id.toolbarMain))
        configNav()

        Log.i("info", firebaseService.firebaseAuth.currentUser?.uid.toString())
    }

    private fun configNav(){
        NavigationUI.setupWithNavController(bnvMenu, Navigation.findNavController(this, R.id.fragContent))
    }
}
