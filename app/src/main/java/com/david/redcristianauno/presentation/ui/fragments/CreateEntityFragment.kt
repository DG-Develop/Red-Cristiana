package com.david.redcristianauno.presentation.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.david.redcristianauno.R
import com.david.redcristianauno.application.AppConstants.CELL_COLLECTION_NAME
import com.david.redcristianauno.application.AppConstants.CHURCH_COLLECTION_NAME
import com.david.redcristianauno.application.AppConstants.CREATE_ENTITY_FRAGMENT
import com.david.redcristianauno.application.AppConstants.NET_COLLECTION_NAME
import com.david.redcristianauno.application.AppConstants.SUBNET_COLLECTION_NAME
import com.david.redcristianauno.application.AppConstants.USER_COLLECTION_NAME
import com.david.redcristianauno.data.model.Celula
import com.david.redcristianauno.data.model.Red
import com.david.redcristianauno.data.model.Subred
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.data.network.FirebaseService
import com.david.redcristianauno.domain.models.User
import com.david.redcristianauno.presentation.objectsUtils.SnackBarMD
import com.david.redcristianauno.presentation.objectsUtils.UserSingleton
import com.david.redcristianauno.presentation.ui.adapters.CreateEntityUserAdapter
import com.david.redcristianauno.presentation.viewmodel.CreateEntityViewModel
import com.david.redcristianauno.vo.Resource
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_create_entity.*
import java.lang.Exception
import java.util.*

