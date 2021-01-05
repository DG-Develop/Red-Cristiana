package com.david.redcristianauno.presentation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.david.redcristianauno.R
import com.david.redcristianauno.application.AppConstants.LIST_USER_FRAGMENT
import com.david.redcristianauno.domain.ConfigurationUseCaseImpl
import com.david.redcristianauno.data.network.ConfigurationRepositoryImpl
import com.david.redcristianauno.domain.models.User
import com.david.redcristianauno.presentation.ui.adapters.UserAdapter
import com.david.redcristianauno.presentation.viewmodel.ConfigurationViewModel
import com.david.redcristianauno.presentation.viewmodel.ConfigurationViewModelFactory
import com.david.redcristianauno.presentation.viewmodel.ListUserConfigurationViewModel
import com.david.redcristianauno.vo.Resource
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_list_user_configuration_dialog.*

@AndroidEntryPoint
class ListUserConfigurationDialogFragment : DialogFragment(), UserAdapter.OnListUserClickListener{

    private lateinit var listAdapter: UserAdapter
    private val userChecked = ArrayList<User>()
    private val checked = MutableLiveData<Boolean>()
    private val listUserConfigurationViewModel by viewModels<ListUserConfigurationViewModel>()
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_user_configuration_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarBackListUserConfiguration.navigationIcon =
            ContextCompat.getDrawable(view.context, R.drawable.ic_arrow_back)
        toolbarBackListUserConfiguration.setNavigationOnClickListener {
            dismiss()
        }

        val user = arguments?.getString("user")

        listUserConfigurationViewModel.setFilter(listOf(user!!))

        ibDeleteUserConfigurationDialog.setOnClickListener {
            if (userChecked.size > 0) {
                MaterialAlertDialogBuilder(
                    requireContext(),
                    R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog
                )
                    .setTitle("Eliminar")
                    .setMessage("¿Estás seguro que quieres eliminar?")
                    .setPositiveButton("Eliminar") { _, _ ->
                        rlBaseListUser.visibility = View.VISIBLE
                        for (userChecked in userChecked) {
                            viewModel.deleteUserFromFirebase(userChecked.id)
                        }
                        viewModel.refresh(user)
                        userChecked.clear()
                        tiSearchConfiguration.visibility = View.VISIBLE
                        llCrudConfiguration.visibility = View.GONE
                    }
                    .setNegativeButton("Cancelar") { dialog, _ ->
                        dialog.cancel()
                    }.show()
            }
        }

        ibMoveUserConfigurationDialog.setOnClickListener {
            if (userChecked.size > 0) {
                context?.let {
                    MaterialAlertDialogBuilder(
                        requireContext(),
                        R.style.Body_ThemeOverlay_MaterialComponents_MaterialAlertDialog
                    )
                        .setTitle("Elija un permiso")
                        .setItems(R.array.permissions) { _, which ->
                            val array = resources.getStringArray(R.array.permissions)
                            val data = array[which]
                            rlBaseListUser.visibility = View.VISIBLE
                            for (userSelected in userChecked) {
                                /*viewModel.updateUserFromFirebase(userSelected.id, data)*/
                                listUserConfigurationViewModel.updateUserFromFirestore(
                                    mapOf("permission" to data)
                                )
                            }
                            viewModel.refresh(user)
                            userChecked.clear()
                            tiSearchConfiguration.visibility = View.VISIBLE
                            llCrudConfiguration.visibility = View.GONE
                        }.show()
                }
            }
        }

        etSearch.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.search(etSearch.text.toString(), "names")
                true
            } else {
                false
            }
        }

        setupObservers()
        observedUsersSelected()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    private fun setupObservers(){
        listUserConfigurationViewModel.getListUserFromFirebase().observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Loading -> rlBaseListUser.visibility = View.VISIBLE
                is Resource.Success -> {
                    rlBaseListUser.visibility = View.GONE

                    listAdapter = UserAdapter(requireContext(), result.data, this)
                    rvListUser.adapter = listAdapter
                }
                is Resource.Failure -> {
                    rlBaseListUser.visibility = View.GONE
                    Log.i(LIST_USER_FRAGMENT, "Error ${result.exception.message}")
                }
            }
        })
    }

    private fun observedUsersSelected() {
        checked.observe(viewLifecycleOwner, Observer {
            if (!it) {
                if (userChecked.size <= 0) {
                    tiSearchConfiguration.visibility = View.VISIBLE
                    llCrudConfiguration.visibility = View.GONE
                }
            }
        })
    }

    override fun onItemLongClick(cardView: MaterialCardView, user: User): Boolean {
        cardView.isChecked = !cardView.isChecked
        cardView.isFocusable = !cardView.isFocusable
        if (cardView.isChecked){
            llCrudConfiguration.visibility = View.VISIBLE
            tiSearchConfiguration.visibility = View.GONE
            userChecked.add(user)
            checked.value = cardView.isChecked
        }else{
            userChecked.remove(user)
            checked.value = cardView.isChecked
        }
        return true
    }
}
