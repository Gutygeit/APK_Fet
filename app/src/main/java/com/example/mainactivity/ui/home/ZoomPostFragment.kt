package com.example.mainactivity.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.mainactivity.R
import com.example.mainactivity.data.Post


class ZoomPostFragment : Fragment() {
    private lateinit var post: Post

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_zoom_post, container, false)


        post = arguments?.getParcelable<Post>("post")!!

        val textPost = view.findViewById<TextView>(R.id.zoomedTextPost)
        val imgPost = view.findViewById<ImageView>(R.id.zoomedImgPost)
        val imgUser = view.findViewById<ImageView>(R.id.zoomedImgUser)
        val user = view.findViewById<TextView>(R.id.zoomedUser)
        val role = view.findViewById<TextView>(R.id.zoomedRole)
        val tag = view.findViewById<TextView>(R.id.zoomedTag)

        textPost.text = post.Content
        imgPost.setImageBitmap(post.Image)
        imgUser.setImageBitmap(post.ProfileP)
        user.text = post.Auteur
        role.text = post.Role
        tag.text = post.Tag

        return view
    }
}
