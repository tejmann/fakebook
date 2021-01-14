package com.example.myfacebook

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class PagerViewModel(val auth: FirebaseAuth) : ViewModel() {
    private val uid = auth.uid
    fun currentUser() = auth.currentUser
    fun getTabs() = listOf(Tab { ListFragment.newInstance(WallViewModel::class.java) },
        Tab {
            ListFragment.newInstance(
                ProfileViewModel::class.java,
                uid
            )
        })
}