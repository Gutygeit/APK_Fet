package com.example.mainactivity.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * This class is used to create a feed view model.
 * @property _text The text.
 * @property text The text.
 * @constructor Creates a feed view model.
 * @return Nothing.
 */
class FeedViewModel: ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "test text"
    }
    val text: LiveData<String> = _text
}