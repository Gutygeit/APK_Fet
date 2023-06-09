@file:Suppress("NAME_SHADOWING")

package com.example.mainactivity.ui.home

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mainactivity.data.Post
import com.example.mainactivity.databinding.FragmentHomeBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.text.SimpleDateFormat

/**
 * This class is used to create a home fragment.
 * @property _binding The binding of the fragment.
 * @property feedlayout The layout of the feed.
 * @property auth The authentication.
 * @property storage The storage.
 * @property adapter The post adapter.
 * @property recyclerView The recycler view.
 * @property postList The list of posts.
 * @property model The filter view model.
 * @constructor Creates a home fragment.
 * @return Nothing.
 */
class HomeFragment : Fragment() {

    private lateinit var adapter: PostAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var postList : ArrayList<Post>
    private lateinit var model : FilterViewModel


    private var _binding: FragmentHomeBinding? = null
    private var feedlayout: LinearLayout? =null
    // This property is only valid between onCreateView and
    // onDestroyView.i
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    val storage = Firebase.storage("gs://apkfet-a63e3.appspot.com/")

    /**
     * This function is used to initialize the data.
     * @return Nothing.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManager = LinearLayoutManager(context)
        recyclerView = binding.recyclerFeed
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        model = ViewModelProvider(requireActivity())[FilterViewModel::class.java]


        dataInitialize()

        binding.swipe.setOnRefreshListener {
            dataInitialize()
            binding.swipe.isRefreshing = false

        }
        binding.topBtn.setOnClickListener{
            binding.recyclerFeed.scrollToPosition(0)
        }

        binding.filter.setOnClickListener{
            if(binding.filterFragment.isVisible) {
                binding.filterFragment.isVisible = false
                model.getList()
                Firebase.firestore.collection("User")
                    .whereEqualTo("Mail", auth.currentUser?.email.toString()).get()
                    .addOnSuccessListener { userGotten ->
                        for (document in userGotten) {
                            Firebase.firestore.collection("User").document(document.id)
                                .collection("Tags").get().addOnSuccessListener { tags ->
                                    for (tag in tags) {
                                        for (tagdata in tag.data) {
                                            if(tagdata.key.toString() !in model.getList()){
                                                Firebase.firestore.collection("User").document(document.id)
                                                    .collection("Tags").document(tag.id).update(tagdata.key.toString(), false)
                                            }
                                            else {
                                                Firebase.firestore.collection("User").document(document.id)
                                                    .collection("Tags").document(tag.id).update(tagdata.key.toString(), true)
                                            }
                                        }
                                    }
                                }
                        }
                    }
                //Thread.sleep(1000)
                //dataInitialize()

            }
            else{
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
                                        }
                                    }
                                }
                        }
                    }
                binding.filterFragment.isVisible = true
            }
        }



        return root
    }


    /**
     * This function is used to create the post adapter.
     * @return Nothing.
     */
    private fun getBitmapPP(strImg : String?) : Bitmap{

        var bitmap : Bitmap
        bitmap = BitmapFactory.decodeResource(requireContext().resources, com.example.mainactivity.R.drawable.pp1)
        when (strImg) {

            "1" -> bitmap = BitmapFactory.decodeResource(requireContext().resources, com.example.mainactivity.R.drawable.pp1)
            "2" -> bitmap = BitmapFactory.decodeResource(requireContext().resources, com.example.mainactivity.R.drawable.pp2)
            "3" -> bitmap = BitmapFactory.decodeResource(requireContext().resources, com.example.mainactivity.R.drawable.pp3)
            "4" -> bitmap = BitmapFactory.decodeResource(requireContext().resources, com.example.mainactivity.R.drawable.pp4)
            "5" -> bitmap = BitmapFactory.decodeResource(requireContext().resources, com.example.mainactivity.R.drawable.pp5)
            "6" -> bitmap = BitmapFactory.decodeResource(requireContext().resources, com.example.mainactivity.R.drawable.pp6)
            "7" -> bitmap = BitmapFactory.decodeResource(requireContext().resources, com.example.mainactivity.R.drawable.pp7)
            "8" -> bitmap = BitmapFactory.decodeResource(requireContext().resources, com.example.mainactivity.R.drawable.pp8)
            "9" -> bitmap = BitmapFactory.decodeResource(requireContext().resources, com.example.mainactivity.R.drawable.pp9)
        }
        return bitmap
    }

