package com.example.mainactivity.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mainactivity.R
import com.example.mainactivity.data.Post


class PostAdapter(private val listPost : ArrayList<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.post, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = listPost[position]

        holder.textPost.text = currentItem.Content
        holder.imgPost.setImageBitmap(currentItem.Image)
        holder.imgUser.setImageBitmap(currentItem.ProfileP)
        holder.user.text = currentItem.Auteur
        holder.tag.text = currentItem.Tag

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            if (context is AppCompatActivity) {
                val zoomedPostFragment = ZoomPostFragment.newInstance(currentItem)
                context.supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main, zoomedPostFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

    }

    override fun getItemCount(): Int {
        return listPost.size
    }


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val textPost : TextView = itemView.findViewById(R.id.textPost)
        val imgPost : ImageView = itemView.findViewById(R.id.imgPost)
        val imgUser : ImageView = itemView.findViewById(R.id.imgUser)
        val user : TextView = itemView.findViewById(R.id.user)
        val tag : TextView = itemView.findViewById(R.id.tag)
    }



}