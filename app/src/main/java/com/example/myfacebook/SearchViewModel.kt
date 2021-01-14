package com.example.myfacebook

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.functions.FirebaseFunctions

class SearchViewModel(val functions: FirebaseFunctions) : ViewModel() {

    fun fetchUsers(search: String) {
        functions.getHttpsCallable("getUsers").call(search).continueWith {
            val result = it.result?.data
            Log.d("SEARCH", result.toString())
        }
    }
}