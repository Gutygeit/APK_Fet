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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            post = it.getParcelable(ARG_POST)!!
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_zoomed_post, container, false)

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
        role.text = "gg"
        tag.text = post.Tag

        return view
    }

    companion object {
        private const val ARG_POST = "post"

        @JvmStatic
        fun newInstance(post: Post) =
            ZoomPostFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_POST, post)
                }
            }
    }
}