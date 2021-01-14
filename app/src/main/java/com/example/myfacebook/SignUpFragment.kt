package com.example.myfacebook

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_signup.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpFragment : Fragment() {

    private val viewModel: SignUpViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.signUpResult().observe(viewLifecycleOwner, Observer {
            when (it.first) {
                SignUpViewModel.SignUpStatus.EMAIL_OK -> {
                    hideEditText()
                }

                SignUpViewModel.SignUpStatus.NAME_OK -> {
                    findNavController().popBackStack(R.id.logInFragment, true)
                    findNavController().navigate(R.id.dpFragment)
                }
                SignUpViewModel.SignUpStatus.FAILED -> {
                    Snackbar.make(view, it.second, Snackbar.LENGTH_SHORT).show()
                }
            }
        })

        signUpButton.setOnClickListener {
            if (signup_id.isVisible) {
                val email = signup_id.editText?.text.toString()
                val password = signup_password.editText?.text.toString()
                viewModel.signUp(email, password)
            } else {
                val name = signup_name.editText?.text.toString()
                viewModel.setName(name)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        setUpEditText()
    }

    private fun hideEditText() {
        signup_id.visibility = View.INVISIBLE
        signup_password.visibility = View.INVISIBLE
        signup_name.visibility = View.VISIBLE
        signup_name.editText?.addTextChangedListener(textWatcher)
        signUpButton.isEnabled = false
        signUpButton.text = getString(R.string.continue_button)
    }

    private fun setUpEditText() {
        signup_id.editText?.addTextChangedListener(textWatcher)
        signup_password.editText?.addTextChangedListener(textWatcher)
    }

    private fun hasRequiredInfo(): Boolean {
        return if (signup_id.isVisible) !(signup_id.editText?.text.isNullOrEmpty()
                || signup_password.editText?.text.isNullOrEmpty())
        else !(signup_name.editText?.text.isNullOrEmpty())
    }


    private val textWatcher by lazy {
        object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                signUpButton.isEnabled = hasRequiredInfo()
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