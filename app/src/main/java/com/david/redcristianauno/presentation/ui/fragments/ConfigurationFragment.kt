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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.bottom_sheet_entity.*
import kotlinx.android.synthetic.main.fragment_configuration.*

class ConfigurationFragment : Fragment() {

    val firebaseService = FirebaseService()
    private var permission: String? = null
    private val user = UserSingleton.getUser()
    private lateinit var type: String //Dependiendo del tipo de dato podra hacer diferentes funciones

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

        val mBottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_entity)
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        var flag = true

        cvProfile.setOnClickListener {
            findNavController().navigate(R.id.profileConfigurationFragmentDialog)
        }

        cvCreate.setOnClickListener {
            if(type == "Admin"){
                mBottomSheetBehavior.state = if (flag) BottomSheetBehavior.STATE_EXPANDED else BottomSheetBehavior.STATE_HIDDEN
                flag = !flag
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

        // Bottom Sheet Entity Events
        btnGoRed.setOnClickListener {
            val bundle = bundleOf(
                "permission" to "Admin",
                "dataType" to "Red"
            )
            mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            flag=!flag
            findNavController().navigate(R.id.generalListFragment, bundle)
        }

        btnGoSubred.setOnClickListener {
            val bundle = bundleOf(
                "permission" to type,
                "dataType" to "Subred"
            )
            mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            flag=!flag
            findNavController().navigate(R.id.generalListFragment, bundle)
        }

        btnGoCelula.setOnClickListener{
            val bundle = bundleOf(
                "permission" to type,
                "dataType" to "Celula"
            )
            mBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            flag=!flag
            findNavController().navigate(R.id.generalListFragment, bundle)
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
                type = data
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
