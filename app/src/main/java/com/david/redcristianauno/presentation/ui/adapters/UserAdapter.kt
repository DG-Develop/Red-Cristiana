package com.david.redcristianauno.presentation.ui.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.david.redcristianauno.R
import com.david.redcristianauno.base.BaseViewHolder
import com.david.redcristianauno.data.model.User
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter(
    private val context: Context,
    private val listUsers: List<User>,
    private val itemClickListener: OnListUserClickListener
) : RecyclerView.Adapter<BaseViewHolder<*>>(){

    interface OnListUserClickListener{
        fun onItemLongClick(cardView: MaterialCardView, user: User): Boolean
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> =
        UserViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_user,
                parent,
                false
            )
        )


    override fun getItemCount(): Int = listUsers.size

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder){
            is UserViewHolder -> holder.bind(listUsers[position], position)
            else -> throw IllegalArgumentException("Se olvido pasar el viewmodel en el bind")
        }
    }

    inner class UserViewHolder(itemView: View) : BaseViewHolder<User>(itemView){

        override fun bind(item: User, position: Int) {
            putImage(item)
            itemView.tvNameUser.text = item.names
            itemView.tvEmailUser.text = item.email
            itemView.mcvUserItem.setOnLongClickListener{
                itemClickListener.onItemLongClick(itemView.mcvUserItem, item)
            }
        }

        private fun putImage(item: User) {
            when(item.permission){
                "Postulado" -> itemView.ivTypeUser.setImageResource(R.drawable.ic_icon_normal)
                "Normal" -> itemView.ivTypeUser.setImageResource(R.drawable.ic_icon_member)
            }
        }
    }
}