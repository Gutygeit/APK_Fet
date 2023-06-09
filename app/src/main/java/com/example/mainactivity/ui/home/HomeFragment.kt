package com.example.mainactivity.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mainactivity.data.Post
import com.example.mainactivity.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {


    private lateinit var adapter: PostAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var postList : ArrayList<Post>

    lateinit var  textPosts : Array<String>
    lateinit var  users : Array<String>
    lateinit var  roles : Array<String>
    lateinit var  tags : Array<String>
    lateinit var  imgUsers : Array<String>
    lateinit var  imgPosts : Array<String>


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


        dataInitialize()
        val LayoutManager = LinearLayoutManager(context)
        recyclerView = binding.recyclerFeed;
        recyclerView.layoutManager = LayoutManager
        recyclerView.setHasFixedSize(true)
        adapter = PostAdapter(postList)
        recyclerView.adapter = adapter



        binding.swipe.getViewTreeObserver().addOnScrollChangedListener(OnScrollChangedListener {
            println(binding.scrollFeed.scrollY)
            binding.swipe.isEnabled = binding.scrollFeed.scrollY == 0
            })

        binding.swipe.setOnRefreshListener {
            dataInitialize()
            adapter = PostAdapter(postList)
            recyclerView.adapter = adapter
            binding.swipe.isRefreshing = false
        }

        return root

    }


    private fun dataInitialize(){


        postList = arrayListOf<Post>()

        for(i in textPosts.indices){
            val post = Post(textPosts[i],roles[i],tags[i], imgPosts[i],users[i], imgUsers[i])
            postList.add(post)
        }
        textPosts = arrayOf(
            "",
            Math.random().toString() +
            "test message \n\n\n\n -------------------------- ",
            "test message \n\n\n\n -------------------------- ",
            "test message \n\n\n\n -------------------------- ",
            Math.random().toString() +
                    "test message \n\n\n\n -------------------------- ",
            "test message \n\n\n\n -------------------------- ",
            "test message \n\n\n\n -------------------------- "
        )



        users = arrayOf(
            "",
            "Zoe ",
            "Dion",
            "Gauthier",
            "Zoe ",
            "Dion",
            "Gauthier"
        )
        roles = arrayOf(
            "",
            "Etudiants",
            "Prof",
            "Administration",
            "Etudiants",
            "Prof",
            "Administration"
        )

        tags = arrayOf(
            "",
            "Examin",
            "Cours",
            "Stage",
            "Examin",
            "Cours",
            "Stage"

        )
        imgUsers = arrayOf(
            "",
            "img1",
            "img2",
            "img3",
            "img1",
            "img2",
            "img3"
        )
        imgPosts = arrayOf(
            "",
            "imgPosts 1",
            "imgPosts 2",
            "imgPosts 3",
            "imgPosts 1",
            "imgPosts 2",
            "imgPosts 3"
        )

        for(i in textPosts.indices){
            val post = Post(textPosts[i],roles[i],tags[i], imgPosts[i],users[i], imgUsers[i])
            postList.add(post)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}