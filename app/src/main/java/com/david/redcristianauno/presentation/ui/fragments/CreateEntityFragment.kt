package com.david.redcristianauno.presentation.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.david.redcristianauno.R
import com.david.redcristianauno.data.model.CreateEntityModel
import com.david.redcristianauno.data.network.ChurchRepositoryImpl
import com.david.redcristianauno.data.network.ConfigurationRepositoryImpl
import com.david.redcristianauno.data.network.UserRepositoryImpl
import com.david.redcristianauno.domain.ChurchUseCaseImpl
import com.david.redcristianauno.domain.ConfigurationUseCaseImpl
import com.david.redcristianauno.presentation.objectsUtils.UserSingleton
import com.david.redcristianauno.presentation.ui.adapters.CreateEntityAdapter
import com.david.redcristianauno.presentation.viewmodel.ConfigurationViewModel
import com.david.redcristianauno.presentation.viewmodel.ConfigurationViewModelFactory
import com.david.redcristianauno.presentation.viewmodel.CreateEntityViewModel
import com.david.redcristianauno.presentation.viewmodel.CreateEntityViewModelFactory
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_create_entity.*
import java.util.*

class CreateEntityFragment : DialogFragment(), CreateEntityAdapter.OnListEntityClickListener {

    private lateinit var listAdapter: CreateEntityAdapter

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            CreateEntityViewModelFactory(
                ChurchUseCaseImpl(
                    ChurchRepositoryImpl(),
                    UserRepositoryImpl()
                )
            )
        ).get(CreateEntityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_entity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarCreateEntity.navigationIcon =
            ContextCompat.getDrawable(view.context, R.drawable.ic_arrow_back)
        toolbarCreateEntity.setNavigationOnClickListener {
            dismiss()
        }
        val permision = arguments?.getString("permission")
        putHints(permision)

        viewModel.listUsersFromFirebase()
        viewModel.listSubredFromFirebase(
            UserSingleton.getIdEntity("Iglesia")!!,
            UserSingleton.getIdEntity("Red")!!
        )

        cgFilterEntity.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            if (chip != null) {
                showSearch(chip.text)
            } else {
                llSearchContainer.visibility = View.GONE
            }
        }

        etFindByEntity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun onTextChanged(char: CharSequence?, start: Int, before: Int, count: Int) {
                if (tilFindByEntity.hint == "Escriba el ID") {
                    viewModel.search(
                        char.toString().toLowerCase(Locale.getDefault()).trim(), "id"
                    )
                } else {
                    viewModel.search(
                        char.toString().toLowerCase(Locale.getDefault()).trim(), "email"
                    )
                }
            }
        })

        observedViewModel()
    }

    private fun showSearch(text: CharSequence?) {
        etFindByEntity.setText("")
        llSearchContainer.visibility = View.VISIBLE
        tilFindByEntity.hint = "Escriba el $text"
    }

    private fun putHints(permision: String?) {
        when (permision) {
            "AT" -> {
                tilCreateEntityName.hint = "Nombre Red"
                tvLeaderCreateEntity.text = "Líder de Red"
                tvChoiceCreateEntity.text = "Escoge máximo dos grupos de Subred"
            }
        }
    }

    private fun observedViewModel() {
        viewModel.users.observe(viewLifecycleOwner, Observer { users ->
            listAdapter = context?.let { CreateEntityAdapter(it, users, this) }!!
            rvListUserEntity.adapter = listAdapter
        })

        viewModel.subred.observe(viewLifecycleOwner, Observer { subredes ->
            listAdapter = context?.let { CreateEntityAdapter(it, subredes, this) }!!
            rvListEntity.adapter = listAdapter
        })

    }

    private var isOtherSelected = false

    override fun onItemClick(cardView: MaterialCardView, user: CreateEntityModel) {
        if (isOtherSelected){
            cardView.isChecked = !cardView.isChecked
            isOtherSelected = false
        }else{
            isOtherSelected = true
            cardView.isChecked = !cardView.isChecked
        }

    }

    companion object {
        const val TAG = "entityInfo"
    }
}