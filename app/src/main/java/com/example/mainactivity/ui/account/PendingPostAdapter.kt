package com.example.mainactivity.ui.account

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.mainactivity.R
import com.example.mainactivity.data.PendingPost
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream


@Suppress("RedundantWith", "DEPRECATION")
class PendingPostAdapter(private val listPost : ArrayList<PendingPost>) : RecyclerView.Adapter<PendingPostAdapter.ViewHolder>() {

    private var navController: NavController? = null
    private val db = FirebaseFirestore.getInstance()
    val storage = Firebase.storage("gs://apkfet-a63e3.appspot.com/")
    private lateinit var auth: FirebaseAuth

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        navController = Navigation.findNavController(parent.context as AppCompatActivity, R.id.nav_host_fragment_activity_main)
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pendingpost, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = listPost[position]
        auth = FirebaseAuth.getInstance()

        holder.textPost.text = currentItem.Content
        holder.imgPost.setImageBitmap(currentItem.Image)
        holder.imgUser.setImageBitmap(currentItem.ProfileP)
        holder.user.text = currentItem.Auteur
        holder.tag.text = currentItem.Tag



        holder.apply {
            with(holder.acceptbtn) {
                acceptbtn.setOnClickListener {
                    Firebase.firestore.collection("Pending_Post").document(currentItem.Id!!).get()
                        .addOnSuccessListener {
                            var data: HashMap<String, Any>
                            var img = ""
                            val storageRef = storage.reference
                            if (currentItem.Image != null) {

                                val bytes = ByteArrayOutputStream()
                                currentItem.Image?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                                val path = MediaStore.Images.Media.insertImage(
                                    context.contentResolver,
                                    currentItem.Image,
                                    "Title",
                                    null
                                )
                                val imageURI = Uri.parse(path.toString())
                                val riversRef =
                                    storageRef.child("images/${imageURI!!.lastPathSegment}.jpeg")
                                riversRef.putFile(imageURI)
                                img = "images/${imageURI!!.lastPathSegment}.jpeg"
                            }
                            Firebase.firestore.collection("User")
                                .whereEqualTo("FirstName", currentItem.Auteur).get()
                                .addOnSuccessListener {
                                    use->
                                    for(user in use){
                                        data = hashMapOf(
                                            "Auteur" to user.id,
                                            "Content" to holder.textPost.text.toString(),
                                            "Comments" to listOf<String>(),
                                            "Tag" to holder.tag.text.toString(),
                                            "Image" to img,
                                            "Date" to FieldValue.serverTimestamp()
                                        )

                                        db.collection("Post").add(data)

                                    }

                                    Firebase.firestore.collection("Pending_Post").document(
                                        currentItem.Id.toString()
                                    ).delete()


                                }

                        }
                    holder.acceptbtn.visibility = View.GONE
                    holder.rejectbtn.visibility = View.GONE
                    Toast.makeText(
                        itemView.context,
                        "Le post a bien été accepté",
                        Toast.LENGTH_SHORT
                    ).show()
                    }
                }
            }
        holder.apply {
            with(holder.rejectbtn) {
                rejectbtn.setOnClickListener {
                    db.collection("Pending_Post").document(currentItem.Id!!).delete()
                    holder.acceptbtn.visibility = View.GONE
                    holder.rejectbtn.visibility = View.GONE
                    Toast.makeText(
                        itemView.context,
                        "Le post a bien été refusé",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }


    override fun getItemCount(): Int {
        return listPost.size
    }


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val textPost : TextView = itemView.findViewById(R.id.textPost)
        val imgPost : ImageView = itemView.findViewById(R.id.imgPost)
        val imgUser : ImageView = itemView.findViewById(R.id.imgUser)
        val user : TextView = itemView.findViewById(R.id.user)
        val tag : TextView = itemView.findViewById(R.id.tag)
        val acceptbtn : Button = itemView.findViewById(R.id.acceptBtn)
        val rejectbtn : Button = itemView.findViewById(R.id.rejectBtn)
    }



}
