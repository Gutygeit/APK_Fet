@file:Suppress("NAME_SHADOWING")

package com.example.mainactivity.ui.account

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mainactivity.data.PendingPost
import com.example.mainactivity.databinding.FragmentAdminBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File


class AdminFragment : Fragment() {
    private lateinit var adapter: PendingPostAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var postList : ArrayList<PendingPost>
    private lateinit var auth: FirebaseAuth
    private val storage = Firebase.storage("gs://apkfet-a63e3.appspot.com/")
    private var _binding: FragmentAdminBinding? = null
    private val binding get() = _binding!!

    /**
     * This function is called when the fragment is created.
     * It returns the view of the fragment for admin account.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return View
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val layoutManager = LinearLayoutManager(context)
        recyclerView = binding.pendingfeed
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        dataInitialize()

        binding.swipe.setOnRefreshListener {
            dataInitialize()
            binding.swipe.isRefreshing = false

        }
        return root
    }

    private fun getBitmapPP(strImg : String?) : Bitmap {

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
                            val postRef = Firebase.firestore.collection("Pending_Post").orderBy("Date",
                                com.google.firebase.firestore.Query.Direction.ASCENDING)
                            postRef.get().addOnSuccessListener { result ->
                                for (document in result) {
                                    if (document.data["Tag"].toString() in tagList) {
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
                                                        val post = PendingPost(
                                                            result.data?.get("FirstName")
                                                                ?.toString()!!,
                                                            document.data["Content"].toString(),
                                                            bitmap,
                                                            bitmap2,
                                                            document.data["Tag"].toString(),
                                                            document.id
                                                        )

                                                        postList.add(post)
                                                        adapter = PendingPostAdapter(postList)
                                                        recyclerView.adapter = adapter
                                                    }
                                            } else {
                                                val post = PendingPost(
                                                    result.data?.get("FirstName")
                                                        ?.toString()!!,
                                                    document.data["Content"].toString(),
                                                    bitmap,
                                                    null,
                                                    document.data["Tag"].toString(),
                                                    document.id
                                                )
                                                postList.add(post)
                                                adapter = PendingPostAdapter(postList)
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
            adapter = PendingPostAdapter(postList)
            recyclerView.adapter = adapter
        }
    }
}