package com.david.redcristianauno.presentation.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.david.redcristianauno.R
import com.david.redcristianauno.base.BaseViewHolder
import com.david.redcristianauno.data.model.CreateEntityModel
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.item_entity.view.*
import java.lang.IllegalArgumentException

class CreateEntityAdapter(
    private val context: Context,
    private var list: ArrayList<CreateEntityModel>,
    private val itemClickListener: OnListEntityClickListener
) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    interface OnListEntityClickListener {
        fun onItemClick(cardView: MaterialCardView, entity: CreateEntityModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> =
        EntityViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_entity,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder){
            is EntityViewHolder -> holder.bind(list[position], position)
            else -> throw IllegalArgumentException("Se olvido pasar el viewmodel en el bind")
        }
    }

    inner class EntityViewHolder(itemView: View) : BaseViewHolder<CreateEntityModel>(itemView) {
        override fun bind(item: CreateEntityModel, position: Int) {
            putImage(item)
            itemView.tvNameEntity.text = item.name
            itemView.tvNameLeader.text = item.name_leader
            itemView.mcvEntityItem.setOnClickListener {
                itemClickListener.onItemClick(itemView.mcvEntityItem, item)
            }
        }

        private fun putImage(item: CreateEntityModel){
            when(item.image){
                "Subred" -> itemView.ivTypeEntity.setImageResource(R.drawable.ic_icon_subred)
                "Celula" -> itemView.ivTypeEntity.setImageResource(R.drawable.ic_icon_celula)
            }
        }
    }
}