    /**
     * This function is used to initialize the data.
     * @return Nothing.
     */
    @SuppressLint("SimpleDateFormat")
    private fun dataInitialize() {
        auth = FirebaseAuth.getInstance()

        postList = arrayListOf()
        val tagList = arrayListOf<String>()

        Firebase.firestore.collection("User")
            .whereEqualTo("Mail", auth.currentUser?.email.toString()).get()
            .addOnSuccessListener { userGotten ->
                for (document in userGotten) {
                    Firebase.firestore.collection("User").document(document.id)
                        .collection("Tags").get().addOnSuccessListener { tags ->
                        for (tag in tags) {
                            for (tagdata in tag.data) {
                                if (tagdata.value as Boolean) {
                                    tagList.add(tagdata.key.toString())
                                }
                            }
                        }
                                    val postRef = Firebase.firestore.collection("Post").orderBy("Date",
                                    com.google.firebase.firestore.Query.Direction.ASCENDING)
                                    postRef.get().addOnSuccessListener { result ->
                                        for (document in result) {
                                            if (document.data["Tag"].toString() in tagList) {
                                                val timestamp = document.data["Date"] as com.google.firebase.Timestamp
                                                val date = timestamp.toDate()
                                                val dateFormat = SimpleDateFormat("dd/MM/yyyy")
                                                val dateString: String? = dateFormat.format(date)
                                                val idAuteur = document.data["Auteur"].toString()
                                                val docRef = Firebase.firestore.collection("User")
                                                    .document(idAuteur)
                                                docRef.get().addOnSuccessListener { result ->

                                                    val gg = result.data?.get("pp")?.toString()
                                                    val bitmap = getBitmapPP(gg)
                                                        val image =
                                                            document.data["Image"].toString()
                                                        if (!(image.contentEquals(""))) {
                                                            val stoRef2 =
                                                                storage.reference.child(image)
                                                            val localfile2 = File.createTempFile(
                                                                image.split("/")[1].split(".")[0],
                                                                image.split("/")[1].split(".")[1],
                                                            )
                                                            stoRef2.getFile(localfile2)
                                                                .addOnSuccessListener {
                                                                    val bitmap2 =
                                                                        BitmapFactory.decodeFile(
                                                                            localfile2.absolutePath
                                                                        )
                                                                    val post = Post(
                                                                        "${result.data?.get("FirstName")
                                                                            ?.toString()!!} ${result.data?.get("LastName")
                                                                            ?.toString()!!}",
                                                                        document.data["Content"].toString(),
                                                                        result.data?.get("Role")
                                                                            ?.toString()!!,
                                                                        bitmap,
                                                                        bitmap2,
                                                                        document.data["Tag"].toString(),
                                                                        dateString,
                                                                        document.id
                                                                         //TODO
                                                                    )

                                                                    postList.add(post)
                                                                    adapter = PostAdapter(postList)
                                                                    recyclerView.adapter = adapter
                                                                }
                                                        } else {
                                                            val post = Post("${result.data?.get("FirstName")
                                                                ?.toString()!!} ${result.data?.get("LastName")
                                                                ?.toString()!!}",
                                                                document.data["Content"].toString(),
                                                                result.data?.get("Role")?.toString()!!,
                                                                bitmap,
                                                                null,
                                                                document.data["Tag"].toString(),
                                                                dateString,
                                                                document.id
                                                            )
                                                            postList.add(post)
                                                            adapter = PostAdapter(postList)
                                                            recyclerView.adapter = adapter
                                                        }

                                                    }.addOnFailureListener {
                                                        Toast.makeText(
                                                            activity, "Failed",
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    }
                                            }
                                        }

                                    }.addOnFailureListener {
                                        Toast.makeText(
                                            activity, "Failed",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                    }
                }


            }
        if(tagList.isEmpty()){
            adapter = PostAdapter(postList)
            recyclerView.adapter = adapter
        }
    }

    /**
     * This function is used to create the post adapter.
     * @return Nothing.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}