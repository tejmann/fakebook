package com.example.myfacebook

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_dp.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class DPFragment : Fragment() {

    private val storage = Firebase.storage
    private val storageRef = storage.reference
    val db = Firebase.firestore
    val functions = Firebase.functions

    private val viewModel: DPViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dp, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.uploadPictureStatus().observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().popBackStack()
                findNavController().navigate(R.id.tabHostFragment)
            }
        })
        dp_upload_button.setOnClickListener {
            openFile()
        }
        dp_skip_button.setOnClickListener {
            findNavController().navigate(DPFragmentDirections.actionDpFragmentToTabHostFragment())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE) {
            constraint_top.isVisible = false
            constraint_nested.isVisible = true
            data?.data?.let { uri ->
                viewModel.uploadImage(uri)
            }
        }
    }

    private fun openFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"

        }
        startActivityForResult(intent, PICK_IMAGE)
    }

    private val PICK_IMAGE = 2
}