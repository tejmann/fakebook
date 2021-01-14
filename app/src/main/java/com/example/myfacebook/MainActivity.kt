package com.example.myfacebook

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_MyFaceBook)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (isNetworkConnected()) {
            no_internet.isVisible = false
            nav_host_fragment.isVisible = true
        } else {
            no_internet.isVisible = true
            nav_host_fragment.isVisible = false
        }
    }

    private fun isNetworkConnected() =
        (getSystemService(Context.CONNECTIVITY_SERVICE) as?
                ConnectivityManager)?.activeNetworkInfo?.isConnectedOrConnecting
            ?: false
}