package com.david.redcristianauno.presentation.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
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
import com.david.redcristianauno.data.model.Red
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


        cgFilterEntity.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            if (chip != null) {
                showSearch(chip.text)
            } else {
                tilFindByEntity.visibility = View.GONE
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

        Log.i(TAG, "Collection: ${selectUser.values.first().names}")
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

    private fun showSearch(text: CharSequence?) {
        etFindByEntity.setText("")
        tilFindByEntity.visibility = View.VISIBLE
        tilFindByEntity.hint = "Escriba el $text"
    }

    private fun putHints(permision: String?) {
        when (permision) {
            "AT" -> {
                tvTitleCreate.text = "Nombre Red"
                tvLeaderCreateEntity.text = "Líder de Red"
            }
        }
    }

    private fun observedViewModel() {
        viewModel.users.observe(viewLifecycleOwner, Observer { users ->
            listAdapterUsers = context?.let { CreateEntityUserAdapter(it, users, this) }!!
            rvListUserEntity.adapter = listAdapterUsers
            /* Este metodo agranda el tamaño de cache para que no se repitan datos en memoria*/
            rvListUserEntity.setItemViewCacheSize(users.size)
        })
    }

    private val selectUser = mutableMapOf<MaterialCardView, User>()

    override fun onItemClickUser(cardView: MaterialCardView, user: User) {

        if (selectUser.isEmpty()) {
            selectUser[cardView] = user
        } else if (cardView.isChecked && selectUser.isNotEmpty()) {
            selectUser.clear()
        } else {
            val card: MaterialCardView = selectUser.keys.first()
            card.isChecked = !card.isChecked
            selectUser.clear()
            selectUser[cardView] = user
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