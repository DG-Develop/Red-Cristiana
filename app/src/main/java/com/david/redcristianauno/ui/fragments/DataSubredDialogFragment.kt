package com.david.redcristianauno.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment

import com.david.redcristianauno.R
import com.david.redcristianauno.model.DataSubred
import com.david.redcristianauno.network.Callback
import com.david.redcristianauno.network.FirebaseService
import kotlinx.android.synthetic.main.fragment_data_subred_dialog.*
import java.lang.Exception
import java.text.DateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class DataSubredDialogFragment : DialogFragment() {

    private var cal = Calendar.getInstance()
    private val firebaseService = FirebaseService()
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

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            cal.firstDayOfWeek = Calendar.TUESDAY

            val cal2 = Calendar.getInstance()
            cal2.firstDayOfWeek = Calendar.TUESDAY

            if(cal[Calendar.WEEK_OF_YEAR] == cal2[Calendar.WEEK_OF_YEAR]){
                updateDateInView()
            }else{
                Toast.makeText(context, "La fecha de la semana no coincide con el de hoy", Toast.LENGTH_LONG).show()
            }

        }

        ibFechaSubred.setOnClickListener {
            context?.let {
                DatePickerDialog(
                    it,
                    dateSetListener,
                    // set DatePickerDialog to point to today's date when it loads up
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        }

        btnSendDataSubredDialogFragment.setOnClickListener {
            val host_name = etNameDataSubredDialogFragment.text.toString().trim { it <= ' ' }
            val assistance = etAssistenceDataSubredDialog.text.toString().trim { it <= ' ' }
            val date = etDateDataSubredDialogFragment.text.toString().trim { it <= ' ' }

            if(!TextUtils.isEmpty(host_name) && !TextUtils.isEmpty(assistance) && !TextUtils.isEmpty(date))
                dataRegisterSubred(host_name, assistance, date)
            else
                Toast.makeText(context, "Por favor llenar los campos de asistencia y de fecha", Toast.LENGTH_SHORT).show()
        }

    }

    private fun dataRegisterSubred(host_name : String, assistance : String, date : String) {
        var offering = 0.0
        var res = ""

        if(etOfferingDataSubredDialog.text.toString().trim{ it <= ' '}.isNotEmpty()) {
            val integers = etOfferingDataSubredDialog.text.toString()
            val decimals = sDecimalsSubred.selectedItem.toString()
            res = integers + decimals
            offering = res.toDouble()
        }

        val dataSubred = DataSubred()
        dataSubred.id_user = firebaseService.firebaseAuth.currentUser?.uid.toString()
        dataSubred.host_name = host_name
        dataSubred.assistance = assistance.toInt()
        dataSubred.offering = offering
        dataSubred.date = date

        firebaseService.setDocumentWithOutID(dataSubred, "data subred", object: Callback<Void>{
            override fun OnSucces(result: Void?) {
                Toast.makeText(context, "Enviado", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(exception: Exception) {
                Toast.makeText(context, "No se pudo enviar", Toast.LENGTH_SHORT).show()
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

}
