package com.david.redcristianauno.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.david.redcristianauno.domain.HistoricalWeeklyUseCase
import com.david.redcristianauno.model.DataCelula
import com.david.redcristianauno.model.HistoricalWeekly
import com.david.redcristianauno.model.network.Callback
import com.david.redcristianauno.model.network.FirebaseService
import com.david.redcristianauno.model.network.FirebaseService.Companion.HISTORICAL_WEEKLY_COLLECTION_NAME
import java.lang.Exception
import java.text.DateFormat
import java.util.*

class MainViewModel(historicalWeeklyUseCase: HistoricalWeeklyUseCase): ViewModel() {
    private val historicalWeekly = historicalWeeklyUseCase

    fun getDataHistoryWeekly(date: String){
        historicalWeekly.getHistoricalWeeklyWithDate(date, object : Callback<Boolean> {
            override fun OnSucces(result: Boolean?) {
                if(result == false){
                    findAllDataCelula(date)
                }
            }

            override fun onFailure(exception: Exception) {
                Log.e("ErrorMain", "No se encontro la fecha")
            }
        })
    }

    private fun findAllDataCelula(date: String) {
        historicalWeekly.getDataCelula(object :Callback<List<DataCelula>>{
            override fun OnSucces(result: List<DataCelula>?) {
                val dateListToSearch = weeklyFormat()

                var assistance_total = 0
                var guest_total = 0
                var child_total = 0
                var offering_total = 0.0

                for (dateList in dateListToSearch){
                    if (result != null) {
                        for (dataCelula in result){
                            if(dateList == dataCelula.date){
                               assistance_total += dataCelula.assistance
                                guest_total += dataCelula.guest
                                child_total += dataCelula.child
                                offering_total += dataCelula.offering
                            }
                        }
                    }
                }
                createHistoricalWeekly(date, assistance_total, guest_total, child_total, offering_total)
            }

            override fun onFailure(exception: Exception) {

            }
        })
    }

    private fun createHistoricalWeekly(date: String, assistanceTotal: Int, guestTotal: Int, childTotal: Int, offeringTotal: Double) {
        val firebaseService = FirebaseService()
        val historicalWeekly = HistoricalWeekly()
        historicalWeekly.assistance_total = assistanceTotal
        historicalWeekly.guest_total = guestTotal
        historicalWeekly.child_total = childTotal
        historicalWeekly.offering_total = offeringTotal
        historicalWeekly.historical_type = "Celula"

        firebaseService.setDocumentWithID(historicalWeekly,HISTORICAL_WEEKLY_COLLECTION_NAME,date)
    }

    private fun weeklyFormat(): MutableList<String> {
        val calendar = Calendar.getInstance()
        var dayOfMonth = calendar[Calendar.DAY_OF_MONTH]
        var month = calendar[Calendar.MONTH] + 1
        val year = calendar[Calendar.YEAR]

        val dates: MutableList<String> = mutableListOf()

        for (i in 1..7) {
            dayOfMonth--
            when (month) {
                1, 2, 4, 6, 8, 9, 11 -> {
                    if (dayOfMonth <= 0) {
                        month -= 1
                        dayOfMonth = 31
                    }
                }
                5, 7, 10, 12 -> {
                    if (dayOfMonth <= 0) {
                        month -= 1
                        dayOfMonth = 30
                    }
                }
                3 -> {
                    if (dayOfMonth <= 0) {
                        if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
                            month -= 1
                            dayOfMonth = 29
                        } else {
                            month -= 1
                            dayOfMonth = 28
                        }
                    }

                }
            }
            calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            calendar[Calendar.MONTH] = month - 1
            calendar[Calendar.YEAR] = year
            val date = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.time)
            dates.add(date)
        }
        return dates
    }
}