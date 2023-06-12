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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class FilterFragment: Fragment() {

    //private lateinit var adapter: PostAdapter
    private lateinit var tagList : ArrayList<String>
    private lateinit var auth: FirebaseAuth



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
        auth = FirebaseAuth.getInstance()

        val root: View = binding.root



        val model = ViewModelProvider(requireActivity()).get(FilterViewModel::class.java)
        //val allTag = model.getAll()

        val chipGroup: ChipGroup = binding.chipGroup
        var tagArray = arrayListOf<String>()
        val userRef = Firebase.firestore.collection("User")
            .whereEqualTo("Mail", auth.currentUser?.email.toString()).get()
            .addOnSuccessListener { userGotten ->
                for (document in userGotten) {
                    val tagRef = Firebase.firestore.collection("User").document(document.id)
                        .collection("Tags").get().addOnSuccessListener { tags ->
                            for (tag in tags) {
                                for (tagdata in tag.data) {
                                    tagArray.add(tagdata.key)
                                }
                                for(element in tagArray){
                                    val chip = Chip(context)
                                    chip.text = element
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
                            }
                        }
                   }
                }




        return root
    }

}