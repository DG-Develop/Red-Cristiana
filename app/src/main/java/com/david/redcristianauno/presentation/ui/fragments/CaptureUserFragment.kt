package com.david.redcristianauno.presentation.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.david.redcristianauno.R
import com.david.redcristianauno.application.AppConstants.CAPTURE_USER_FRAGMENT
import com.david.redcristianauno.application.AppConstants.USER_COLLECTION_NAME
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.data.network.FirebaseService
import com.david.redcristianauno.domain.models.CellDataSource
import com.david.redcristianauno.domain.models.User
import com.david.redcristianauno.domain.models.UserDataSource
import com.david.redcristianauno.domain.models.asListCellDataSource
import com.david.redcristianauno.presentation.objectsUtils.SnackBarMD
import com.david.redcristianauno.presentation.ui.activities.LoginActivity
import com.david.redcristianauno.presentation.viewmodel.CaptureUserViewModel
import com.david.redcristianauno.vo.Resource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.DocumentReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_capture_user.*
import java.lang.Exception

@AndroidEntryPoint
class CaptureUserFragment : DialogFragment() {

    private val captureViewModel by viewModels<CaptureUserViewModel>()
    private lateinit var church: String
    private lateinit var user: UserDataSource
    private lateinit var listCell: List<CellDataSource>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_capture_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarCaptureUser.navigationIcon =
            ContextCompat.getDrawable(view.context, R.drawable.ic_arrow_back)
        toolbarCaptureUser.setNavigationOnClickListener {
            dismiss()
        }

        val user: User = arguments?.getParcelable("user")!!

        church = getIdEntity("Iglesia", user)
        captureViewModel.setChurch(church)

