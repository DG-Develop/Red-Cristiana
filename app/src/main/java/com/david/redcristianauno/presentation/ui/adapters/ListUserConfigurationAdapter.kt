package com.david.redcristianauno.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.david.redcristianauno.R
import com.david.redcristianauno.data.model.User

class ListUserConfigurationAdapter(private val llSearch: View, private val llCrud: View) : RecyclerView.Adapter<ListUserConfigurationAdapter.ViewHolder>(){

    private val listUsers = ArrayList<User>()
    val userChecked = ArrayList<User>()
    val checked = MutableLiveData<Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_list_user, parent, false)
    )

    override fun getItemCount(): Int = listUsers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val user = listUsers[position]

        holder.tvNameUser.text = user.names
        holder.tvEmailUser.text = user.email
        holder.cbUser.isChecked = false

        holder.cbUser.setOnCheckedChangeListener { button, isChecked ->
            if(button.isChecked){
                llCrud.visibility = View.VISIBLE
                llSearch.visibility = View.GONE
                userChecked.add(listUsers[position])
                checked.value = isChecked

            }else{
                userChecked.remove(user)
                checked.value = isChecked
            }
        }
    }
    fun updateuser(user: List<User>){
        listUsers.clear()
        listUsers.addAll(user)
        notifyDataSetChanged()
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvNameUser: TextView = itemView.findViewById(R.id.tvNameUserItemList)
        val tvEmailUser: TextView = itemView.findViewById(R.id.tvEmailUserItemList)
        val cbUser: CheckBox = itemView.findViewById(R.id.cbItemListUser)
    }
}