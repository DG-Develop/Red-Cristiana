package com.david.redcristianauno.presentation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.david.redcristianauno.R
import com.david.redcristianauno.data.network.ChurchRepositoryImpl
import com.david.redcristianauno.data.network.UserRepositoryImpl
import com.david.redcristianauno.domain.ChurchUseCaseImpl
import com.david.redcristianauno.presentation.objectsUtils.UserSingleton
import com.david.redcristianauno.presentation.ui.adapters.GeneralListAdapter
import com.david.redcristianauno.presentation.viewmodel.GeneralListViewModel
import com.david.redcristianauno.presentation.viewmodel.GeneralListViewModelFactory
import kotlinx.android.synthetic.main.fragment_general_list.*

class GeneralListFragment : DialogFragment() {

    private val user = UserSingleton.getUser()
    private lateinit var listAdapter: GeneralListAdapter
    private lateinit var type: String

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            GeneralListViewModelFactory(ChurchUseCaseImpl(ChurchRepositoryImpl(), UserRepositoryImpl()))
        ).get(GeneralListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_general_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarGeneralList.navigationIcon = ContextCompat.getDrawable(view.context, R.drawable.ic_arrow_back)
        toolbarGeneralList.setNavigationOnClickListener {
            dismiss()
        }

        viewModel.listRedesFromFirebase(UserSingleton.getIdEntity("Iglesia")!!)
        val data = arguments?.getString("permission")

        putTitle(data)
        observedViewModel()
        fab_new_item.setOnClickListener {
            val bundle = bundleOf("typeList" to type)
            findNavController().navigate(R.id.createEntityFragment, bundle)
        }
    }

    private fun observedViewModel(){
        viewModel.redes.observe(viewLifecycleOwner, Observer { redes->
            listAdapter = context?.let { GeneralListAdapter(it, redes) }!!
            rvListUserGeneral.adapter = listAdapter
        })
    }

    private fun putTitle(permission: String?) {
        when(permission){
            "Normal", "Lider Celula" -> {

            }
            "Admin"-> {
                val dataType = arguments?.getString("dataType")
                when (dataType) {
                    "Red" -> {
                        rvTitleGeneral.text = "Lista de Redes"
                        type = dataType
                    }
                    "Subred" -> {
                        rvTitleGeneral.text = "Lista de Subredes"
                        type = dataType
                    }
                    "Celula" -> {
                        rvTitleGeneral.text = "Lista de Celulas"
                        type = dataType
                    }
                }
            }
        }
    }

    companion object{
        const val TAG = "genInfo"
    }

}