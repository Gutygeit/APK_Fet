package com.example.mainactivity.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mainactivity.data.Post
import com.example.mainactivity.databinding.FragmentFilterBinding
import com.google.firebase.auth.FirebaseAuth

class FilterFragment: Fragment() {

    //private lateinit var adapter: PostAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var tagList : ArrayList<String>
    private lateinit var adapter: FilterAdapter


    private var _binding: FragmentFilterBinding? = null
    //private var feedlayout: LinearLayout? =null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    //private lateinit var auth: FirebaseAuth



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        tagList = arrayListOf()
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val LayoutManager = LinearLayoutManager(context)
        recyclerView = binding.recyclerView;
        recyclerView.layoutManager = LayoutManager
        recyclerView.setHasFixedSize(true)

        for(i in 1..42){
            var tag : String = i.toString()
            tagList.add(tag)
        }

        adapter = FilterAdapter(tagList)
        recyclerView.adapter = adapter


        return root
    }

}