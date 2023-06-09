package com.example.mainactivity.ui.home

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mainactivity.data.Post
import com.example.mainactivity.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import kotlin.concurrent.thread


class HomeFragment : Fragment() {

    private lateinit var adapter: PostAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var postList : ArrayList<Post>


    private var _binding: FragmentHomeBinding? = null
    private var feedlayout: LinearLayout? =null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

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

        dataInitialize()

        binding.swipe.getViewTreeObserver().addOnScrollChangedListener(OnScrollChangedListener {
            println(binding.scrollFeed.scrollY)
            binding.swipe.isEnabled = binding.scrollFeed.scrollY == 0
            binding.swipe.isRefreshing = false
            })

        binding.swipe.setOnRefreshListener {
            dataInitialize()

        }
        return root
    }


    private fun dataInitialize(){
        auth = FirebaseAuth.getInstance()
        postList = arrayListOf<Post>()


        val postRef = Firebase.firestore.collection("Post")
        postRef.get().addOnSuccessListener{
                result->
                for (document in result) {
                    val id_auteur = document.data["Auteur"].toString()
                    val docRef = Firebase.firestore.collection("User")
                        .document(id_auteur)
                    docRef.get().addOnSuccessListener { result ->
                        val gg = result.data?.get("PP")?.toString()
                            val StoRef = FirebaseStorage.getInstance().reference.child(gg.toString())
                            val localfile = File.createTempFile(
                                gg!!.split("/").get(1).split(".").get(0),
                                gg.split("/").get(1).split(".").get(1),
                            )
                            Toast.makeText(getActivity(), gg,
                            Toast.LENGTH_LONG).show()
                            StoRef.getFile(localfile).addOnSuccessListener {
                                val image = document.data["Image"].toString()
                                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                                val StoRef2 = FirebaseStorage.getInstance().reference.child(image.toString())
                                val localfile2 = File.createTempFile(
                                    image!!.split("/").get(1).split(".").get(0),
                                    image.split("/").get(1).split(".").get(1),
                                )
                                StoRef2.getFile(localfile2).addOnSuccessListener {
                                    val bitmap2 = BitmapFactory.decodeFile(localfile2.absolutePath)
                                    val post = Post(
                                        document.data["Auteur"].toString(),
                                        document.data["Content"].toString(),
                                        bitmap,
                                        bitmap2,
                                        document.data["Tag"].toString()
                                    )
                                    postList.add(post)
                                    adapter = PostAdapter(postList)
                                    recyclerView.adapter = adapter
                                }
                            }.addOnFailureListener {
                                Toast.makeText(getActivity(),"Failed" ,
                                    Toast.LENGTH_LONG).show();
                            }

                    }.addOnFailureListener{
                        Toast.makeText(getActivity(),"Failed" ,
                            Toast.LENGTH_LONG).show();
                    }
                }

        }.addOnFailureListener {
            Toast.makeText(getActivity(),"Failed" ,
                Toast.LENGTH_LONG).show();
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}