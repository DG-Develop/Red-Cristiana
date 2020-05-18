package com.david.redcristianauno.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.david.redcristianauno.R
import kotlinx.android.synthetic.main.fragment_historical.*

class HistoricalFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_historical, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSeeDataHistoricalDaily.setOnClickListener {
            findNavController().navigate(R.id.action_navHistoricalFragment_to_historyDailyFragmentDialog)
        }

        btnSeeDataHistoricalWeekly.setOnClickListener {
            findNavController().navigate(R.id.action_navHistoricalFragment_to_historyWeeklyFragmentDialog)
        }
    }
}