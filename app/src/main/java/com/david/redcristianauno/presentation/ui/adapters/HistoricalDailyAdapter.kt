package com.david.redcristianauno.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.david.redcristianauno.R
import com.david.redcristianauno.data.model.DataCelula

class HistoricalDailyAdapter : RecyclerView.Adapter<HistoricalDailyAdapter.ViewHolder>() {

    private var listDataCelula = ArrayList<DataCelula>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_history_daily, parent, false)
    )

    override fun getItemCount(): Int = listDataCelula.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataCelula = listDataCelula[position]

        holder.tvHistoricalDailyName.text = dataCelula.user_name
        holder.tvHistoricalDailyEmail.text = dataCelula.email_user
        holder.tvHistoricalDailyHostName.text =
            holder.itemView.context.getString(R.string.HostNameAdapter, dataCelula.host_name)
        holder.tvHistoricalDailyCelula.text =
            holder.itemView.context.getString(R.string.CelulaNameAdapter, dataCelula.address)
        holder.tvHistoricalDailyAssistance.text = holder.itemView.context.getString(
            R.string.AssistanceAdapter,
            dataCelula.assistance.toString()
        )
        holder.tvHistoricalDailyGuest.text =
            holder.itemView.context.getString(R.string.GuestAdapter, dataCelula.guest.toString())
        holder.tvHistoricalDailyChild.text =
            holder.itemView.context.getString(R.string.ChildAdapter, dataCelula.child.toString())
        holder.tvHistoricalDailyOffering.text = holder.itemView.context.getString(
            R.string.OfferingAdapter,
            dataCelula.offering.toString()
        )

    }

    fun updateData(data: List<DataCelula>) {
        listDataCelula.clear()
        listDataCelula.addAll(data)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHistoricalDailyName = itemView.findViewById<TextView>(R.id.tvNameItemHistoryDaily)
        val tvHistoricalDailyEmail = itemView.findViewById<TextView>(R.id.tvEmailItemHistoryDaily)
        val tvHistoricalDailyHostName =
            itemView.findViewById<TextView>(R.id.tvHostNameItemHistoryDaily)
        val tvHistoricalDailyCelula = itemView.findViewById<TextView>(R.id.tvCelulaItemHistoryDaily)
        val tvHistoricalDailyAssistance =
            itemView.findViewById<TextView>(R.id.tvAssistanceItemHistoryDaily)
        val tvHistoricalDailyGuest = itemView.findViewById<TextView>(R.id.tvGuestItemHistoryDaily)
        val tvHistoricalDailyChild = itemView.findViewById<TextView>(R.id.tvChildItemHistoryDaily)
        val tvHistoricalDailyOffering =
            itemView.findViewById<TextView>(R.id.tvOfferingItemHistoryDaily)
    }

}