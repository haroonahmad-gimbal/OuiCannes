package com.gimbal.kotlin.ouicannes.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeViewModel : ViewModel() {

    private val auth = Firebase.auth
    private val _text = MutableLiveData<String>().apply {
        value = "Hello, ${auth.currentUser?.displayName}"
    }
    val text: LiveData<String> = _text
}