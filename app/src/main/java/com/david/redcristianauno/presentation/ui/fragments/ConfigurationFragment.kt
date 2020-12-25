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
import com.david.redcristianauno.presentation.ui.activities.LoginActivity
import com.david.redcristianauno.presentation.viewmodel.ConfigurationViewModelP
import com.david.redcristianauno.vo.Resource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_configuration.*

@AndroidEntryPoint
class ConfigurationFragment : Fragment() {
    private lateinit var type: String //Dependiendo del tipo de dato podra hacer diferentes funciones
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
            findNavController().navigate(R.id.profileConfigurationFragmentDialog)
        }

        cvCreate.setOnClickListener {
            if(type == "Admin"){
               findNavController().navigate(R.id.bottomSheetConfig)
            }else{
                findNavController().navigate(R.id.generalListFragment)
            }
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

        // Bottom Sheet Entity Events
        /*btnGoRed.setOnClickListener {
            val bundle = bundleOf(
                "permission" to "Admin",
                "dataType" to "Red"
            )
            findNavController().navigate(R.id.generalListFragment, bundle)
        }

        btnGoSubred.setOnClickListener {
            val bundle = bundleOf(
                "permission" to type,
                "dataType" to "Subred"
            )

            findNavController().navigate(R.id.generalListFragment, bundle)
        }

        btnGoCelula.setOnClickListener{
            val bundle = bundleOf(
                "permission" to type,
                "dataType" to "Celula"
            )
            findNavController().navigate(R.id.generalListFragment, bundle)
        }*/

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
                    result.data?.let { hideMenuUserNormal(it.permission) }
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
        }

        permissionList.forEach{ permission->
            if(permission == "Admin"){
                type = permission
                putCreateTitle("Ver Lista Entidades")
            }
        }
    }

    private fun putCreateTitle(title: String) {
        tvCreateConfiguration.text = title
    }
}
