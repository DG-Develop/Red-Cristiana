package com.david.redcristianauno.presentation.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.david.redcristianauno.R
import com.david.redcristianauno.data.model.User
import com.david.redcristianauno.data.network.ChurchRepositoryImpl
import com.david.redcristianauno.data.network.FirebaseService
import com.david.redcristianauno.data.network.UserRepositoryImpl
import com.david.redcristianauno.domain.ChurchUseCaseImpl
import com.david.redcristianauno.presentation.viewmodel.JoinViewModel
import com.david.redcristianauno.presentation.viewmodel.JoinViewModelFactory
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_join.*

class JoinActivity : AppCompatActivity() {
    val firebaseService = FirebaseService()

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            JoinViewModelFactory(ChurchUseCaseImpl(ChurchRepositoryImpl(), UserRepositoryImpl()))
        ).get(JoinViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        viewModel.fillTilIglesia()

        dropdown_church.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, _, _ ->
                val data = dropdown_church.text.toString().split(": ")
                Log.i(TAG, "id: ${data[1]}")
                viewModel.fillTilRed(data[1])
            }

        dropdown_red.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, _, _ ->
                val iglesia = dropdown_church.text.toString().split(": ")
                viewModel.fillTilSubred(iglesia[1], dropdown_red.text.toString())
            }

        dropdown_subred.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, _, _ ->
                val iglesia = dropdown_church.text.toString().split(": ")
                viewModel.fillTilCelula(
                    iglesia[1], dropdown_red.text.toString(), dropdown_subred.text.toString()
                )
            }

        btn_start_session_loginActivity.setOnClickListener {
            getChurchAndUpdate()
            actionMain()
        }

        observables()
    }

    private fun actionMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun getChurchAndUpdate() {
        val data = dropdown_church.text.toString().split(": ")
        val references = firebaseService.firebaseFirestore
            .collection(FirebaseService.IGLESIA_COLLECTION_NAME)
            .document(data[1])
            .collection(FirebaseService.REDES_COLLECTION_NAME)
            .document(dropdown_red.text.toString())
            .collection(FirebaseService.SUBREDES_COLLECTION_NAME)
            .document(dropdown_subred.text.toString())
            .collection(FirebaseService.CELULA_COLLECTION_NAME)
            .document(dropdown_celula.text.toString())

        viewModel.updateDataChurchFromFirebase(references)
    }

    private fun observables() {
        viewModel.iglesias.observe(this, Observer { listIglesias ->
            val listNames = mutableListOf<String>()

            for (names in listIglesias) {
                listNames.add("${names.name}: ${names.id_iglesia}")
            }

            val firstData = listNames[0]
            Log.i(TAG, "firstData: $firstData")
            val adapter = ArrayAdapter(this, R.layout.dropdown_menu_popup_item, listNames)
            dropdown_church.setText(firstData)
            val split = firstData.split(": ")
            viewModel.fillTilRed(split[1])
            dropdown_church.setAdapter(adapter)
        })

        viewModel.redes.observe(this, Observer { listRedes ->
            val firstDataRed = listRedes[0]
            Log.i(TAG, "firstDataRed: $firstDataRed")
            dropdown_red.setText(firstDataRed)
            val adapter = ArrayAdapter(this, R.layout.dropdown_menu_popup_item, listRedes)
            val iglesia = dropdown_church.text.toString().split(": ")
            viewModel.fillTilSubred(iglesia[1], dropdown_red.text.toString())
            dropdown_red.setAdapter(adapter)
        })

        viewModel.subredes.observe(this, Observer { listSubredes ->
            val firstDataSubred = listSubredes[0]
            Log.i(TAG, "firstDataRed: $firstDataSubred")
            dropdown_subred.setText(firstDataSubred)
            val adapter = ArrayAdapter(this, R.layout.dropdown_menu_popup_item, listSubredes)
            val iglesia = dropdown_church.text.toString().split(": ")
            viewModel.fillTilCelula(
                iglesia[1], dropdown_red.text.toString(), dropdown_subred.text.toString()
            )
            dropdown_subred.setAdapter(adapter)
        })

        viewModel.celulas.observe(this, Observer { listCelulas ->
            val firstDataCelula = listCelulas[0]
            Log.i(TAG, "firstDataRed: $firstDataCelula")
            dropdown_celula.setText(firstDataCelula)
            val adapter = ArrayAdapter(this, R.layout.dropdown_menu_popup_item, listCelulas)
            dropdown_celula.setAdapter(adapter)

        })
    }

    companion object {
         const val TAG = "JoinInfo"
    }
}