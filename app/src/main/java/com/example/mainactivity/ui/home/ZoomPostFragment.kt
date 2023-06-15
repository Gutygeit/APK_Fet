@file:Suppress("DEPRECATION")

package com.example.mainactivity.ui.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mainactivity.R
import com.example.mainactivity.data.Comment
import com.example.mainactivity.data.Post
import com.example.mainactivity.databinding.FragmentZoomPostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


/**
 * This class is used to create a zoom post fragment.
 * @property post The post.
 * @constructor Creates a zoom post fragment.
 * @return Nothing.
 */
class ZoomPostFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var post: Post

    private var _binding: FragmentZoomPostBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CommentAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var commentList : ArrayList<Comment>
    private lateinit var id : String

    private lateinit var auth: FirebaseAuth
    val storage = Firebase.storage("gs://apkfet-a63e3.appspot.com/")

    /**
     * This function is used to create the view.
     * @param inflater The layout inflater.
     * @param container The view group container.
     * @param savedInstanceState The saved instance state.
     * @return The view.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_zoom_post, container, false)

        _binding = FragmentZoomPostBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManager = LinearLayoutManager(context)
        recyclerView = binding.RecyclerView
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)



        post = arguments?.getParcelable("post")!!

        val textPost = view.findViewById<TextView>(R.id.zoomedTextPost)
        val imgPost = view.findViewById<ImageView>(R.id.zoomedImgPost)
        val imgUser = view.findViewById<ImageView>(R.id.zoomedImgUser)
        val user = view.findViewById<TextView>(R.id.zoomedUser)
        val role = view.findViewById<TextView>(R.id.zoomedRole)
        val tag = view.findViewById<TextView>(R.id.zoomedTag)
        val date = view.findViewById<TextView>(R.id.zoomedDate)



        binding.zoomedTextPost.text = post.Content
        binding.zoomedImgPost.setImageBitmap(post.Image)
        binding.zoomedImgUser.setImageBitmap(post.ProfileP)
        binding.zoomedUser.text = post.Auteur
        binding.zoomedRole.text = post.Role
        binding.zoomedTag.text = post.Tag
        binding.zoomedDate.text = post.date



        id = post.id.toString()

        binding.sendButton.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            val user = auth.currentUser
            val docRef = Firebase.firestore.collection("User")
            docRef.whereEqualTo("Mail",user?.email.toString()).get().addOnSuccessListener { result ->
                var nom = result.documents[0].data!!["LastName"].toString()
                var prenom = result.documents[0].data!!["FirstName"].toString()
                var pp = result.documents[0].data!!["pp"].toString()
                var content = binding.textInputEditTextMessage.text
                var toSend = "$pp $prenom $nom:$content"

                val refPost = db.collection("Post").document(id)
                refPost.update("Comments", FieldValue.arrayUnion(toSend))

                binding.textInputEditTextMessage.text = null
            }


        }

        dataInitialize()

        return root



    }

    private fun dataInitialize() {
        auth = FirebaseAuth.getInstance()
        commentList = arrayListOf()

        Firebase.firestore.collection("Post")
            .document(id).get()
            .addOnSuccessListener { postGotten ->

                var listComment = postGotten.data?.get("Comments") as List<String>
                for(i in listComment.indices){
                    if(!listComment[i].isEmpty()){
                        var infos = listComment[i].split(":")[0]
                        var content = listComment[i].split(":")[1]
                        var ppnum = infos.split(" ")[0]

                        var bitmap : Bitmap
                        bitmap = BitmapFactory.decodeResource(requireContext().resources, com.example.mainactivity.R.drawable.pp1)
                        when (ppnum) {

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

                        var prenom =  infos.split(" ")[1]
                        var nom =  infos.split(" ")[2]
                        var comment = Comment("$prenom $nom", content, bitmap)
                        commentList.add(comment)
                    }
                }

                println(commentList)
                println(commentList)
                println(commentList)
                println(commentList)
                adapter = CommentAdapter(commentList)
                recyclerView.adapter = adapter

                }


    }

    }


