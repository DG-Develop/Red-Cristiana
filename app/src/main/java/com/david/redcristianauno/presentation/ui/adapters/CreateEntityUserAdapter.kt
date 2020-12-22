package com.david.redcristianauno.presentation.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.david.redcristianauno.R
import com.david.redcristianauno.core.BaseViewHolder
import com.david.redcristianauno.data.model.User
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.item_entity_user.view.*
import java.lang.IllegalArgumentException

class CreateEntityUserAdapter(
    private val context: Context,
    private val listUserEntity: List<User>,
    private val itemClickListenerUser: OnListEntityUserListener
) : RecyclerView.Adapter<BaseViewHolder<*>>(){

    interface OnListEntityUserListener{
        fun onItemClickUser(
            cardView: MaterialCardView,
            iv: ImageView,
            tvName: TextView,
            tvEmail: TextView,
            user: User
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> =
        UserEntityViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_entity_user,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = listUserEntity.size

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder){
            is UserEntityViewHolder -> holder.bind(listUserEntity[position], position)
            else -> throw IllegalArgumentException("Se olvido pasar el viewmodel en el bind")
        }
    }

    inner class UserEntityViewHolder(itemView: View) : BaseViewHolder<User>(itemView){

        override fun bind(item: User, position: Int) {
            putImage(item)
            itemView.tvName.text = item.names
            itemView.tvEmail.text = item.email
            itemView.mcvEntityUserItem.setOnClickListener {
                itemClickListenerUser.onItemClickUser(
                    itemView.mcvEntityUserItem,
                    itemView.ivPermission,
                    itemView.tvName,
                    itemView.tvEmail,
                    item
                )
            }
        }

        private fun putImage(item: User){
            /* En comentarios de forma temporal */
            /*when(item.permission){
                "Normal" -> itemView.ivPermission.setImageResource(R.drawable.ic_icon_member)
                "Lider Celula" -> itemView.ivPermission.setImageResource(R.drawable.ic_icon_leader_celula)
                "Lider Subred" -> itemView.ivPermission.setImageResource(R.drawable.ic_icon_leader_subred)
                "Lider Red" -> itemView.ivPermission.setImageResource(R.drawable.ic_icon_leader_red)
            }*/

            when(item.permission){
                "Normal" -> itemView.ivPermission.setImageResource(R.drawable.ic_icon_member)
                "Lider Celula" -> itemView.ivPermission.setImageResource(R.drawable.ic_icon_leader_celula)
                "Subred" -> itemView.ivPermission.setImageResource(R.drawable.ic_icon_leader_subred)
                "Red" -> itemView.ivPermission.setImageResource(R.drawable.ic_icon_leader_red)
            }
        }
    }
}