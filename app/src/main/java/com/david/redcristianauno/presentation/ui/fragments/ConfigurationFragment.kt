package com.david.redcristianauno.presentation.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.david.redcristianauno.R
import com.david.redcristianauno.domain.PermissionUseCaseImpl
import com.david.redcristianauno.data.network.FirebaseService
import com.david.redcristianauno.data.network.HistoricalWeeklyRepositoryImpl
import com.david.redcristianauno.presentation.objectsUtils.UserSingleton
import com.david.redcristianauno.presentation.ui.activities.LoginActivity
import com.david.redcristianauno.presentation.viewmodel.PermissionViewModel
import com.david.redcristianauno.presentation.viewmodel.PermissionViewModelFactory
import com.david.redcristianauno.vo.Resource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_configuration.*

class ConfigurationFragment : Fragment() {

    val firebaseService = FirebaseService()
    private var permission: String? = null
    private val user = UserSingleton.getUser()

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            PermissionViewModelFactory(PermissionUseCaseImpl(HistoricalWeeklyRepositoryImpl()))
        ).get(PermissionViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_configuration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i(TAG, "Permiso: ${user?.permission}")

        //observedPermission()
        hideMenuUserNormal(user?.permission!!)

        cvProfile.setOnClickListener {
            findNavController().navigate(R.id.profileConfigurationFragmentDialog)
        }

        cvCreate.setOnClickListener {
            findNavController().navigate(R.id.generalListFragment)
        }

        cvManageUser.setOnClickListener{
            findNavController().navigate(R.id.manageUserConfigurationFragmentDialog)
        }

        cvUploadNotice.setOnClickListener{
            findNavController().navigate(R.id.uploadNoticeConfigurationFragmentDialog)
        }

        cvCloseSession.setOnClickListener {
            MaterialAlertDialogBuilder(
                context,
                R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog
            )
                .setTitle("Cerrar Sesión")
                .setMessage("¿Estás seguro de cerrar la sesión?")
                .setPositiveButton("Aceptar") { _, _ ->
                    firebaseService.firebaseAuth.signOut()

                    startActivity(Intent(context, LoginActivity::class.java))
                    activity?.finish()
                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.cancel()
                }.show()
        }

    }

    private fun observedPermission(){
        viewModel.id_user = firebaseService.firebaseAuth.currentUser?.uid.toString()

        viewModel.fetchPermission.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                    rlBaseConfiguration.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    rlBaseConfiguration.visibility = View.GONE
                    permission = result.data
                    hideMenuUserNormal(permission!!)
                }
                is Resource.Failure -> {
                    Log.e("ERROR:", "${result.exception.message}")
                }
            }
        })
    }

    private fun hideMenuUserNormal(data: String){
        when(data){
            "Normal", "Lider Celula" -> {
                cvCreate.visibility = View.GONE
                cvManageUser.visibility = View.GONE
                cvUploadNotice.visibility = View.GONE
                putCreateTitle("Crear Celula")
            }
            "Admin"-> {
                putCreateTitle("Ver Lista Entidades")
            }
        }
    }

    private fun putCreateTitle(title: String) {
        tvCreateConfiguration.text = title
    }

    companion object{
        private const val TAG = "ConfigInfo"
    }
}
