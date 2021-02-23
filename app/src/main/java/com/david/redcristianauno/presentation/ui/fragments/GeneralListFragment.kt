package com.david.redcristianauno.presentation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.david.redcristianauno.R
import com.david.redcristianauno.application.AppConstants.GENERAL_LIST_FRAGMENT
import com.david.redcristianauno.data.network.ChurchRepositoryImpl
import com.david.redcristianauno.data.network.UserRepositoryImpl
import com.david.redcristianauno.domain.ChurchUseCaseImpl
import com.david.redcristianauno.domain.models.User
import com.david.redcristianauno.domain.models.asListGeneralModel
import com.david.redcristianauno.presentation.ui.adapters.GeneralListAdapter
import com.david.redcristianauno.presentation.viewmodel.GeneralListViewModel
import com.david.redcristianauno.presentation.viewmodel.GeneralListViewModelFactory
import com.david.redcristianauno.vo.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_general_list.*

@AndroidEntryPoint
class GeneralListFragment : DialogFragment() {

    private lateinit var listAdapter: GeneralListAdapter
    private lateinit var type: String
    private lateinit var userData: User

    private val generalListViewModel by viewModels<GeneralListViewModel> ()

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            GeneralListViewModelFactory(
                ChurchUseCaseImpl(
                    ChurchRepositoryImpl(),
                    UserRepositoryImpl()
                )
            )
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
        toolbarGeneralList.navigationIcon =
            ContextCompat.getDrawable(view.context, R.drawable.ic_arrow_back)
        toolbarGeneralList.setNavigationOnClickListener {
            findNavController().navigate(R.id.navSettingFragment)
        }

         userData= arguments?.getParcelable("user")!!

        putTitle()

        dropdown_entity_general_red.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, _, _ ->
                val red = dropdown_entity_general_red.text.toString()
                fillListOrFillDropdownEntity(red)
            }

        dropdown_entity_general_subred.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, _, _ ->
                val subred = dropdown_entity_general_subred.text.toString()
                viewModel.listCelulasFromFirebase(
                    getIdEntity("Iglesia", userData),
                    dropdown_entity_general_red.text.toString(),
                    subred
                )
            }

        fab_new_item.setOnClickListener {
            val bundle = bundleOf(
                "typeList" to type,
                "red" to dropdown_entity_general_red.text.toString(),
                "subred" to dropdown_entity_general_subred.text.toString(),
                "user" to userData
            )
            findNavController().navigate(R.id.createEntityFragment, bundle)
        }

        observedViewModel()
    }

    private fun fillListOrFillDropdownEntity(name: String) {
        when (arguments?.getString("dataType")) {
            "Subred" -> {
                viewModel.listSubredesFromFirebase(getIdEntity("Iglesia", userData), name)
            }
            "Celula" -> {
                viewModel.fillTilSubred(getIdEntity("Iglesia", userData), name)
            }
        }
    }

    private fun observedViewModel() {
//        viewModel.redes.observe(viewLifecycleOwner, Observer { redes ->
//            listAdapter = context?.let { GeneralListAdapter(it, redes) }!!
//            rvListUserGeneral.adapter = listAdapter
//        })

        generalListViewModel.fetchNetwork.observe(viewLifecycleOwner, Observer { result->
            when(result){
                is Resource.Loading -> { Log.i(GENERAL_LIST_FRAGMENT, "Cargando...")}
                is Resource.Success -> {
                    listAdapter = GeneralListAdapter(requireContext(), result.data.asListGeneralModel())
                }
                is Resource.Failure -> {
                    Log.e(GENERAL_LIST_FRAGMENT, "Error al traer los datos", result.exception)
                }
            }
        })

        viewModel.subredes.observe(viewLifecycleOwner, Observer { subredes ->
            listAdapter = context?.let { GeneralListAdapter(it, subredes) }!!
            rvListUserGeneral.adapter = listAdapter
        })

        viewModel.celulas.observe(viewLifecycleOwner, Observer { celulas ->
            listAdapter = context?.let { GeneralListAdapter(it, celulas) }!!
            rvListUserGeneral.adapter = listAdapter
        })

        // dropdown observes
        viewModel.dropdownRed.observe(viewLifecycleOwner, Observer { listRedes ->
            var firstData = ""
            if (listRedes.isNotEmpty()) {
                firstData = listRedes[0]
            }

            dropdown_entity_general_red.setText(firstData)

            val adapter =
                ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, listRedes)
            dropdown_entity_general_red.setAdapter(adapter)

            if(arguments?.getString("dataType") == "Celula"){
                viewModel.fillTilSubred(
                    getIdEntity("Iglesia", userData),
                    firstData
                )
            }else{
                viewModel.listSubredesFromFirebase(getIdEntity("Iglesia", userData), firstData)
            }

        })

        viewModel.dropdownSubred.observe(viewLifecycleOwner, Observer { listSubredes ->
            var firstData = ""
            if (listSubredes.isNotEmpty()) {
                firstData = listSubredes[0]
            }
            dropdown_entity_general_subred.setText(firstData)

            val adapter =
                ArrayAdapter(requireContext(), R.layout.dropdown_menu_popup_item, listSubredes)
            dropdown_entity_general_subred.setAdapter(adapter)

            if (dropdown_entity_general_red.text.toString().isNotEmpty()) {
                viewModel.listCelulasFromFirebase(
                    getIdEntity("Iglesia", userData),
                    dropdown_entity_general_red.text.toString(),
                    firstData
                )
            }
        })
    }

    private fun putTitle() {
        if(userData.permission.contains("Admin")){
            when (val dataType = arguments?.getString("dataType")) {
                "Red" -> {
                    generalListViewModel.setChurch(getIdEntity("Iglesia", userData))
//                    viewModel.listRedesFromFirebase(getIdEntity("Iglesia", userData))
                    rvTitleGeneral.text = "Lista de Redes"
                    type = dataType
                }
                "Subred" -> {
                    viewModel.fillTilRed(getIdEntity("Iglesia", userData))
                    mcvEntityContainer.visibility = View.VISIBLE
                    rvTitleGeneral.text = "Lista de Subredes"
                    type = dataType
                }
                "Celula" -> {
                    viewModel.fillTilRed(getIdEntity("Iglesia", userData))
                    tvSelectContainerSubred.visibility = View.VISIBLE
                    tilEntityGeneralSubred.visibility = View.VISIBLE
                    mcvEntityContainer.visibility = View.VISIBLE
                    rvTitleGeneral.text = "Lista de Celulas"
                    type = dataType
                }
            }
        }
    }

    private fun getIdEntity(type: String, user: User?):String {
        val documents = user?.iglesia_references?.split("/")
        if (documents != null) {
            return when(type){
                "Iglesia" -> documents[1]
                "Red" -> documents[3]
                "Subred" -> documents[5]
                "Celula" -> documents[7]
                else -> ""
            }
        }
        return ""
    }

}