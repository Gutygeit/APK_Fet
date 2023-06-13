package com.example.mainactivity.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mainactivity.data.Post

/**
 * This class is used to create a filter view model.
 * @property listTag The list of tags.
 * @constructor Creates a filter view model.
 * @return Nothing.
 */
class FilterViewModel : ViewModel() {

    //val allTag = arrayListOf<String>() // à remplir via la bdd (en fonction du user)
    // ...
    val listTag = arrayListOf<String>()
    // val listTag = allTag

    fun addTag(tag : String){
        listTag.add(tag)
    }
    fun removeTag(tag : String){
        listTag.remove(tag)
    }
    fun getList() : ArrayList<String> {
        return listTag
    }
    /*
    fun getAll() : ArrayList<String>{
        return allTag
    }
    */
}