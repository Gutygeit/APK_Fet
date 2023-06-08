package com.example.mainactivity.ui.home

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.mainactivity.data.Post
import com.example.mainactivity.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var adapter: PostAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var postList : ArrayList<Post>

    lateinit var  textPosts : Array<String>

    private var _binding: FragmentHomeBinding? = null
    private var feedlayout: LinearLayout? =null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        dataInitializeOf()
        val LayoutManager = LinearLayoutManager(context)
        recyclerView = binding.recyclerFeed;
        recyclerView.layoutManager = LayoutManager
        recyclerView.setHasFixedSize(true)
        adapter = PostAdapter(postList)
        recyclerView.adapter = adapter

                /*
                val textView: TextView = binding.textHome
                homeViewModel.text.observe(viewLifecycleOwner) {
                    textView.text = it
                }
                */






        return root

    }


    private fun dataInitializeOf(){
        postList = arrayListOf<Post>()

        textPosts = arrayOf(
            "test message \n\n\n\n -------------------------- ",
            "test message \n\n\n\n -------------------------- ",
            "test message \n\n\n\n -------------------------- ",
            "test message \n\n\n\n -------------------------- ",
            "test message \n\n\n\n -------------------------- ",
            "test message \n\n\n\n -------------------------- ",
            "test message \n\n\n\n -------------------------- ",
            "test message \n\n\n\n -------------------------- ",
            "test message \n\n\n\n -------------------------- ",
            "test message \n\n\n\n -------------------------- ",
            "test message \n\n\n\n -------------------------- ",
            "test message \n\n\n\n -------------------------- "
        )

        for(i in textPosts.indices){
            val post = Post(textPosts[i])
            postList.add(post)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}