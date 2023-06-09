package com.example.mainactivity.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mainactivity.R
import com.example.mainactivity.data.Post

class PostAdapter(private val listPost : ArrayList<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.post, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostAdapter.ViewHolder, position: Int) {
        val currentItem = listPost[position]
        holder.textPost.text = currentItem.text
        holder.imgPost.text = currentItem.imgPost
        holder.imgUser.text = currentItem.imgUser
        holder.user.text = currentItem.user
        holder.role.text = currentItem.role
        holder.tag.text = currentItem.tag

    }

    override fun getItemCount(): Int {
        return listPost.size
    }


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val textPost : TextView = itemView.findViewById(R.id.textPost)
        val imgPost : TextView = itemView.findViewById(R.id.imgPost)
        val imgUser : TextView = itemView.findViewById(R.id.imgUser)
        val user : TextView = itemView.findViewById(R.id.user)
        val role : TextView = itemView.findViewById(R.id.role)
        val tag : TextView = itemView.findViewById(R.id.tag)
    }

}