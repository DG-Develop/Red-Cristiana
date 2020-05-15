package com.david.redcristianauno

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import com.david.redcristianauno.domain.HistoricalWeeklyUseCaseImpl
import com.david.redcristianauno.model.network.FirebaseService
import com.david.redcristianauno.model.network.HistoricalWeeklyRepositoryImpl
import com.david.redcristianauno.viewmodel.MainViewModel
import com.david.redcristianauno.viewmodel.MainViewModelFactory
import com.david.redcristianauno.vo.Resource
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val firebaseService = FirebaseService()
    private var permission: String? = null

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            MainViewModelFactory(HistoricalWeeklyUseCaseImpl(HistoricalWeeklyRepositoryImpl()))
        ).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setActionBar(findViewById(R.id.toolbarMain))
        configNav()
        observedDayOfWeek()
        observedPermission()
    }

    private fun configNav(){
        NavigationUI.setupWithNavController(bnvMenu, Navigation.findNavController(this, R.id.fragContent))
    }

    private fun observedDayOfWeek(){
        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.TUESDAY

        val dayOfWeek = calendar[Calendar.DAY_OF_WEEK]

        if(dayOfWeek  == calendar.firstDayOfWeek){
            val currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.time)
            viewModel.getDataHistoryWeekly(currentDateString)
        }
    }
    private fun observedPermission(){
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

    private fun hideMenuUserNormal(data: String){
        if (data == "Normal"){
            bnvMenu.menu.removeItem(R.id.navDataFragment)
            bnvMenu.menu.removeItem(R.id.navHistoricalFragment)
        }
    }

}
