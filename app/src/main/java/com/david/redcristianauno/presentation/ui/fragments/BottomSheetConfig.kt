package com.david.redcristianauno.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.david.redcristianauno.R
import com.david.redcristianauno.domain.models.User
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_entity.*

class BottomSheetConfig: BottomSheetDialogFragment() {

    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_entity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         user = arguments?.getParcelable("user")!!

        btnGoRed.setOnClickListener {
            val bundle = bundleOf(
                "user" to user,
                "dataType" to "Red"
            )
            findNavController().navigate(R.id.generalListFragment, bundle)

        }

        btnGoSubred.setOnClickListener {
            val bundle = bundleOf(
                "user" to user,
                "dataType" to "Subred"
            )

            findNavController().navigate(R.id.generalListFragment, bundle)
        }

        btnGoCelula.setOnClickListener{
            val bundle = bundleOf(
                "user" to user,
                "dataType" to "Celula"
            )
            findNavController().navigate(R.id.generalListFragment, bundle)
        }
    }


}