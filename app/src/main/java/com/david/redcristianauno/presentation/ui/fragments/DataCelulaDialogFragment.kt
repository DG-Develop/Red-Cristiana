package com.david.redcristianauno.presentation.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.david.redcristianauno.presentation.objectsUtils.SnackBarMD
import com.david.redcristianauno.presentation.viewmodel.UserViewModel
import com.david.redcristianauno.presentation.viewmodel.UserViewModelFactory
import com.david.redcristianauno.vo.Resource
import com.google.android.material.datepicker.MaterialDatePicker

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
        viewModel.fillTilSubred()
        fillDecimals()

        filled_exposed_dropdown_Subred.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, _, _ ->
                viewModel.fillTilCelula(filled_exposed_dropdown_Subred.text.toString())
            }

        ibAddCelulaDialogFragment.setOnClickListener {
            tilSubredDataDialogFragment.visibility = View.INVISIBLE
            tilAddress.visibility = View.VISIBLE
            ibCloseCelulaDialogFragment.visibility = View.VISIBLE
            ibAddCelulaDialogFragment.visibility = View.INVISIBLE
        }

        ibCloseCelulaDialogFragment.setOnClickListener {
            tilSubredDataDialogFragment.visibility = View.VISIBLE
            tilAddress.visibility = View.INVISIBLE
            ibAddCelulaDialogFragment.visibility = View.VISIBLE
            ibCloseCelulaDialogFragment.visibility = View.INVISIBLE
        }

        //Listener del imageButton de la fecha
        ibFechaCelula.setOnClickListener {
            showDatePicker(it)
        }

        btnSendDataCelulaDialogFragment.setOnClickListener {
            val host_name = etHostNameDataSubredDialogFragment.text.toString().trim { it <= ' ' }
            val assistance = etAssistenceDataCelulaDialog.text.toString().trim { it <= ' ' }
            val date = etDateDataCelulaDialogFragment.text.toString()

            if (!TextUtils.isEmpty(host_name) && !TextUtils.isEmpty(assistance) && !TextUtils.isEmpty(date)){
                dataRegisterCelula(it, host_name, assistance, date)
            } else{
                SnackBarMD.getSBIndefinite(it, "Por favor llenar los campos de asistencia y de fecha")
            }
        }
    }

    private fun showDatePicker(view: View){
        val builder: MaterialDatePicker.Builder<Long> = MaterialDatePicker.Builder.datePicker()
        builder.setTitleText("Selecciona una fecha")
        builder.setTheme(R.style.DialogTheme)
        val currentTimeInMillis = Calendar.getInstance().timeInMillis
        builder.setSelection(currentTimeInMillis)
        val picker = builder.build()
        activity?.supportFragmentManager?.let { picker.show(it, picker.toString()) }


        picker.addOnPositiveButtonClickListener {
            cal.timeInMillis = it
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR))
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH))
            cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1)

            cal.firstDayOfWeek = Calendar.TUESDAY

            val cal2 = Calendar.getInstance()
            cal2.firstDayOfWeek = Calendar.TUESDAY

            if (cal[Calendar.WEEK_OF_YEAR] == cal2[Calendar.WEEK_OF_YEAR]) {
                updateDateInView()
            } else {
                SnackBarMD.getSBIndefinite(view, "La fecha de la semana no coincide con el de hoy")
            }
        }
    }

    private fun dataRegisterCelula(view: View, host_name: String, assistance: String, date: String) {
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
            val decimals = filled_exposed_dropdown_decimals.text
            res = integers + decimals
            offering = res.toDouble()
        }

        if (assistance.toDouble() < guest || assistance.toDouble() < child) {
           SnackBarMD.getSBIndefinite(view, "El valor de la asistencia no debe de ser menor que el de invitado o el de niños")
        } else {
            val dataCelula = DataCelula()
            dataCelula.id_user = firebaseService.firebaseAuth.currentUser?.uid.toString()
            dataCelula.email_user = firebaseService.firebaseAuth.currentUser?.email.toString()
            dataCelula.user_name = username
            dataCelula.host_name = host_name
            if (etAddressDataSubredDialogFragment.text.toString().trim { it <= ' ' }.isEmpty())
               dataCelula.address = filled_exposed_dropdown.text.toString()
            else
                dataCelula.address =
                    etAddressDataSubredDialogFragment.text.toString().trim { it <= ' ' }
            dataCelula.assistance = assistance.toInt()
            dataCelula.guest = guest
            dataCelula.child = child
            dataCelula.offering = offering
            dataCelula.date = date

            firebaseService.setDocumentWithOutID(dataCelula, "data celula", object : Callback<Void> {
                override fun OnSucces(result: Void?) {
                    SnackBarMD.getSBIndefinite(view, "Enviado")
                }

                override fun onFailure(exception: Exception) {
                    SnackBarMD.getSBIndefinite(view, "No se pudo enviar")
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

    private fun fillDecimals(){
        val array = resources.getStringArray(R.array.decimals)
        val adapter = context?.let { ArrayAdapter(it,
            R.layout.dropdown_menu_popup_item,
            array)
        }
        filled_exposed_dropdown_decimals.setText(array[0])
        filled_exposed_dropdown_decimals.setAdapter(adapter)
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
        viewModel.subredes.observe(viewLifecycleOwner, Observer { listSubred ->
            val adapter = context?.let { ArrayAdapter(it,
                    R.layout.dropdown_menu_popup_item,
                    listSubred)
            }
            filled_exposed_dropdown_Subred.setText(listSubred[0])
            viewModel.fillTilCelula(listSubred[0])
            filled_exposed_dropdown_Subred.setAdapter(adapter)
        })

        viewModel.celulas.observe(viewLifecycleOwner, Observer { listCelulas ->
            val adapter = context?.let { ArrayAdapter(it,
                R.layout.dropdown_menu_popup_item,
                listCelulas)
            }
            filled_exposed_dropdown.setText(listCelulas[0])
            filled_exposed_dropdown.setAdapter(adapter)
        })
    }
}
