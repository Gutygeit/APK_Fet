package com.example.mainactivity.ui.home

import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
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
import com.example.mainactivity.R
import com.example.mainactivity.data.Post
import com.example.mainactivity.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.File


class HomeFragment : Fragment() {

    private lateinit var adapter: PostAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var postList : ArrayList<Post>
    private lateinit var model : FilterViewModel


    private var _binding: FragmentHomeBinding? = null
    private var feedlayout: LinearLayout? =null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    val storage = Firebase.storage("gs://apkfet-a63e3.appspot.com")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val LayoutManager = LinearLayoutManager(context)
        recyclerView = binding.recyclerFeed;
        recyclerView.layoutManager = LayoutManager
        recyclerView.setHasFixedSize(true)

        model = ViewModelProvider(requireActivity()).get(FilterViewModel::class.java)


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
                model.getList()
                val userRef = Firebase.firestore.collection("User")
                    .whereEqualTo("Mail", auth.currentUser?.email.toString()).get()
                    .addOnSuccessListener { userGotten ->
                        for (document in userGotten) {
                            Firebase.firestore.collection("User").document(document.id)
                                .collection("Tags").get().addOnSuccessListener { tags ->
                                    for (tag in tags) {
                                        for (tagdata in tag.data) {
                                            if(!(tagdata.key.toString() in model.getList())){
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
                binding.filterFragment.isVisible = false
                Thread.sleep(5000)
                dataInitialize()
            }
            else{

                binding.filterFragment.isVisible = true
            }
        }



        return root
    }


    private fun dataInitialize() {
        auth = FirebaseAuth.getInstance()
        postList = arrayListOf<Post>()
        var tagList = arrayListOf<String>()

        val userRef = Firebase.firestore.collection("User")
            .whereEqualTo("Mail", auth.currentUser?.email.toString()).get()
            .addOnSuccessListener { userGotten ->
                for (document in userGotten) {
                    val tagRef = Firebase.firestore.collection("User").document(document.id)
                        .collection("Tags").get().addOnSuccessListener { tags ->
                        for (tag in tags) {
                            for (tagdata in tag.data) {
                                if (tagdata.value as Boolean) {
                                    tagList.add(tagdata.key.toString())

                                    val postRef = Firebase.firestore.collection("Post")
                                    postRef.get().addOnSuccessListener { result ->
                                        for (document in result) {
                                            Toast.makeText(
                                                getActivity(), (document.data["Tag"].toString() in tagList).toString(),
                                                Toast.LENGTH_LONG
                                            ).show();

                                            if (document.data["Tag"].toString() in tagList) {

                                                val id_auteur = document.data["Auteur"].toString()
                                                val docRef = Firebase.firestore.collection("User")
                                                    .document(id_auteur)
                                                docRef.get().addOnSuccessListener { result ->
                                                    val gg = result.data?.get("PP")?.toString()
                                                    val StoRef =
                                                        storage.reference.child(gg.toString())
                                                    val localfile = File.createTempFile(
                                                        gg!!.split("/").get(1).split(".").get(0),
                                                        gg.split("/").get(1).split(".").get(1),
                                                    )

                                                    StoRef.getFile(localfile).addOnSuccessListener {
                                                        val image =
                                                            document.data["Image"].toString()
                                                        val bitmap =
                                                            BitmapFactory.decodeFile(localfile.absolutePath)
                                                        if (!(image.contentEquals(""))) {
                                                            val StoRef2 =
                                                                storage.reference.child(image.toString())
                                                            val localfile2 = File.createTempFile(
                                                                image!!.split("/").get(1).split(".")
                                                                    .get(0),
                                                                image.split("/").get(1).split(".")
                                                                    .get(1),
                                                            )
                                                            StoRef2.getFile(localfile2)
                                                                .addOnSuccessListener {
                                                                    val bitmap2 =
                                                                        BitmapFactory.decodeFile(
                                                                            localfile2.absolutePath
                                                                        )
                                                                    val post = Post(
                                                                        result.data?.get("FirstName")
                                                                            ?.toString()!!,
                                                                        document.data["Content"].toString(),
                                                                        bitmap,
                                                                        bitmap2,
                                                                        document.data["Tag"].toString()
                                                                    )

                                                                    postList.add(post)
                                                                    adapter = PostAdapter(postList)
                                                                    recyclerView.adapter = adapter
                                                                }
                                                        } else {
                                                            val post = Post(
                                                                result.data?.get("FirstName")
                                                                    ?.toString()!!,
                                                                document.data["Content"].toString(),
                                                                bitmap,
                                                                null,
                                                                document.data["Tag"].toString()
                                                            )
                                                            postList.add(post)
                                                            adapter = PostAdapter(postList)
                                                            recyclerView.adapter = adapter
                                                        }

                                                    }.addOnFailureListener {
                                                        Toast.makeText(
                                                            getActivity(), "Failed",
                                                            Toast.LENGTH_LONG
                                                        ).show();
                                                    }

                                                }.addOnFailureListener {
                                                    Toast.makeText(
                                                        getActivity(), "Failed",
                                                        Toast.LENGTH_LONG
                                                    ).show();
                                                }
                                            }
                                        }

                                    }.addOnFailureListener {
                                        Toast.makeText(
                                            getActivity(), "Failed",
                                            Toast.LENGTH_LONG
                                        ).show();
                                    }

                                }

                            }
                        }
                    }
                }


            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}