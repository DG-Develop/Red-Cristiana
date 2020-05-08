package com.david.redcristianauno

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.david.redcristianauno.domain.HistoricalWeeklyUseCase
import com.david.redcristianauno.domain.HistoricalWeeklyUseCaseImpl
import com.david.redcristianauno.model.network.FirebaseService
import com.david.redcristianauno.model.network.HistoricalWeeklyRepositoryImpl
import com.david.redcristianauno.viewmodel.MainViewModel
import com.david.redcristianauno.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

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
    }

    private fun configNav(){
        NavigationUI.setupWithNavController(bnvMenu, Navigation.findNavController(this, R.id.fragContent))
    }

    fun observedDayOfWeek(){
        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.TUESDAY

        val dayOfWeek = calendar[Calendar.DAY_OF_WEEK]

        if(dayOfWeek  == calendar.firstDayOfWeek){
            val currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.time)
            viewModel.getDataHistoryWeekly(currentDateString)
        }
    }

}
