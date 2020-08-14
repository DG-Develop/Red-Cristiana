package com.david.redcristianauno.presentation.ui.adapters

import android.content.Context
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
        fun onItemClick(cardView: MaterialCardView, user: CreateEntityModel)
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
            itemView.tvName.text = item.name
            itemView.tvEmail.text = item.email
            itemView.mcvEntityItem.setOnClickListener {
                itemClickListener.onItemClick(itemView.mcvEntityItem, item)
            }
        }

        private fun putImage(item: CreateEntityModel){
            when(item.image){
                "Normal" -> itemView.ivType.setImageResource(R.drawable.ic_icon_member)
                "Lider Celula" -> itemView.ivType.setImageResource(R.drawable.ic_icon_leader_celula)
                "Lider Subred" -> itemView.ivType.setImageResource(R.drawable.ic_icon_leader_subred)
                "Lider Red" -> itemView.ivType.setImageResource(R.drawable.ic_icon_leader_red)
                "Subred" -> itemView.ivType.setImageResource(R.drawable.ic_icon_subred)
                "Celula" -> itemView.ivType.setImageResource(R.drawable.ic_icon_celula)
            }
        }
    }
}