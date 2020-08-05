package com.david.redcristianauno.presentation.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.david.redcristianauno.R
import com.david.redcristianauno.base.BaseViewHolder
import com.david.redcristianauno.data.model.GeneralModel
import kotlinx.android.synthetic.main.item_general_list.view.*
import java.lang.IllegalArgumentException

class GeneralListAdapter(
    private val context: Context,
    private val list: List<GeneralModel>
): RecyclerView.Adapter<BaseViewHolder<*>>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> =
        GeneralViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_general_list,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
       when(holder){
           is GeneralViewHolder-> holder.bind(list[position], position)
           else -> throw IllegalArgumentException("Se olvido pasar el viewodel en el bind")
       }
    }

    inner class GeneralViewHolder(itemView: View): BaseViewHolder<GeneralModel>(itemView){
        override fun bind(item: GeneralModel, position: Int) {
            itemView.tvTitleCard.text = item.id
            itemView.tvContentGeneral.text = item.name_leader
        }
    }
}