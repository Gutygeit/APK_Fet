package com.example.mainactivity.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mainactivity.databinding.FragmentFilterBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * This class is used to create a filter fragment.
 * @property _binding The binding.
 * @property tagList The list of tags.
 * @property auth The authentication.
 * @constructor Creates a filter fragment.
 * @return Nothing.
 */
class FilterFragment: Fragment() {

    //private lateinit var adapter: PostAdapter
    private lateinit var tagList : ArrayList<String>
    private lateinit var auth: FirebaseAuth



    private var _binding: FragmentFilterBinding? = null

    private val binding get() = _binding!!
    //private lateinit var auth: FirebaseAuth


    /**
     * This function is used to create the view.
     * @param inflater The layout inflater.
     * @param container The view group container.
     * @param savedInstanceState The saved instance state.
     * @return The view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        tagList = arrayListOf()
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        val root: View = binding.root



        val model = ViewModelProvider(requireActivity())[FilterViewModel::class.java]
        //val allTag = model.getAll()

        val chipGroup: ChipGroup = binding.chipGroup
        val tagArray = arrayListOf<String>()
        Firebase.firestore.collection("User")
            .whereEqualTo("Mail", auth.currentUser?.email.toString()).get()
            .addOnSuccessListener { userGotten ->
                for (document in userGotten) {
                    Firebase.firestore.collection("User").document(document.id)
                        .collection("Tags").get().addOnSuccessListener { tags ->
                            for (tag in tags) {
                                for (tagdata in tag.data) {
                                    tagArray.add(tagdata.key.toString())
                                }
                                for(element in tagArray){
                                    val chip = Chip(context)
                                    chip.text = element
                                    chip.isCheckedIconVisible = true
                                    chip.isCheckable = true
                                    chip.isChecked = tag.data[element] as Boolean
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