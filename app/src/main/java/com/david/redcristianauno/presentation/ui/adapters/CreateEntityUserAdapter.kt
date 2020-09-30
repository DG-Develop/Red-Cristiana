package com.david.redcristianauno.presentation.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.david.redcristianauno.R
import com.david.redcristianauno.base.BaseViewHolder
import com.david.redcristianauno.data.model.User
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.item_entity_user.view.*
import java.lang.IllegalArgumentException

class CreateEntityUserAdapter(
    private val context: Context,
    private var list: ArrayList<User>,
    private val itemClickListenerUser: OnListEntityUserListener
) : RecyclerView.Adapter<BaseViewHolder<*>>(){

    interface OnListEntityUserListener{
        fun onItemClickUser(cardView: MaterialCardView, user: User)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> =
        UserEntityViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_entity_user,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        Log.i("entityInfo", "position $position")
        when(holder){
            is UserEntityViewHolder -> holder.bind(list[position], position)
            else -> throw IllegalArgumentException("Se olvido pasar el viewmodel en el bind")
        }
    }

    override fun getItemCount(): Int = list.size

    inner class UserEntityViewHolder(itemView: View) : BaseViewHolder<User>(itemView){

        override fun bind(item: User, position: Int) {
            putImage(item)
            itemView.tvName.text = item.names
            itemView.tvEmail.text = item.email
            itemView.mcvEntityUserItem.setOnClickListener {
                itemClickListenerUser.onItemClickUser(itemView.mcvEntityUserItem, item)
            }
        }

        private fun putImage(item: User){
            when(item.permission){
                "Normal" -> itemView.ivPermission.setImageResource(R.drawable.ic_icon_member)
                "Lider Celula" -> itemView.ivPermission.setImageResource(R.drawable.ic_icon_leader_celula)
                "Lider Subred" -> itemView.ivPermission.setImageResource(R.drawable.ic_icon_leader_subred)
                "Lider Red" -> itemView.ivPermission.setImageResource(R.drawable.ic_icon_leader_red)
            }
        }
    }
}