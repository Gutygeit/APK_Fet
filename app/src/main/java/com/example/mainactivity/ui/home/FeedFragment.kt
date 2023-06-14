package com.example.mainactivity.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mainactivity.databinding.PostBinding


 class FeedFragment : Fragment() {

    private var _binding: PostBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    /**
     * Creates the view for the fragment
     * @param inflater The layout inflater
     * @param container The view group container
     * @param savedInstanceState The saved instance state
     * @return The view for the fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = PostBinding.inflate(inflater, container, false)


        return binding.root
    }

    /**
     * Destroys the view for the fragment
     * @return Unit
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}