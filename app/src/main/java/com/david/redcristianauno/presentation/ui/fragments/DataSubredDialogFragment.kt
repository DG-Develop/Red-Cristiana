package com.david.redcristianauno.presentation.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer

import com.david.redcristianauno.R
import com.david.redcristianauno.domain.UserUseCaseImpl
import com.david.redcristianauno.data.model.DataSubred
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.data.network.FirebaseService
import com.david.redcristianauno.data.network.UserRepositoryImpl
import com.david.redcristianauno.presentation.ui.UtilUI.SnackBarMD
import com.david.redcristianauno.presentation.viewmodel.UserViewModel
import com.david.redcristianauno.presentation.viewmodel.UserViewModelFactory
import com.david.redcristianauno.vo.Resource
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.android.synthetic.main.fragment_data_celula_dialog.*
import kotlinx.android.synthetic.main.fragment_data_subred_dialog.*
import kotlinx.android.synthetic.main.fragment_data_subred_dialog.filled_exposed_dropdown_decimals
import java.lang.Exception
import java.text.DateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class DataSubredDialogFragment : DialogFragment() {

    private var cal = Calendar.getInstance()
    private val firebaseService = FirebaseService()

    private var username: String = ""

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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data_subred_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarBackRegisterSubred.navigationIcon = ContextCompat.getDrawable(view.context, R.drawable.ic_arrow_back)
        toolbarBackRegisterSubred.setNavigationOnClickListener {
            dismiss()
        }

        observeData()
        fillDecimals()

        ibFechaSubred.setOnClickListener {
            showDatePicker(it)
        }


        btnSendDataSubredDialogFragment.setOnClickListener {
            val host_name = etNameDataSubredDialogFragment.text.toString().trim { it <= ' ' }
            val assistance = etAssistenceDataSubredDialog.text.toString().trim { it <= ' ' }
            val date = etDateDataSubredDialogFragment.text.toString().trim { it <= ' ' }

            if(!TextUtils.isEmpty(host_name) && !TextUtils.isEmpty(assistance) && !TextUtils.isEmpty(date)) {
                dataRegisterSubred(host_name, assistance, date)
            } else{
                SnackBarMD.getSBIndefinite(view, "Por favor llenar los campos de asistencia y de fecha")
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

    private fun fillDecimals(){
        val array = resources.getStringArray(R.array.decimals)
        val adapter = context?.let { ArrayAdapter(it,
            R.layout.dropdown_menu_popup_item,
            array)
        }
        filled_exposed_dropdown_decimals.setText(array[0])
        filled_exposed_dropdown_decimals.setAdapter(adapter)
    }

    private fun dataRegisterSubred(host_name : String, assistance : String, date : String) {
        var offering = 0.0
        var res = ""

        if(etOfferingDataSubredDialog.text.toString().trim{ it <= ' '}.isNotEmpty()) {
            val integers = etOfferingDataSubredDialog.text.toString()
            val decimals = filled_exposed_dropdown_decimals.text.toString()
            res = integers + decimals
            offering = res.toDouble()
        }

        val dataSubred = DataSubred()
        dataSubred.id_user = firebaseService.firebaseAuth.currentUser?.uid.toString()
        dataSubred.user_name = username
        dataSubred.email_user = firebaseService.firebaseAuth.currentUser?.email.toString()
        dataSubred.host_name = host_name
        dataSubred.assistance = assistance.toInt()
        dataSubred.offering = offering
        dataSubred.date = date

        firebaseService.setDocumentWithOutID(dataSubred, "data subred", object: Callback<Void>{
            override fun OnSucces(result: Void?) {
                SnackBarMD.getSBIndefinite(view!!, "Enviado")
            }

            override fun onFailure(exception: Exception) {
                SnackBarMD.getSBIndefinite(view!!, "No se pudo enviar")
            }

        })

        cleanFields()
    }

    private fun cleanFields(){
        etNameDataSubredDialogFragment.setText("")
        etAssistenceDataSubredDialog.setText("")
        etOfferingDataSubredDialog.setText("")
        etDateDataSubredDialogFragment.setText("")
    }
    private fun updateDateInView() {
        val currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(cal.time)

        etDateDataSubredDialogFragment.setText(currentDateString)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    private fun observeData(){
        viewModel.id_user = firebaseService.firebaseAuth.currentUser?.uid.toString()
        viewModel.fetchNameUser.observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Resource.Loading ->{

                }
                is Resource.Success->{
                    username = result.data
                }
                is Resource.Failure->{
                    Log.e("ERROR:", "${result.exception.message}")
                }
            }
        })
    }


}
