package com.example.myfacebook

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LogInFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.forgotPasswordState().observe(viewLifecycleOwner, Observer { response ->
            if (response.first) {
                Snackbar.make(view, R.string.password_reset, Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(view, response.second, Snackbar.LENGTH_SHORT).show()
            }
        })
        viewModel.loginResponse().observe(viewLifecycleOwner, Observer { response ->
            when (response.first) {
                LoginViewModel.LoginStatus.SUCCESS -> {
                    viewModel.updateResponse(LoginViewModel.LoginStatus.LOADED)
                    findNavController().popBackStack()
                    findNavController()
                        .navigate(R.id.tabHostFragment)
                }
                LoginViewModel.LoginStatus.FAILED -> Snackbar.make(
                    view,
                    response.second?.message.toString(),
                    Snackbar.LENGTH_LONG
                ).show()
                else -> {
                    //no-op
                }
            }
        })
        loginButton.setOnClickListener {
            val email = login_id.editText?.text.toString()
            val password = login_password.editText?.text.toString()
            viewModel.logIn(email, password)
        }

        create_account.setOnClickListener {
            findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToSignUpFragment())
        }

        forgot_password.setOnClickListener {
            val email = login_id.editText?.text.toString()
            if (email.isNullOrEmpty()) {
                Snackbar.make(view, R.string.enter_email, Snackbar.LENGTH_SHORT).show()
            } else {
                viewModel.forgotPassword(email)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        setUpEditText()
    }

    private fun setUpEditText() {
        login_id.editText?.addTextChangedListener(textWatcher)
        login_password.editText?.addTextChangedListener(textWatcher)
    }

    private fun hasRequiredInfo(): Boolean {
        return !(login_id.editText?.text.isNullOrEmpty()
                || login_password.editText?.text.isNullOrEmpty())
    }

    private val textWatcher by lazy {
        object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                loginButton.isEnabled = hasRequiredInfo()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //no-op
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //no-op
            }
        }

    }

    override fun onStop() {
        super.onStop()
        login_id.editText?.removeTextChangedListener(textWatcher)
        login_password.editText?.removeTextChangedListener(textWatcher)
    }
}