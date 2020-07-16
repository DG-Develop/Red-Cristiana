package com.david.redcristianauno.presentation.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.david.redcristianauno.R
import com.david.redcristianauno.base.BaseViewHolder
import com.david.redcristianauno.data.model.User
import kotlinx.android.synthetic.main.item_user.view.*
import java.lang.IllegalArgumentException

class UserAdapter(
    private val context: Context,
    val listUsers: List<User>
) : RecyclerView.Adapter<BaseViewHolder<*>>(){

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
            itemView.tvNameUser.text = item.names
            itemView.tvEmailUser.text = item.email
        }
    }
}