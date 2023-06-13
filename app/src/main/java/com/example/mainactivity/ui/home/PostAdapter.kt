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
import com.example.mainactivity.data.Post


/**
 * This class is used to create a post adapter.
 * @property listPost The list of posts.
 * @constructor Creates a post adapter.
 * @param listPost The list of posts.
 */
class PostAdapter(private val listPost : ArrayList<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private var navController: NavController? = null


    /**
     * This function to create a view holder.
     * @param parent The parent view group.
     * @param viewType The view type.
     * @return The view holder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        navController = Navigation.findNavController(parent.context as AppCompatActivity, R.id.nav_host_fragment_activity_main)
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.post, parent, false)
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

        holder.textPost.text = currentItem.Content
        holder.imgPost.setImageBitmap(currentItem.Image)
        holder.imgUser.setImageBitmap(currentItem.ProfileP)
        holder.user.text = currentItem.Auteur
        holder.tag.text = currentItem.Tag


        holder.apply {
            with(holder.itemView) {
                itemView.setOnClickListener {
                    navController = Navigation.findNavController(itemView)
                    val action = HomeFragmentDirections.actionNavigationHomeToZoomPostFragment(currentItem)
                    navController?.navigate(action)
                }
            }
        }
    }


    /**
     * This function returns the number of items in the list.
     * @return The number of items in the list.
     */
    override fun getItemCount(): Int {
        return listPost.size
    }


    /**
     * This class is used to create a view holder.
     * @constructor Creates a view holder.
     * @param itemView The view.
     */
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val textPost : TextView = itemView.findViewById(R.id.textPost)
        val imgPost : ImageView = itemView.findViewById(R.id.imgPost)
        val imgUser : ImageView = itemView.findViewById(R.id.imgUser)
        val user : TextView = itemView.findViewById(R.id.user)
        val tag : TextView = itemView.findViewById(R.id.tag)
    }



}