@AndroidEntryPoint
class CreateEntityFragment :
    DialogFragment(),
    CreateEntityUserAdapter.OnListEntityUserListener {
    val firebaseService = FirebaseService()
    private lateinit var listAdapterUsers: CreateEntityUserAdapter
    private var permission: String = ""
    private lateinit var listUsers: List<User>
    private var listFilterUsers: List<User> = listOf()
    private lateinit var user: User

    /*Parameters for the RecyclerView when clicked*/
    private val selectUser = mutableMapOf<MaterialCardView, User>()
    private val listItemsColors = mutableListOf<Any>()
    private lateinit var textName: TextView
    private lateinit var textEmail: TextView
    private lateinit var ivCircle: ImageView

    private val createEntityViewModel by viewModels<CreateEntityViewModel>()

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
        val data = arguments?.getString("typeList")
        putHints(data)

        user = arguments?.getParcelable("user")!!

        createEntityViewModel.getListUsersFromFirebase()


        cgFilterEntity.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val filter = if (chip != null) chip.text else ""
            permission = when (filter) {
                "Miembro" -> {
                    "Normal"
                }
                "Celula" -> {
                    "Lider Celula"
                }
                else -> {
                    filter.toString()
                }
            }

            if (permission.isNotBlank()) {
                val list = filterUser(permission)
                setupRecycler(list)
            } else {
                setupRecycler(listUsers)
            }
            svFindByEntity.setQuery("", false)
        }

        cgFindEntity.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            if (chip != null) showSearch(chip.text) else svFindByEntity.visibility = View.GONE
        }

        setupSearchView()

        fab_send_entity.setOnClickListener {
            createEntity(it)
        }

        observedViewModel()
    }

    private fun createEntity(view: View) {
        val name = etCreateEntityName.text.toString().trim()

        if (TextUtils.isEmpty(name)) {
            return SnackBarMD.getSBIndefinite(view, "Ponga un nombre a la red")
        }

        if (selectUser.isEmpty()) {
            return SnackBarMD.getSBIndefinite(view, "Por favot seleccione a un lider de red")
        }

        when (arguments?.getString("typeList")) {
            "Celula" -> createCelula(name, view)
            "Subred" -> createSubred(name, view)
            "Red" -> createRed(name, view)
        }
    }

    private fun createRed(name: String, view: View) {
        val red = Red()
        red.id_red = name
        red.name_leader = selectUser.values.first().names
        red.created_by = firebaseService.firebaseFirestore
            .collection(USER_COLLECTION_NAME)
            .document(firebaseService.firebaseAuth.currentUser?.uid.toString())

        firebaseService.setDocumentWithID(
            red,
            "${CHURCH_COLLECTION_NAME}/" +
                    "${getIdEntity("Iglesia", user)}/" +
                    NET_COLLECTION_NAME,
            name,
            object : Callback<Void> {
                override fun OnSucces(result: Void?) {
                    SnackBarMD.getSBNormal(view, "Registrado con exito")
                }

                override fun onFailure(exception: Exception) {
                    SnackBarMD.getSBNormal(
                        view,
                        "Hubo un error en el registro intentelo mas tarde."
                    )
                }
            })
    }

    private fun createSubred(name: String, view: View) {
        val subred = Subred()
        subred.id_subred = name
        subred.name_leader = selectUser.values.first().names
        subred.created_by = firebaseService.firebaseFirestore
            .collection(USER_COLLECTION_NAME)
            .document(firebaseService.firebaseAuth.currentUser?.uid.toString())

        firebaseService.setDocumentWithID(
            subred,
            "${CHURCH_COLLECTION_NAME}/" +
                    "${getIdEntity("Iglesia", user)}/" +
                    "${NET_COLLECTION_NAME}/" +
                    "${arguments?.getString("red")!!}/" +
                    SUBNET_COLLECTION_NAME,
            name,

            object : Callback<Void> {
                override fun OnSucces(result: Void?) {
                    SnackBarMD.getSBNormal(view, "Registrado con exito")
                }

                override fun onFailure(exception: Exception) {
                    SnackBarMD.getSBNormal(
                        view,
                        "Hubo un error en el registro intentelo mas tarde."
                    )
                }
            })
    }

    private fun createCelula(name: String, view: View) {
        val celula = Celula()
        celula.id_celula = name
        celula.name_leader = selectUser.values.first().names
        celula.created_by = firebaseService.firebaseFirestore
            .collection(USER_COLLECTION_NAME)
            .document(firebaseService.firebaseAuth.currentUser?.uid.toString())

        firebaseService.setDocumentWithID(
            celula,
            "${CHURCH_COLLECTION_NAME}/" +
                    "${getIdEntity("Iglesia", user)}/" +
                    "${NET_COLLECTION_NAME}/" +
                    "${arguments?.getString("red")!!}/" +
                    "${SUBNET_COLLECTION_NAME}/" +
                    "${arguments?.getString("subred")!!}/" +
                    CELL_COLLECTION_NAME,
            name,

            object : Callback<Void> {
                override fun OnSucces(result: Void?) {
                    SnackBarMD.getSBNormal(view, "Registrado con exito")
                }

                override fun onFailure(exception: Exception) {
                    SnackBarMD.getSBNormal(
                        view,
                        "Hubo un error en el registro intentelo mas tarde."
                    )
                }
            })
    }

    private fun setupRecycler(list: List<User>) {
        listAdapterUsers = CreateEntityUserAdapter(requireContext(), list, this)
        rvListUserEntity.adapter = listAdapterUsers
        /* Este metodo agranda el tamaño de cache para que no se repitan datos en memoria */
        rvListUserEntity.setItemViewCacheSize(list.size)
    }

    private fun setupSearchView() {
        svFindByEntity.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query != null) {
                    var listSearch: List<User> = listUsers
                    if (svFindByEntity.queryHint == "Escriba el Nombre") {
                        listSearch = searchUser(query.toLowerCase(Locale.ROOT), "names")
                    } else if (svFindByEntity.queryHint == "Escriba el Correo") {
                        listSearch = searchUser(query.toLowerCase(Locale.ROOT), "email")
                    }

                    setupRecycler(listSearch)
                }

                return false
            }
        })
    }

    private fun showSearch(text: CharSequence?) {
        svFindByEntity.setQuery("", false)
        svFindByEntity.visibility = View.VISIBLE
        svFindByEntity.queryHint = "Escriba el $text"
    }

    private fun putHints(permision: String?) {
        when (permision) {
            "Celula" -> {
                tvTitleCreate.text = "Nombre Celula"
                tvLeaderCreateEntity.text = "Líder de Celula"
            }
            "Subred" -> {
                tvTitleCreate.text = "Nombre Subred"
                tvLeaderCreateEntity.text = "Líder de Subred"
            }
            "Red" -> {
                tvTitleCreate.text = "Nombre Red"
                tvLeaderCreateEntity.text = "Líder de Red"
            }
        }
    }

    private fun observedViewModel() {
        createEntityViewModel.getListUsersFromFirebase().observe(
            viewLifecycleOwner,
            Observer { result ->
                when (result) {
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        setupRecycler(result.data)
                        listUsers = result.data
                    }
                    is Resource.Failure -> {
                        Log.i(CREATE_ENTITY_FRAGMENT, "Error: ${result.exception}")
                    }
                }

            })
    }

    private fun searchUser(query: String, key: String): List<User> {
        val list = if(listFilterUsers.isNullOrEmpty()) listUsers else listFilterUsers

        return when (key) {
            "names" -> {
                list.filter { user ->
                    user.names
                        .toLowerCase(Locale.ROOT)
                        .startsWith(query)
                        .or(user.names.endsWith(query + "\uf8ff"))
                }
            }
            "email" -> {
                list.filter { user ->
                    user.email
                        .toLowerCase(Locale.ROOT)
                        .startsWith(query)
                        .or(user.email.endsWith(query + "\uf8ff"))
                }
            }
            else -> list
        }
    }

    private fun filterUser(permission: String):List<User>{
        listFilterUsers = listUsers.filter { user->
            user.permission.contains(permission)
        }
        return listFilterUsers
    }

    override fun onItemClickUser(
        cardView: MaterialCardView,
        iv: ImageView,
        tvName: TextView,
        tvEmail: TextView,
        user: User
    ) {

        if (selectUser.isEmpty() && listItemsColors.isEmpty()) {
            selectUser[cardView] = user

            listItemsColors.add(tvName)
            listItemsColors.add(iv)
            listItemsColors.add(tvEmail)

            textName = listItemsColors[0] as TextView
            textName.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))

            ivCircle = listItemsColors[1] as ImageView
            ivCircle.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.colorAccent),
                android.graphics.PorterDuff.Mode.SRC_IN
            )

            textEmail = listItemsColors[2] as TextView
            textEmail.setTextColor(ContextCompat.getColor(requireContext(), R.color.AccentLow))

        } else if (cardView.isChecked && selectUser.isNotEmpty() && listItemsColors.isNotEmpty()) {
            textName.setTextColor(ContextCompat.getColor(requireContext(), R.color.Gray))
            ivCircle.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.Gray),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
            textEmail.setTextColor(ContextCompat.getColor(requireContext(), R.color.GraySecondary))

            listItemsColors.clear()
            selectUser.clear()
        } else {
            val card: MaterialCardView = selectUser.keys.first()
            card.isChecked = !card.isChecked
            textName.setTextColor(ContextCompat.getColor(requireContext(), R.color.Gray))
            textEmail.setTextColor(ContextCompat.getColor(requireContext(), R.color.GraySecondary))
            ivCircle.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.Gray),
                android.graphics.PorterDuff.Mode.SRC_IN
            )

            listItemsColors.clear()
            selectUser.clear()

            selectUser[cardView] = user
            listItemsColors.add(tvName)
            listItemsColors.add(iv)
            listItemsColors.add(tvEmail)

            textName = listItemsColors[0] as TextView
            textName.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))

            ivCircle = listItemsColors[1] as ImageView
            ivCircle.setColorFilter(
                ContextCompat.getColor(requireContext(), R.color.colorAccent),
                android.graphics.PorterDuff.Mode.SRC_IN
            )

            textEmail = listItemsColors[2] as TextView
            textEmail.setTextColor(ContextCompat.getColor(requireContext(), R.color.AccentLow))
        }

        cardView.isChecked = !cardView.isChecked
        cardView.isFocusable = !cardView.isFocusable

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

    companion object {
        const val TAG = "entityInfo"
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}