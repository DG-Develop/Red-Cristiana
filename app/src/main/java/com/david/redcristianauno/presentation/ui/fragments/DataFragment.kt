package com.david.redcristianauno.presentation.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.david.redcristianauno.R
import kotlinx.android.synthetic.main.fragment_data.*

/**
 * A simple [Fragment] subclass.
 */
class DataFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnEnterokySubredDataFragment.setOnClickListener {
            findNavController().navigate(R.id.action_navDataFragment_to_dataSubredFragmentDialog)
        }

        btnEnterokyCelulaDataFragment.setOnClickListener {
            findNavController().navigate(R.id.action_navDataFragment_to_dataCelulaFragmentDialog)
        }
    }

}
