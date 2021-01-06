package com.david.redcristianauno.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels

import com.david.redcristianauno.R
import com.david.redcristianauno.domain.models.User
import com.david.redcristianauno.presentation.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile_configuration_dialog.*

@AndroidEntryPoint
class ProfileConfigurationDialogFragment : DialogFragment() {

    private val profileViewModel by viewModels<ProfileViewModel>()
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile_configuration_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarBackProfileConfiguration.navigationIcon = ContextCompat.getDrawable(view.context, R.drawable.ic_arrow_back)
        toolbarBackProfileConfiguration.setNavigationOnClickListener {
            dismiss()
        }

        user = arguments?.getParcelable("user")!!

        fillFields(user)

        btnEditProfileConfigurationDialog.setOnClickListener {
            btnSaveProfileConfigurationDialog.visibility = View.VISIBLE
            btnEditProfileConfigurationDialog.visibility = View.GONE
            enableOrDisableFields(true)
        }

        btnSaveProfileConfigurationDialog.setOnClickListener {
            updateDataUser()
            enableOrDisableFields(false)
            btnSaveProfileConfigurationDialog.visibility = View.GONE
            btnEditProfileConfigurationDialog.visibility = View.VISIBLE
        }
    }

    private fun updateDataUser() {
        val fields =  mapOf(
            "names" to etNameConfigurationDialogFragment.text.toString().trim(),
            "last_names" to etLastNamesConfigurationDialogFragment.text.toString().trim(),
            "telephone" to etTelephoneConfigurationDialogFragment.text.toString().trim(),
            "address" to etAddressConfigurationDialogFragment.text.toString().trim()
        )
        profileViewModel.updateUser(fields, user.id)
    }

    private fun enableOrDisableFields(response: Boolean) {
        etNameConfigurationDialogFragment.isEnabled = response
        etLastNamesConfigurationDialogFragment.isEnabled = response
        etTelephoneConfigurationDialogFragment.isEnabled = response
        etAddressConfigurationDialogFragment.isEnabled = response
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    private fun fillFields(user: User){
        etNameConfigurationDialogFragment.setText(user.names)
        etLastNamesConfigurationDialogFragment.setText(user.last_names)
        etTelephoneConfigurationDialogFragment.setText(user.telephone)
        etAddressConfigurationDialogFragment.setText(user.address)
    }
}
