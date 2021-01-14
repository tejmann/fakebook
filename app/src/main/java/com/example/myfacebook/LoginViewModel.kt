package com.example.myfacebook

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception

class LoginViewModel(private val auth: FirebaseAuth) : ViewModel() {

    private val loginResponse = MutableLiveData<Pair<LoginStatus, Exception?>>()
    fun loginResponse(): LiveData<Pair<LoginStatus, Exception?>> = loginResponse

    private val forgotPasswordState = MutableLiveData<Pair<Boolean, String>>()
    fun forgotPasswordState(): LiveData<Pair<Boolean, String>> = forgotPasswordState

    fun logIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) loginResponse.postValue(
                    Pair(
                        LoginStatus.SUCCESS,
                        task.exception
                    )
                )
                else loginResponse.postValue(Pair(LoginStatus.FAILED, task.exception))
            }
    }

    fun forgotPassword(email: String) {
        auth.sendPasswordResetEmail(email).addOnSuccessListener {
            forgotPasswordState.postValue(Pair(true, emptyString()))
        }.addOnFailureListener {
            forgotPasswordState.postValue(Pair(false, it.localizedMessage))
        }
    }

    fun updateResponse(status: LoginStatus) = loginResponse.postValue(Pair(status, null))

    enum class LoginStatus {
        LOADED,
        SUCCESS,
        FAILED
    }

}