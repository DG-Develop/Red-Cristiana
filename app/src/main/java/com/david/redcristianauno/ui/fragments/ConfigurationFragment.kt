package com.david.redcristianauno.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.david.redcristianauno.Preferences
import com.david.redcristianauno.R
import com.david.redcristianauno.network.FirebaseService
import com.david.redcristianauno.ui.activities.LoginActivity
import kotlinx.android.synthetic.main.fragment_configuration.*

/**
 * A simple [Fragment] subclass.
 */
class ConfigurationFragment : Fragment() {

    val firebaseService = FirebaseService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_configuration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cvProfile.setOnClickListener {
            findNavController().navigate(R.id.profileConfigurationFragmentDialog)
        }

        cvManageUser.setOnClickListener{
            findNavController().navigate(R.id.manageUserConfigurationFragmentDialog)
        }

        cvUploadNotice.setOnClickListener{
            findNavController().navigate(R.id.uploadNoticeConfigurationFragmentDialog)
        }

        cvCloseSession.setOnClickListener{
            AlertDialog.Builder(context)
                .setTitle("Cerrar Sesión")
                .setMessage("¿Estás seguro de cerrar la sesión?")
                .setPositiveButton("Aceptar") { dialog, which ->
                    firebaseService.firebaseAuth.signOut()

                    startActivity(Intent(context, LoginActivity::class.java))
                    activity?.finish()
                }
                .setNegativeButton("Cancelar") { dialog, which -> dialog.cancel() }.show()
        }

    }
}
