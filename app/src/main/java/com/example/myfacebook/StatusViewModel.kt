package com.example.myfacebook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class StatusViewModel(val db: FirebaseFirestore) : ViewModel() {

    fun result(): LiveData<Pair<Boolean, String?>> = result
    private val result = MutableLiveData<Pair<Boolean, String?>>()

    fun postStatus(message: String) {
        val status = Status(StatusData(message, "undefined", "undefined", Timestamp.now()))
        db.collection(OBJECT_COLLECTION)
            .add(status)
            .addOnSuccessListener {
                result.postValue(Pair(true, null))
            }
            .addOnFailureListener { exception ->
                result.postValue(Pair(false, exception.message))
            }
    }

    companion object {
        const val OBJECT_COLLECTION = "objects"
    }

}