        dropdown_capture_red.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, _, _ ->
                val data = dropdown_capture_red.text.toString()
                captureViewModel.setNetwork(church, data)
            }

        dropdown_capture_subred.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, _, _ ->
                val data = dropdown_capture_subred.text.toString()
                captureViewModel.setSubNetwork(
                    church,
                    dropdown_capture_red.text.toString(),
                    data
                )
            }

        fab_send_capture.setOnClickListener {
            closeSession()
        }

        setupObservers()
    }

    private fun cellSelected(cell: String): CellDataSource? {
        return listCell.find { data -> data.id_celula == cell }
    }

    private fun captureUser() {
        val names = etNamesCapture.text.toString().trim()
        val last_name = etLastNamesCapture.text.toString().trim()
        val address = etAddressCapture.text.toString().trim()
        val telephone = etTelephoneCapture.text.toString().trim()
        val email = etEmailCapture.text.toString().trim()
        val password = etPassCapture.text.toString().trim()
        val confirm_password = etConfirmPassCapture.text.toString().trim()


        if (!TextUtils.isEmpty(names) &&
            !TextUtils.isEmpty(last_name) &&
            !TextUtils.isEmpty(address) &&
            !TextUtils.isEmpty(telephone) &&
            !TextUtils.isEmpty(email) &&
            !TextUtils.isEmpty(password) &&
            !TextUtils.isEmpty(confirm_password)
        ) {
            if (password == confirm_password) {

                captureViewModel.setCredentials(email, password)
                val churchReference = captureViewModel.getPathCellFromFirebase(
                    church,
                    dropdown_capture_red.text.toString(),
                    dropdown_capture_subred.text.toString(),
                    dropdown_capture_celula.text.toString()
                )

                user = UserDataSource(
                    names = names,
                    last_names = last_name,
                    address = address,
                    telephone = telephone,
                    email = email,
                    permission = listOf("Postulado"),
                    iglesia_references = churchReference
                )
            } else {
                SnackBarMD.getSBIndefinite(fab_send_capture, "Contraseñas no coinciden")
            }
        }
    }

    private fun setupObservers() {
        captureViewModel.createUserAuthFromFirebase().observe(this, Observer { result ->
            when (result) {
                is Resource.Loading -> rlBaseCapture.visibility = View.VISIBLE
                is Resource.Success -> {
                    val id = result.data?.user?.uid.toString()
                    val cell = cellSelected(dropdown_capture_celula.text.toString())
                    val firebaseService = FirebaseService()
                    val document: DocumentReference = firebaseService.firebaseFirestore.collection(
                        USER_COLLECTION_NAME
                    ).document(id)
                    cell?.users?.add(document)

                    val fields = mapOf<String, Any>(
                        "users" to cell?.users!!
                    )

                    captureViewModel.updatedCellFromFirebase(
                        church,
                        dropdown_capture_red.text.toString(),
                        dropdown_capture_subred.text.toString(),
                        dropdown_capture_celula.text.toString(),
                        fields
                    )

                    user.id = id
                    captureViewModel.createUserFirestoreFromFirebase(
                        user,
                        object : Callback<Void> {
                            override fun OnSucces(result: Void?) {
                                captureViewModel.signOut()
                                startActivity(Intent(requireActivity(), LoginActivity::class.java))
                                requireActivity().finish()
                            }

                            override fun onFailure(exception: Exception) {
                                SnackBarMD.getSBIndefinite(
                                    fab_send_capture,
                                    "Error al crear al usuario"
                                )
                            }
                        })
                }
                is Resource.Failure -> {
                    if (result.exception is FirebaseAuthUserCollisionException) {
                        SnackBarMD.getSBIndefinite(fab_send_capture, "Este correo ya esta en uso")
                    } else {
                        SnackBarMD.getSBNormal(fab_send_capture, result.exception.message!!)
                    }
                    rlBaseCapture.visibility = View.GONE
                }
            }
        })

        captureViewModel.fetchNetwork.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> Log.i(CAPTURE_USER_FRAGMENT, "Cargando Red...")
                is Resource.Success -> {
                    val list = result.data.map { net -> net.id_red }
                    dropdown_capture_red.setText(list[0])

                    val adapter =
                        ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, list)

                    captureViewModel.setNetwork(church, list[0])

                    dropdown_capture_red.setAdapter(adapter)
                }
                is Resource.Failure -> Log.e(CAPTURE_USER_FRAGMENT, "Error: ${result.exception}")
            }
        })

        captureViewModel.fetchSubNetwork.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> Log.i(CAPTURE_USER_FRAGMENT, "Cargando Subred...")
                is Resource.Success -> {
                    val list = result.data.map { net -> net.id_subred }
                    
                    val firstData = if (list.isEmpty()) "" else list[0]
                    dropdown_capture_subred.setText(firstData)

                    val adapter =
                        ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, list)

                    if (firstData.isBlank()) {
                        dropdown_capture_celula.setText("")
                    } else {
                        captureViewModel.setSubNetwork(
                            church,
                            dropdown_capture_red.text.toString(),
                            firstData
                        )
                    }

                    dropdown_capture_subred.setAdapter(adapter)
                }
                is Resource.Failure -> Log.e(CAPTURE_USER_FRAGMENT, "Error: ${result.exception}")
            }
        })

        captureViewModel.fetchCell.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> Log.i(CAPTURE_USER_FRAGMENT, "Cargando Celula...")
                is Resource.Success -> {
                    val data = result.data.asListCellDataSource()
                    listCell = data
                    val list = data.map { net -> net.id_celula }
                    val firstData = if (list.isEmpty()) "" else list[0]
                    dropdown_capture_celula.setText(firstData)

                    val adapter =
                        ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, list)
                    dropdown_capture_celula.setAdapter(adapter)

                }
                is Resource.Failure -> Log.e(CAPTURE_USER_FRAGMENT, "Error: ${result.exception}")
            }
        })
    }

    private fun closeSession() {
        MaterialAlertDialogBuilder(
            requireContext(),
            R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog
        )
            .setTitle("Capturar Usuario")
            .setMessage("Por motivos de seguridad al momento de crear " +
                    "un usuario se requiere cerrar el inicio de sesión actual, " +
                    "¿Desea continuar?")
            .setPositiveButton("Aceptar"){ _, _ ->
                captureUser()
            }
            .setNegativeButton("Cancelar"){dialog, _ ->
                dialog.cancel()
            }.show()
    }

    private fun getIdEntity(type: String, user: User?): String {
        val documents = user?.iglesia_references?.split("/")
        if (documents != null) {
            return when (type) {
                "Iglesia" -> documents[1]
                "Red" -> documents[3]
                "Subred" -> documents[5]
                "Celula" -> documents[7]
                else -> ""
            }
        }
        return ""
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

}