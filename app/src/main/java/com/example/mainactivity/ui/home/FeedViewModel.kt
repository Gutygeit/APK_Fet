package com.example.mainactivity.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FeedViewModel: ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "test text"
    }
    val text: LiveData<String> = _text
}