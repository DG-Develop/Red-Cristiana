package com.david.redcristianauno.presentation.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.david.redcristianauno.R
import com.david.redcristianauno.domain.HistoricalWeeklyUseCaseImpl
import com.david.redcristianauno.data.network.HistoricalWeeklyRepositoryImpl
import com.david.redcristianauno.presentation.ui.adapters.HistoricalWeeklyAdapter
import com.david.redcristianauno.presentation.viewmodel.HistoryWeeklyViewModel
import com.david.redcristianauno.presentation.viewmodel.HistoryWeeklyViewModelFactory
import kotlinx.android.synthetic.main.fragment_history_weekly_dialog.*

/**
 * A simple [Fragment] subclass.
 */
class HistoryWeeklyDialogFragment : DialogFragment() {

    private lateinit var historicalWeeklyAdapter: HistoricalWeeklyAdapter

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            HistoryWeeklyViewModelFactory(HistoricalWeeklyUseCaseImpl(HistoricalWeeklyRepositoryImpl()))
        ).get(HistoryWeeklyViewModel::class.java)
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
        return inflater.inflate(R.layout.fragment_history_weekly_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarBackHistoryWeekly.navigationIcon = ContextCompat.getDrawable(view.context, R.drawable.ic_arrow_back)
        toolbarBackHistoryWeekly.setNavigationOnClickListener {
            dismiss()
        }
        historicalWeeklyAdapter = HistoricalWeeklyAdapter()
        rvWeeklyItemHistoryWeekly.adapter = historicalWeeklyAdapter
        viewModel.refresh()
        observedViewModel()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    private fun observedViewModel(){
        viewModel.listHistorical.observe(viewLifecycleOwner, Observer { result ->
            historicalWeeklyAdapter.updateData(result)
        })
        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (it != null){
                rlBaseHistoryWeekly.visibility = View.GONE
            }
        })
    }
}
