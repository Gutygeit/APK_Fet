package com.example.mainactivity.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.mainactivity.R
import com.example.mainactivity.data.Comment
import com.example.mainactivity.data.Post


/**
 * This class is used to create a post adapter.
 * @property listPost The list of posts.
 * @constructor Creates a post adapter.
 * @param listPost The list of posts.
 */
class CommentAdapter(private val listPost : ArrayList<Comment>) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    private var navController: NavController? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.comment, parent, false)
        return ViewHolder(itemView)
    }

    /**
     * This function is used to bind the view holder.
     * @param holder The view holder.
     * @param position The position of the item in the list.
     * @return Nothing.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = listPost[position]

        holder.imgUser.setImageBitmap(currentItem.ProfileP)
        holder.textComment.text = currentItem.Content
        holder.user.text = currentItem.Auteur

    }

    override fun getItemCount(): Int {
        return listPost.size
    }


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val user : TextView = itemView.findViewById(R.id.user)
        val imgUser : ImageView = itemView.findViewById(R.id.imgUser)
        val textComment : TextView = itemView.findViewById(R.id.textComment)
    }



}