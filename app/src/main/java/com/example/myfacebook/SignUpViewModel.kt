package com.example.myfacebook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions

class SignUpViewModel(val auth: FirebaseAuth, val functions: FirebaseFunctions) : ViewModel() {


    fun signUpResult(): LiveData<Pair<SignUpStatus, String>> = signUpResult
    private val signUpResult = MutableLiveData<Pair<SignUpStatus, String>>()

    fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            signUpResult.postValue(Pair(SignUpStatus.EMAIL_OK, emptyString()))
        }
            .addOnFailureListener {
                signUpResult.postValue(Pair(SignUpStatus.FAILED, it.localizedMessage))
            }
    }

    fun setName(name: String) {
        functions.getHttpsCallable("addName").call(name)
            .addOnSuccessListener {
                signUpResult.postValue(Pair(SignUpStatus.NAME_OK, emptyString()))
            }
    }

    enum class SignUpStatus {
        EMAIL_OK,
        NAME_OK,
        FAILED
    }
}