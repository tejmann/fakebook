package com.example.myfacebook

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_status.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatusFragment : Fragment() {

    private var message: String? = null

    private val viewModel: StatusViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_status, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.result().observe(viewLifecycleOwner, Observer {
            if (it.first) {
                Snackbar.make(view, R.string.status_post_successful, Snackbar.LENGTH_SHORT).show()
            } else {
                it.second?.let { message ->
                    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
                }
            }
            findNavController().navigateUp()
        })
        status_post.editText?.addTextChangedListener(textWatcher)
        toolbar.setNavigationIcon(R.drawable.ic_up)
        toolbar.setNavigationOnClickListener {
            it.findNavController().navigateUp()
        }
        toolbar.inflateMenu(R.menu.post)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_post -> {
                    message?.let { message ->
                        viewModel.postStatus(message)
                        return@setOnMenuItemClickListener true
                    }
                    Snackbar.make(view, R.string.status_post_failed, Snackbar.LENGTH_SHORT)
                        .show()
                    findNavController().navigateUp()
                    true
                }
                else -> false
            }
        }
    }


    private val textWatcher by lazy {
        object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                message = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //no-op
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //no-op
            }
        }

    }
}