package com.david.redcristianauno.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.david.redcristianauno.R
import kotlinx.android.synthetic.main.fragment_create_entity.*

class CreateEntityFragment : DialogFragment() {

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
        toolbarCreateEntity.navigationIcon = ContextCompat.getDrawable(view.context, R.drawable.ic_arrow_back)
        toolbarCreateEntity.setNavigationOnClickListener {
            dismiss()
        }
        val permision = arguments?.getString("permission")
        putHints(permision)
    }

    private fun putHints(permision: String?) {
        when(permision){
            "AT" -> {
                tilCreateEntityName.hint = "Nombre Red"
                tvLeaderCreateEntity.text = "Líder de Red"
                tilFindByEntity.hint = "Nombre Red"
                tvChoiceCreateEntity.text = "Escoge máximo dos grupos de Subred"
            }
        }
    }

}