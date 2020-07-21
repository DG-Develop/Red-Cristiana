package com.david.redcristianauno.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController

import com.david.redcristianauno.R
import kotlinx.android.synthetic.main.fragment_manage_user_configuration_dialog.*

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

        cvMemberPostulate.setOnClickListener{
            goToNavigator("Postulado")
        }
        cvMemberUser.setOnClickListener {
            goToNavigator("Normal")
        }
        cvLeaderCelulaUser.setOnClickListener {
            goToNavigator("Lider Celula")
        }
        cvSubredUser.setOnClickListener {
            goToNavigator("Subred")
        }
        cvRedUser.setOnClickListener {
            goToNavigator("Red")
        }

    }

    private fun goToNavigator(value: String){
        val bundle = bundleOf(KEY to value)
        findNavController().navigate(R.id.listUserConfigurationFragmentDialog, bundle)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    companion object{
        private const val KEY = "user"
    }

}
