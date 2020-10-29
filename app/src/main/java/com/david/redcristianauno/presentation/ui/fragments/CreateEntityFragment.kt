package com.david.redcristianauno.presentation.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.david.redcristianauno.R
import com.david.redcristianauno.data.model.Celula
import com.david.redcristianauno.data.model.Red
import com.david.redcristianauno.data.model.Subred
import com.david.redcristianauno.data.model.User
import com.david.redcristianauno.data.network.Callback
import com.david.redcristianauno.data.network.ChurchRepositoryImpl
import com.david.redcristianauno.data.network.FirebaseService
import com.david.redcristianauno.data.network.UserRepositoryImpl
import com.david.redcristianauno.domain.ChurchUseCaseImpl
import com.david.redcristianauno.presentation.objectsUtils.SnackBarMD
import com.david.redcristianauno.presentation.objectsUtils.UserSingleton
import com.david.redcristianauno.presentation.ui.adapters.CreateEntityUserAdapter
import com.david.redcristianauno.presentation.viewmodel.CreateEntityViewModel
import com.david.redcristianauno.presentation.viewmodel.CreateEntityViewModelFactory
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_create_entity.*
import java.lang.Exception
import java.util.*

class CreateEntityFragment :
    DialogFragment(),
    CreateEntityUserAdapter.OnListEntityUserListener {
    val firebaseService = FirebaseService()
    private lateinit var listAdapterUsers: CreateEntityUserAdapter
    private var permission: String = ""

    /*Parameters for the RecyclerView when clicked*/
    private val selectUser = mutableMapOf<MaterialCardView, User>()
    private val listItemsColors = mutableListOf<Any>()
    private lateinit var textName: TextView
    private lateinit var textEmail: TextView
    private lateinit var ivCircle: ImageView

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
        val data = arguments?.getString("typeList")
        putHints(data)

        viewModel.listUsersFromFirebase()

        cgFilterEntity.setOnCheckedChangeListener{group, checkedId ->
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

            if(permission.isNotBlank()){
                viewModel.filter(permission)
            }else{
                viewModel.listUsersFromFirebase()
            }

        }


        cgFindEntity.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            if (chip != null) showSearch(chip.text)  else  tilFindByEntity.visibility = View.GONE
        }

        etFindByEntity.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun onTextChanged(char: CharSequence?, start: Int, before: Int, count: Int) {
                if (tilFindByEntity.hint == "Escriba el Nombre") {
                    viewModel.search(
                        permission, char.toString(), "names"
                    )
                }else if(tilFindByEntity.hint == "Escriba el Correo"){
                    viewModel.search(
                        permission, char.toString().toLowerCase(Locale.getDefault()).trim(), "email"
                    )
                }
            }
        })

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
            .collection(FirebaseService.USER_COLLECTION_NAME)
            .document(firebaseService.firebaseAuth.currentUser?.uid.toString())

        firebaseService.setDocumentWithID(
            red,
            "${FirebaseService.IGLESIA_COLLECTION_NAME}/" +
                    "${UserSingleton.getIdEntity("Iglesia")}/" +
                    FirebaseService.REDES_COLLECTION_NAME,
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
            .collection(FirebaseService.USER_COLLECTION_NAME)
            .document(firebaseService.firebaseAuth.currentUser?.uid.toString())

        firebaseService.setDocumentWithID(
            subred,
            "${FirebaseService.IGLESIA_COLLECTION_NAME}/" +
                    "${UserSingleton.getIdEntity("Iglesia")}/" +
                    "${FirebaseService.REDES_COLLECTION_NAME}/" +
                    "${arguments?.getString("red")!!}/" +
                    FirebaseService.SUBREDES_COLLECTION_NAME,
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
            .collection(FirebaseService.USER_COLLECTION_NAME)
            .document(firebaseService.firebaseAuth.currentUser?.uid.toString())

        firebaseService.setDocumentWithID(
            celula,
            "${FirebaseService.IGLESIA_COLLECTION_NAME}/" +
                    "${UserSingleton.getIdEntity("Iglesia")}/" +
                    "${FirebaseService.REDES_COLLECTION_NAME}/" +
                    "${arguments?.getString("red")!!}/" +
                    "${FirebaseService.SUBREDES_COLLECTION_NAME}/" +
                    "${arguments?.getString("subred")!!}/" +
                    FirebaseService.CELULA_COLLECTION_NAME,
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

    private fun showSearch(text: CharSequence?) {
        etFindByEntity.setText("")
        tilFindByEntity.visibility = View.VISIBLE
        tilFindByEntity.hint = "Escriba el $text"
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
        viewModel.users.observe(viewLifecycleOwner, Observer { users ->
            listAdapterUsers = context?.let { CreateEntityUserAdapter(it, users, this) }!!
            rvListUserEntity.adapter = listAdapterUsers
            /* Este metodo agranda el tamaño de cache para que no se repitan datos en memoria */
            rvListUserEntity.setItemViewCacheSize(users.size)
        })
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