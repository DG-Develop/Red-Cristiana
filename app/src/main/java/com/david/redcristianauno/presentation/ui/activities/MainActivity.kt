package com.david.redcristianauno.presentation.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.lifecycle.Observer
import com.david.redcristianauno.R
import com.david.redcristianauno.application.AppConstants.MAIN_ACTIVITY
import com.david.redcristianauno.domain.HistoricalWeeklyUseCaseImpl
import com.david.redcristianauno.data.network.FirebaseService
import com.david.redcristianauno.data.network.HistoricalWeeklyRepositoryImpl
import com.david.redcristianauno.data.preferences.ActivityPreferences
import com.david.redcristianauno.presentation.objectsUtils.UserSingleton
import com.david.redcristianauno.presentation.viewmodel.MainViewModel
import com.david.redcristianauno.presentation.viewmodel.MainViewModelFactory
import com.david.redcristianauno.presentation.viewmodel.MainViewModelP
import com.david.redcristianauno.vo.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DateFormat
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val firebaseService = FirebaseService()
    private var permission: String? = null
    private val user = UserSingleton.getUser()

    private val mainViewModel by viewModels<MainViewModelP>()

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            MainViewModelFactory(HistoricalWeeklyUseCaseImpl(HistoricalWeeklyRepositoryImpl()))
        ).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setActionBar(findViewById(R.id.toolbarMain))
        configNav()
        observedDayOfWeek()
        observedPermission()

        mainViewModel.setSearchUser("8VZSKi5HmVITr30p7WQi")
        setupObservers()
    }

    private fun configNav() {
        NavigationUI.setupWithNavController(
            bnvMenu, Navigation.findNavController(
                this,
                R.id.fragContent
            )
        )
    }

    private fun setupObservers(){
        mainViewModel.fetchUserId.observe(this, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                    Log.i(MAIN_ACTIVITY, "Cargando")
                }
                is Resource.Success -> {
                    Log.i(MAIN_ACTIVITY, "Datos: ${result.data}")
                }
                is Resource.Failure -> {
                    Log.i(MAIN_ACTIVITY, "Error: ${result.exception}")
                }
            }
        })
    }

    private fun observedDayOfWeek() {
        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.TUESDAY

        val dayOfWeek = calendar[Calendar.DAY_OF_WEEK]

        if (dayOfWeek == calendar.firstDayOfWeek) {
            val currentDateString =
                DateFormat.getDateInstance(DateFormat.FULL).format(calendar.time)
            viewModel.getDataHistoryWeekly(currentDateString)
        }
    }

    private fun observedPermission() {
        viewModel.id_user = firebaseService.firebaseAuth.currentUser?.uid.toString()

        viewModel.fetchPermission.observe(this, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                    rlBaseMain.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    rlBaseMain.visibility = View.GONE
                    permission = result.data
                    hideMenuUserNormal(permission!!)
                }
                is Resource.Failure -> {
                    Log.e("ERROR:", "${result.exception.message}")
                }
            }
        })
    }

    private fun hideMenuUserNormal(data: String) {
        if (data == "Normal") {
            bnvMenu.menu.removeItem(R.id.navDataFragment)
            bnvMenu.menu.removeItem(R.id.navHistoricalFragment)
        }
    }

    override fun onStart() {
        super.onStart()
        if (user?.iglesia_references == null){
            oneShowActivity()
        }
    }

    private fun oneShowActivity() {
        val show = ActivityPreferences.getPreferencesBoolean(
                applicationContext,
                ActivityPreferences.PREFERENCES_STATE_ACTIVITY
            )

        if (show) {
            startActivity(Intent(this, JoinOrInviteActivity::class.java))
            ActivityPreferences.savePreferenceBoolean(
                applicationContext,
                false,
                ActivityPreferences.PREFERENCES_STATE_ACTIVITY
            )
            finish()
        }
    }

    companion object {
        private const val TAG = "MainInfo"
    }

}
