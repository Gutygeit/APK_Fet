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
import com.example.mainactivity.R

class MessageFragment : Fragment() {

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!
    private lateinit var messageViewModel: MessageViewModel
    private lateinit var buttonAddPicture: Button
    private lateinit var imageViewMessage: ImageView
    private val pickImage = 100
    private var imageUri: Uri? = null

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
        _binding = FragmentMessageBinding.inflate(inflater, container, false)
        val root: View = binding.root

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
        }
    }
}