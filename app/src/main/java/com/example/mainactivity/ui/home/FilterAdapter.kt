package com.example.mainactivity.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mainactivity.R
import com.example.mainactivity.data.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FilterAdapter(private val tagList : ArrayList<String>) : RecyclerView.Adapter<FilterAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_boxfilter, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FilterAdapter.ViewHolder, position: Int) {
        val currentItem = tagList[position]

        holder.radioButton.text = currentItem

    }

    override fun getItemCount(): Int {
        return tagList.size
    }


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val radioButton : RadioButton = itemView.findViewById(R.id.radioButton)
    }

}