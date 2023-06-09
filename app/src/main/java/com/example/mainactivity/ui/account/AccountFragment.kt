package com.example.mainactivity.ui.account

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mainactivity.MainActivity
import com.example.mainactivity.R
import com.example.mainactivity.WelcomeActivity
import com.example.mainactivity.databinding.FragmentAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.time.Duration.Companion.nanoseconds

class AccountFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentAccountBinding? = null
    private var pp:String = ""

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var deconnect : Button
    private lateinit var confirmer : Button

    private val pickImage = 100
    private var imageUri: Uri? = null
    private lateinit var prenom: EditText
    private lateinit var nom: EditText

    @SuppressLint("WrongThread", "MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = FirebaseAuth.getInstance()
        val view =  inflater.inflate(R.layout.fragment_account, container, false)
        prenom = view.findViewById(R.id.prenom)
        nom = view.findViewById(R.id.nom)


        val docRef = Firebase.firestore.collection("User")
        docRef.whereEqualTo("Mail",auth.currentUser?.email.toString()).get().addOnSuccessListener {
            result->
            for (document in result){

                val gg = document.data["PP"].toString()
                gg?.let {
                    // Assign the value to the global pp variable
                    pp = it

                    var n = document.data["LastName"].toString()
                    var p = document.data["FirstName"].toString()
                    binding.prenom.setText(p)
                    binding.nom.setText(n)

                    Thread.sleep(1000)


                    val StoRef = FirebaseStorage.getInstance().reference.child(pp)
                    val localfile = File.createTempFile(pp.split("/")[1].split(".")[0],pp.split("/")[1].split(".")[1],)
                    StoRef.getFile(localfile).addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                        binding.imageViewpp.setImageBitmap(bitmap)
                    }.addOnFailureListener{
                        Toast.makeText(getActivity(), "This is my Toast message!",
                            Toast.LENGTH_LONG).show();
                    }
                }
            }
        }





        val accountViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.imageViewpp.setOnClickListener{
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        deconnect = binding.deconnect
        deconnect.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(
                activity,
                "Déconnexion réussie",
                Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(activity, WelcomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            requireActivity().finish()

        }

        //TESTS
        confirmer = binding.confirmer
        confirmer.setOnClickListener{
            val txtPrenom = binding.prenom.text
            val txtNom = binding.nom.text
            val docRef = Firebase.firestore.collection("User")

            if (TextUtils.isEmpty(txtNom) || TextUtils.isEmpty(txtPrenom)) //Vérification que les champs ne sont pas vides
                Toast.makeText(
                    activity,
                    "Champs vides !",
                    Toast.LENGTH_SHORT
                ).show()

            else
                docRef.whereEqualTo("Mail",auth.currentUser?.email.toString()).get().addOnSuccessListener { result ->
                    for (document in result) {
                        docRef.document(document.id).update("FirstName", txtPrenom.toString())
                        docRef.document(document.id).update("LastName", txtNom.toString())

                }
                    Toast.makeText(getActivity(), txtPrenom,
                        Toast.LENGTH_LONG).show();
                    //val intent = Intent(activity, MainActivity::class.java)
                    //startActivity(intent)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            val storage = Firebase.storage("gs://apk-fet.appspot.com")
            val storageRef = storage.reference
            imageUri = data?.data
            binding.imageViewpp.setImageURI(imageUri)
            var file = imageUri!!

            val docRef = Firebase.firestore.collection("User")
            docRef.whereEqualTo("Mail",auth.currentUser?.email.toString()).get().addOnSuccessListener { result ->
                for (document in result) {
                    val desertRef = storageRef.child(document.data["PP"].toString())
                    desertRef.delete()
                    docRef.document(document.id).update("PP", "images/${file.lastPathSegment}.jpeg")
                }
            }

            val riversRef = storageRef.child("images/${file.lastPathSegment}.jpeg")
            var uploadTask = riversRef.putFile(file)
            uploadTask.addOnFailureListener {

            }.addOnSuccessListener { taskSnapshot ->

            }


        }
    }



}