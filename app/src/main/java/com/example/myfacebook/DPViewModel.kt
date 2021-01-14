package com.example.myfacebook

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storageMetadata

class DPViewModel(val storage: FirebaseStorage, val functions: FirebaseFunctions) : ViewModel() {
    private val uploadStatus = MutableLiveData<Boolean>()
    fun uploadPictureStatus(): LiveData<Boolean> = uploadStatus

    fun uploadImage(uri: Uri) {
        val storageRef = storage.reference
        val metadata = storageMetadata {
            setCustomMetadata(IS_DP, true.toString())
        }
        val imageRef = storageRef.child("images/${uri.lastPathSegment}")
        val uploadTask = imageRef.putFile(uri, metadata)
        uploadTask.addOnSuccessListener {
            val imagePath = "images/${uri.lastPathSegment}"
            functions.getHttpsCallable(UPLOAD_DP).call(imagePath).continueWith {
                uploadStatus.postValue(true)
            }
        }
    }

    companion object {
        private const val IS_DP = "isDP"
        private const val UPLOAD_DP = "uploadDp"
    }
}