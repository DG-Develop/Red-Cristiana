package com.david.redcristianauno.presentation.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController

import com.david.redcristianauno.R
import kotlinx.android.synthetic.main.fragment_manage_user_configuration_dialog.*

/**
 * A simple [Fragment] subclass.
 */
class ManageUserConfigurationDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_manage_user_configuration_dialog,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarBackManageUserConfiguration.navigationIcon = ContextCompat.getDrawable(view.context, R.drawable.ic_arrow_back)
        toolbarBackManageUserConfiguration.setNavigationOnClickListener {
            dismiss()
        }

        cvNormalUser.setOnClickListener {
            val bundle = bundleOf("user" to "Normal")
            findNavController().navigate(R.id.listUserConfigurationFragmentDialog, bundle)
        }
        cvLeaderCelulaUser.setOnClickListener {
            val bundle = bundleOf("user" to "Lider Celula")
            findNavController().navigate(R.id.listUserConfigurationFragmentDialog, bundle)
        }
        cvSubredUser.setOnClickListener {
            val bundle = bundleOf("user" to "Subred")
            findNavController().navigate(R.id.listUserConfigurationFragmentDialog, bundle)
        }
        cvRedUser.setOnClickListener {
            val bundle = bundleOf("user" to "Red")
            findNavController().navigate(R.id.listUserConfigurationFragmentDialog, bundle)
        }

    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

}
