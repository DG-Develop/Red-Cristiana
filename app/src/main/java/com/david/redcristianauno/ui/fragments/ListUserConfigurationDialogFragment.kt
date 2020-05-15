package com.david.redcristianauno.ui.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.david.redcristianauno.R
import com.david.redcristianauno.domain.ConfigurationUseCaseImpl
import com.david.redcristianauno.model.network.ConfigurationRepositoryImpl
import com.david.redcristianauno.ui.adapters.ListUserConfigurationAdapter
import com.david.redcristianauno.viewmodel.ConfigurationViewModel
import com.david.redcristianauno.viewmodel.ConfigurationViewModelFactory
import kotlinx.android.synthetic.main.fragment_list_user_configuration_dialog.*

/**
 * A simple [Fragment] subclass.
 */
class ListUserConfigurationDialogFragment : DialogFragment() {

    private lateinit var listUserAdapter: ListUserConfigurationAdapter
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ConfigurationViewModelFactory(ConfigurationUseCaseImpl(ConfigurationRepositoryImpl()))
        ).get(ConfigurationViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_user_configuration_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarBackListUserConfiguration.navigationIcon =
            ContextCompat.getDrawable(view.context, R.drawable.ic_arrow_back)
        toolbarBackListUserConfiguration.setNavigationOnClickListener {
            dismiss()
        }
        listUserAdapter = ListUserConfigurationAdapter(tiSearchConfiguration, llCrudConfiguration)
        rvListUser.adapter = listUserAdapter

        val user = arguments?.getString("user")
        viewModel.refresh(user!!)

        ibMoveUserConfigurationDialog.setOnClickListener {
            if (listUserAdapter.userChecked.size > 0) {
                context?.let {
                    AlertDialog.Builder(it)
                        .setTitle("Elija un permiso")
                        .setItems(R.array.permissions) { _, which ->
                            val array = resources.getStringArray(R.array.permissions)
                            val data = array[which]
                            rlBaseListUser.visibility = View.VISIBLE
                            for (userChecked in listUserAdapter.userChecked) {
                                viewModel.updateUserFromFirebase(userChecked.id, data)
                            }
                            viewModel.refresh(user)
                            listUserAdapter.userChecked.clear()
                            tiSearchConfiguration.visibility = View.VISIBLE
                            llCrudConfiguration.visibility = View.GONE
                        }
                        .show()
                }
            }
        }

        ibDeleteUserConfigurationDialog.setOnClickListener {
            if (listUserAdapter.userChecked.size > 0) {
                context?.let { it1 ->
                    AlertDialog.Builder(it1)
                        .setTitle("Eliminar")
                        .setMessage("¿Estás seguro que quieres eliminar?")
                        .setPositiveButton("Aceptar") { _, _ ->
                            rlBaseListUser.visibility = View.VISIBLE
                            //Log.i("UserInfo", "El tamaño de los datos checkeados es: ${listUserAdapter.userChecked.size}")
                            for (userChecked in listUserAdapter.userChecked) {
                                viewModel.deleteUserFromFirebase(userChecked.id)
                            }
                            viewModel.refresh(user)
                            listUserAdapter.userChecked.clear()
                            tiSearchConfiguration.visibility = View.VISIBLE
                            llCrudConfiguration.visibility = View.GONE
                        }
                        .setNegativeButton("Cancelar") { dialog, _ ->
                            dialog.cancel()
                        }.show()
                }

            }
        }

        etSearch.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.search(etSearch.text.toString())
                true
            } else {
                false
            }
        }

        observedViewModel()
        observedUsersSelected()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    private fun observedViewModel() {
        viewModel.listUser.observe(viewLifecycleOwner, Observer { schedule ->
            listUserAdapter.updateuser(schedule)
        })
        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                rlBaseListUser.visibility = View.GONE
            }
        })
    }

    private fun observedUsersSelected() {
        listUserAdapter.checked.observe(viewLifecycleOwner, Observer {
            if (!it) {
                if (listUserAdapter.userChecked.size <= 0) {
                    tiSearchConfiguration.visibility = View.VISIBLE
                    llCrudConfiguration.visibility = View.GONE
                }
            }
        })
    }

}
