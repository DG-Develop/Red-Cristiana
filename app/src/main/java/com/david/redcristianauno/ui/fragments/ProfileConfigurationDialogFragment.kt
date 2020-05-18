package com.david.redcristianauno.ui.fragments

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
import com.david.redcristianauno.domain.ProfileUseCaseImpl
import com.david.redcristianauno.model.User
import com.david.redcristianauno.model.network.UserRepositoryImpl
import com.david.redcristianauno.viewmodel.ProfileViewModel
import com.david.redcristianauno.viewmodel.ProfileViewModelFactory
import kotlinx.android.synthetic.main.fragment_profile_configuration_dialog.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileConfigurationDialogFragment : DialogFragment() {

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ProfileViewModelFactory(ProfileUseCaseImpl(UserRepositoryImpl()))
        ).get(ProfileViewModel::class.java)
    }

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
        viewModel.refresh()

        btnEditProfileConfigurationDialog.setOnClickListener {
            btnSaveProfileConfigurationDialog.visibility = View.VISIBLE
            btnEditProfileConfigurationDialog.visibility = View.GONE
            enableOrDisableFields(true)
        }

        btnSaveProfileConfigurationDialog.setOnClickListener {
            rlBaseProfile.visibility = View.VISIBLE
            updateDataUser()
            enableOrDisableFields(false)
            btnSaveProfileConfigurationDialog.visibility = View.GONE
            btnEditProfileConfigurationDialog.visibility = View.VISIBLE
            rlBaseProfile.visibility = View.GONE
        }

        observedViewModel()
    }

    private fun updateDataUser() {
        val names = etNameConfigurationDialogFragment.text.toString().trim()
        val last_names = etLastNamesConfigurationDialogFragment.text.toString().trim()
        val telephone = etTelephoneConfigurationDialogFragment.text.toString().trim()
        val address = etAddressConfigurationDialogFragment.text.toString().trim()
        viewModel.updateDataUserFromFirebase(names, last_names, telephone, address)
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

    private fun observedViewModel(){
        viewModel.userData.observe(viewLifecycleOwner, Observer { user ->
            fillFields(user)
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (it != null){
                rlBaseProfile.visibility = View.GONE
            }
        })
    }

    private fun fillFields(user:User){
        etNameConfigurationDialogFragment.setText(user.names)
        etLastNamesConfigurationDialogFragment.setText(user.last_names)
        etTelephoneConfigurationDialogFragment.setText(user.telephone)
        etAddressConfigurationDialogFragment.setText(user.address)
    }
}
