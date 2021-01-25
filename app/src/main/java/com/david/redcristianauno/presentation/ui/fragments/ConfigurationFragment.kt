package com.david.redcristianauno.presentation.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.david.redcristianauno.R
import com.david.redcristianauno.application.AppConstants.CONFIG_FRAGMENT
import com.david.redcristianauno.application.AppConstants.MAIN_ACTIVITY
import com.david.redcristianauno.domain.models.User
import com.david.redcristianauno.presentation.ui.activities.LoginActivity
import com.david.redcristianauno.presentation.viewmodel.ConfigurationViewModelP
import com.david.redcristianauno.vo.Resource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_configuration.*

@AndroidEntryPoint
class ConfigurationFragment : Fragment() {
    private lateinit var user: User //Dependiendo del tipo de dato podra hacer diferentes funciones
    private val configurationViewModel by viewModels<ConfigurationViewModelP>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_configuration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       val id = configurationViewModel.getIdUser()
        if(id.isNotBlank()){
            configurationViewModel.getUser(id)
        }

        setupObservers()

        cvProfile.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("user", user)
            findNavController().navigate(R.id.profileConfigurationFragmentDialog, bundle)
        }

        cvCreate.setOnClickListener {
            if(user.permission.contains("Admin")){
                val bundle = Bundle()
                bundle.putParcelable("user", user)
               findNavController().navigate(R.id.bottomSheetConfig, bundle)
            }else{
                findNavController().navigate(R.id.generalListFragment)
            }
        }

        cvCaptureUser.setOnClickListener{
            val bundle = Bundle()
            bundle.putParcelable("user", user)
            findNavController().navigate(R.id.captureUserFragment, bundle)
        }

        cvManageUser.setOnClickListener{
            findNavController().navigate(R.id.manageUserConfigurationFragmentDialog)
        }

        cvUploadNotice.setOnClickListener{
            findNavController().navigate(R.id.uploadNoticeConfigurationFragmentDialog)
        }

        cvCloseSession.setOnClickListener {
            closeSession()
        }
    }

    private fun closeSession(){
        MaterialAlertDialogBuilder(
            requireContext(),
            R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog
        )
            .setTitle("Cerrar Sesión")
            .setMessage("¿Estás seguro de cerrar la sesión?")
            .setPositiveButton("Aceptar") { _, _ ->
                configurationViewModel.signOutFromFirebase()
                startActivity(Intent(context, LoginActivity::class.java))
                activity?.finish()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }.show()
    }

    private fun setupObservers(){
        configurationViewModel.fetchUser.observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Resource.Loading -> Log.i(CONFIG_FRAGMENT, "Cargando...")
                is Resource.Success -> {
                    if (result.data != null){
                        Log.i(MAIN_ACTIVITY, "User: ${result.data}")
                        hideMenuUserNormal(result.data.permission)
                        user = result.data
                    }
                }
                is Resource.Failure -> Log.i(CONFIG_FRAGMENT, "Error: ${result.exception}")
            }
        })
    }

    private fun hideMenuUserNormal(permissionList: List<String>){
        if(permissionList.isNullOrEmpty()){
            return
        }

        if(permissionList.contains("Normal")){
            cvCreate.visibility = View.GONE
            cvManageUser.visibility = View.GONE
            cvUploadNotice.visibility = View.GONE
            putCreateTitle("Crear Celula")
        }else{
            cvCreate.visibility = View.VISIBLE
            cvManageUser.visibility = View.VISIBLE
            cvUploadNotice.visibility = View.VISIBLE
        }

        permissionList.forEach{ permission->
            if(permission == "Admin"){
                putCreateTitle("Ver Lista Entidades")
            }
        }
    }

    private fun putCreateTitle(title: String) {
        tvCreateConfiguration.text = title
    }
}
