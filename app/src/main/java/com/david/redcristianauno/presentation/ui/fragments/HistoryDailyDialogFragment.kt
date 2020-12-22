package com.david.redcristianauno.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer

import com.david.redcristianauno.R
import com.david.redcristianauno.domain.DataCelulaUseCaseImpl
import com.david.redcristianauno.data.network.DataCelulaRepositoryImpl
import com.david.redcristianauno.presentation.ui.adapters.HistoricalDailyAdapter
import com.david.redcristianauno.presentation.viewmodel.DataCelulaViewModel
import com.david.redcristianauno.presentation.viewmodel.DataCelulaViewModelFactory
import kotlinx.android.synthetic.main.fragment_history_daily_dialog.*
import java.text.DateFormat
import java.util.*


class HistoryDailyDialogFragment : DialogFragment() {

    private var cal = Calendar.getInstance()
    private lateinit var historicalDailyAdapter: HistoricalDailyAdapter
    var currentDateString: String = ""

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            DataCelulaViewModelFactory(DataCelulaUseCaseImpl(DataCelulaRepositoryImpl()))
        ).get(DataCelulaViewModel::class.java)
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
        return inflater.inflate(R.layout.fragment_history_daily_dialog, container, false)
    }

    //@RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarBackHistoryDaily.navigationIcon = ContextCompat.getDrawable(view.context, R.drawable.ic_arrow_back)
        toolbarBackHistoryDaily.setNavigationOnClickListener {
            dismiss()
        }

        historicalDailyAdapter = HistoricalDailyAdapter()
        rvListHistoricalDaily.adapter = historicalDailyAdapter

        currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(cal.time)
        viewModel.refresh(currentDateString)


        dpDetailHistoricalDaily.setOnDateChangeListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            cal.firstDayOfWeek = Calendar.TUESDAY

            currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(cal.time)

            viewModel.refresh(currentDateString)
        }

        /*Con DatePicker
        dpDetailHistoricalDaily.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            cal.firstDayOfWeek = Calendar.TUESDAY

            currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(cal.time)

            viewModel.refresh(currentDateString)
        }*/

        observedViewModel()

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    private fun observedViewModel(){
        viewModel.listSchedule.observe(viewLifecycleOwner, Observer { schedule ->
            historicalDailyAdapter.updateData(schedule)
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if(it != null){
                rlBaseHistoryDaily.visibility = View.GONE
            }
        })
    }
}
