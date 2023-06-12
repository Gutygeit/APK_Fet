package com.example.mainactivity.ui.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mainactivity.databinding.FragmentMessageBinding
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.core.view.isVisible
import com.example.mainactivity.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class MessageFragment : Fragment() {

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!
    private lateinit var messageViewModel: MessageViewModel
    private lateinit var buttonAddPicture: Button
    private lateinit var imageViewMessage: ImageView
    private val pickImage = 100
    private var imageUri: Uri? = null
    private lateinit var auth: FirebaseAuth
    val storage = Firebase.storage("gs://apk-fet.appspot.com")


    override fun onResume(){
        super.onResume()
        val tags = resources.getStringArray(R.array.tags)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, tags)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = FirebaseAuth.getInstance()
        _binding = FragmentMessageBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val db = Firebase.firestore
        var user = auth.currentUser
        val buttonAddPicture: Button = binding.buttonAddPicture
        val imageView: ImageView = binding.imageViewMessage

        // Get the display metrics of the device
        val displayMetrics = DisplayMetrics()
        val windowManager = requireActivity().windowManager
        windowManager.defaultDisplay.getRealMetrics(displayMetrics)

// Calculate the maxHeightPixels based on the screen height
        val maxHeightPixels = (displayMetrics.heightPixels * 0.38).toInt()

        imageView.viewTreeObserver.addOnPreDrawListener {
            val currentHeight = imageView.measuredHeight
            if (currentHeight > maxHeightPixels) {
                imageView.layoutParams.height = maxHeightPixels
                imageView.requestLayout()
            }
            true
        }

        buttonAddPicture.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        binding.buttonSendMessage.setOnClickListener{
            if(!(binding.textInputLayoutMessage.isEmpty()) && !(binding.autoCompleteTextView.text.toString()=="Sélectionner un tag")){
                val docRef = Firebase.firestore.collection("User")
                docRef.whereEqualTo("Mail",user?.email.toString()).get().addOnSuccessListener { result ->
                    Toast.makeText(getActivity(), result.documents[0].id,
                        Toast.LENGTH_LONG).show();
                    var data = hashMapOf<String,Any>()
                    if(binding.imageViewMessage.drawable != null){
                        val storageRef = storage.reference
                        data = hashMapOf(
                            "Auteur" to result.documents[0].id,
                            "Content" to binding.textInputEditTextMessage.text.toString(),
                            "Tag" to binding.autoCompleteTextView.text.toString(),
                            "Image" to "images/${imageUri!!.lastPathSegment}.jpeg",
                            "Date" to FieldValue.serverTimestamp())
                        val riversRef = storageRef.child("images/${imageUri!!.lastPathSegment}.jpeg")
                        riversRef.putFile(imageUri!!)
                    }
                    else {

                        data = hashMapOf(
                            "Auteur" to result.documents[0].id,
                            "Content" to binding.textInputEditTextMessage.text.toString(),
                            "Tag" to binding.autoCompleteTextView.text.toString(),
                            "Image" to "",
                            "Date" to FieldValue.serverTimestamp())
                    }
                    db.collection("Post").add(data)
                }
                imageView.setVisibility(View.INVISIBLE)
            }
            else{
                Toast.makeText(getActivity(), "Certains champs sont vides",
                    Toast.LENGTH_LONG).show();
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
            imageUri = data?.data
            binding.imageViewMessage.setImageURI(imageUri)
            binding.imageViewMessage.setVisibility(View.VISIBLE)
        }
    }
}