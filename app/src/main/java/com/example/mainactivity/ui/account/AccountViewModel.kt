package com.example.mainactivity.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * This is the ViewModel for the AccountFragment.
 * It is used to store and manage UI-related data in a lifecycle conscious way.
 * This allows data to survive configuration changes such as screen rotations.
 * The data is then available to the fragment to display.
 */
class AccountViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is account Fragment"
    }
    val text: LiveData<String> = _text
}