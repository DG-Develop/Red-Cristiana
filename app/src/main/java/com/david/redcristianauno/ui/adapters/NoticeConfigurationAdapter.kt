package com.david.redcristianauno.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.david.redcristianauno.R
import com.david.redcristianauno.model.News
import com.squareup.picasso.Picasso

class NoticeConfigurationAdapter : RecyclerView.Adapter<NoticeConfigurationAdapter.ViewHolder>(){

    private val listNews = ArrayList<News>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeConfigurationAdapter.ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_home_notice, parent, false)
    )

    override fun getItemCount(): Int = listNews.size

    override fun onBindViewHolder(holder: NoticeConfigurationAdapter.ViewHolder, position: Int) {
        val news = listNews[position]

        Picasso.get()
            .load(news.pathPhoto)
            .resize(300, 200)
            .centerCrop()
            .into(holder.imageNew)

        holder.titleNew.text = news.title
        holder.descriptionNew.text = news.description
    }

    fun updateNews(news: List<News>){
        listNews.clear()
        listNews.addAll(news)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imageNew: ImageView = itemView.findViewById(R.id.iv_item_home_image)
        val titleNew: TextView = itemView.findViewById(R.id.tv_item_home_title)
        val descriptionNew: TextView = itemView.findViewById(R.id.tv_item_home_notice)
    }
}