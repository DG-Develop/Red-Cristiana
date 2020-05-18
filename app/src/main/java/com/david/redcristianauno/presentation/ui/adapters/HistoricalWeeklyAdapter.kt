package com.david.redcristianauno.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.david.redcristianauno.R
import com.david.redcristianauno.data.model.HistoricalWeekly

class HistoricalWeeklyAdapter  : RecyclerView.Adapter<HistoricalWeeklyAdapter.ViewHolder>(){

    private var listHistoricalWeekly = ArrayList<HistoricalWeekly>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricalWeeklyAdapter.ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_history_weekly, parent, false)
    )

    override fun getItemCount(): Int = listHistoricalWeekly.size

    override fun onBindViewHolder(holder: HistoricalWeeklyAdapter.ViewHolder, position: Int) {
        val historicalWeekly = listHistoricalWeekly[position]

        holder.titleWeekly.text = historicalWeekly.date
        holder.assistance.text = holder.itemView.context.getString(R.string.AssistanceTotalAdapter, historicalWeekly.assistance_total.toString())
        holder.guest.text = holder.itemView.context.getString(R.string.GuestTotalAdapter, historicalWeekly.guest_total.toString())
        holder.child.text = holder.itemView.context.getString(R.string.ChildTotalAdapter, historicalWeekly.child_total.toString())
        holder.offering.text = holder.itemView.context.getString(R.string.OfferingTotalAdapter, historicalWeekly.offering_total.toString())
    }

    fun updateData(data: List<HistoricalWeekly>){
        listHistoricalWeekly.clear()
        listHistoricalWeekly.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val titleWeekly: TextView = itemView.findViewById(R.id.tvNameWeekItemHistoryDaily)
        val assistance: TextView = itemView.findViewById(R.id.tvAssistanceTotal)
        val guest: TextView = itemView.findViewById(R.id.tvGuestTotal)
        val child: TextView = itemView.findViewById(R.id.tvChildTotal)
        val offering: TextView = itemView.findViewById(R.id.tvOfferingTotal)
    }
}