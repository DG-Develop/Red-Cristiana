package com.david.redcristianauno.presentation.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer

import com.david.redcristianauno.R
import com.david.redcristianauno.domain.UserUseCaseImpl
import com.david.redcristianauno.data.model.DataCelula
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.data.network.FirebaseService
import com.david.redcristianauno.data.network.UserRepositoryImpl
import com.david.redcristianauno.presentation.viewmodel.UserViewModel
import com.david.redcristianauno.presentation.viewmodel.UserViewModelFactory
import com.david.redcristianauno.vo.Resource

import kotlinx.android.synthetic.main.fragment_data_celula_dialog.*
import java.lang.Exception
import java.text.DateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class DataCelulaDialogFragment : DialogFragment() {

    private var cal = Calendar.getInstance()
    private val firebaseService = FirebaseService()
    private var username: String = ""
    val list: MutableList<String> = mutableListOf()

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            UserViewModelFactory(UserUseCaseImpl(UserRepositoryImpl()))
        ).get(UserViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data_celula_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarBackRegisterCelula.navigationIcon =
            ContextCompat.getDrawable(view.context, R.drawable.ic_arrow_back)
        toolbarBackRegisterCelula.setNavigationOnClickListener {
            dismiss()
        }

        observeData()
        context?.let { viewModel.fillSpinnerSubred(it, sSubredDataDialogFragment) }

        sSubredDataDialogFragment.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    context?.let {
                        viewModel.fillSpinnerCelula(
                            it,
                            sCelulaDataDialogFragment,
                            sSubredDataDialogFragment.selectedItem.toString()
                        )
                    }
                }
            }

        ibAddCelulaDialogFragment.setOnClickListener {
            sCelulaDataDialogFragment.visibility = View.INVISIBLE
            etAddressDataSubredDialogFragment.visibility = View.VISIBLE
            ibCloseCelulaDialogFragment.visibility = View.VISIBLE
            ibAddCelulaDialogFragment.visibility = View.INVISIBLE
        }

        ibCloseCelulaDialogFragment.setOnClickListener {
            sCelulaDataDialogFragment.visibility = View.VISIBLE
            etAddressDataSubredDialogFragment.visibility = View.INVISIBLE
            ibAddCelulaDialogFragment.visibility = View.VISIBLE
            ibCloseCelulaDialogFragment.visibility = View.INVISIBLE
        }

        //Evento del DatePickerDialog
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                cal.firstDayOfWeek = Calendar.TUESDAY

                val cal2 = Calendar.getInstance()
                cal2.firstDayOfWeek = Calendar.TUESDAY

                if (cal[Calendar.WEEK_OF_YEAR] == cal2[Calendar.WEEK_OF_YEAR]) {
                    updateDateInView()
                } else {
                    Toast.makeText(
                        context,
                        "La fecha de la semana no coincide con el de hoy",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }

        //Listener del imageButton de la fecha
        ibFechaCelula.setOnClickListener {
            context?.let {
                DatePickerDialog(
                    it,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }

        btnSendDataCelulaDialogFragment.setOnClickListener {
            val host_name = etHostNameDataSubredDialogFragment.text.toString().trim { it <= ' ' }
            val assistance = etAssistenceDataCelulaDialog.text.toString().trim { it <= ' ' }
            val date = etDateDataCelulaDialogFragment.text.toString()

            if (!TextUtils.isEmpty(host_name) && !TextUtils.isEmpty(assistance) && !TextUtils.isEmpty(
                    date
                )
            )
                dataRegisterCelula(host_name, assistance, date)
            else
                Toast.makeText(
                    context,
                    "Por favor llenar los campos de asistencia y de fecha",
                    Toast.LENGTH_SHORT
                ).show();
        }

    }

    private fun dataRegisterCelula(host_name: String, assistance: String, date: String) {
        var guest = 0
        var child = 0
        var offering = 0.0
        var res = ""

        if (etGuestDataCelulaDialog.text.toString().trim { it <= ' ' }.isNotEmpty())
            guest = etGuestDataCelulaDialog.text.toString().toInt()

        if (etChildDataCelulaDialog.text.toString().trim { it <= ' ' }.isNotEmpty())
            child = etChildDataCelulaDialog.text.toString().toInt()

        if (etOfferingDataCelulaDialog.text.toString().trim { it <= ' ' }.isNotEmpty()) {
            val integers = etOfferingDataCelulaDialog.text.toString()
            val decimals = sDecimalsCelula.selectedItem.toString()
            res = integers + decimals
            offering = res.toDouble()
        }

        if (assistance.toDouble() < guest || assistance.toDouble() < child) {
            Toast.makeText(
                context,
                "El valor de la asistencia no debe de ser menor que el de invitado o el de niños",
                Toast.LENGTH_LONG
            ).show()
        } else {
            Log.i("InfoUserName:", username)
            val dataCelula = DataCelula()
            dataCelula.id_user = firebaseService.firebaseAuth.currentUser?.uid.toString()
            dataCelula.email_user = firebaseService.firebaseAuth.currentUser?.email.toString()
            dataCelula.user_name = username
            dataCelula.host_name = host_name
            if (etAddressDataSubredDialogFragment.text.toString().trim { it <= ' ' }.isEmpty())
                dataCelula.address = sCelulaDataDialogFragment.selectedItem.toString()
            else
                dataCelula.address =
                    etAddressDataSubredDialogFragment.text.toString().trim { it <= ' ' }
            dataCelula.assistance = assistance.toInt()
            dataCelula.guest = guest
            dataCelula.child = child
            dataCelula.offering = offering
            dataCelula.date = date

            firebaseService.setDocumentWithOutID(
                dataCelula,
                "data celula",
                object : Callback<Void> {
                    override fun OnSucces(result: Void?) {
                        Toast.makeText(context, "Enviado", Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(exception: Exception) {
                        Toast.makeText(context, "No se pudo enviar", Toast.LENGTH_SHORT).show()
                    }
                })
        }
        cleanFields()
    }

    private fun cleanFields() {
        etAddressDataSubredDialogFragment.setText("")
        etHostNameDataSubredDialogFragment.setText("")
        etAssistenceDataCelulaDialog.setText("")
        etGuestDataCelulaDialog.setText("")
        etChildDataCelulaDialog.setText("")
        etOfferingDataCelulaDialog.setText("")
        etDateDataCelulaDialogFragment.setText("")
    }

    private fun updateDateInView() {
        val currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(cal.time)

        etDateDataCelulaDialogFragment.setText(currentDateString)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    private fun observeData() {
        viewModel.id_user = firebaseService.firebaseAuth.currentUser?.uid.toString()
        viewModel.fetchNameUser.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    username = result.data
                }
                is Resource.Failure -> {
                    Log.e("ERROR:", "${result.exception.message}")
                }
            }
        })
    }
}
