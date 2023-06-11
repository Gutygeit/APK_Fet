package com.example.mainactivity.ui.home

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.mainactivity.databinding.FragmentFilterBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


class FilterFragment: Fragment() {

    //private lateinit var adapter: PostAdapter
    private lateinit var tagList : ArrayList<String>


    private var _binding: FragmentFilterBinding? = null

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



        val model = ViewModelProvider(requireActivity()).get(FilterViewModel::class.java)
        //val allTag = model.getAll()

        val chipGroup: ChipGroup = binding.chipGroup
        for(i in 1..42){
            val chip = Chip(context)
            //chip.text = allTag[i]
            chip.text = i.toString() + "test ecrit"
            chip.setCheckedIconVisible(true)
            //chip.setChipIconResource(R.drawable.)
            chip.isCheckable = true
            chip.isChecked = true
            chip.setOnClickListener {
                if(chip.isChecked){
                    model.addTag(chip.text as String)
                }
                else{
                    model.removeTag(chip.text as String)
                }
            }

            chipGroup.addView(chip)
        }



        return root
    }

}