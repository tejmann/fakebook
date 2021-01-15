package com.example.myfacebook

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    private lateinit var searchString: String

    private val viewModel: SearchViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        search_label.setOnClickListener {
            if (this::searchString.isInitialized) findNavController().navigate(
                R.id.action_searchFragment_to_listFragment,
                bundleOf(
                    "view_model_class" to UsersViewModel::class.java,
                    "function_data" to searchString
                )
            )
        }
    }


    override fun onStart() {
        super.onStart()
        setUpEditText()
    }

    private fun setUpEditText() {
        search.editText?.addTextChangedListener(textWatcher)
    }


    private fun hasRequiredInfo(): Boolean {
        return !(search.editText?.text.isNullOrEmpty())
    }

    private val textWatcher by lazy {
        object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                search_label.isVisible = hasRequiredInfo()
                search_label.text = getString(R.string.show_results, s.toString())
                searchString = s.toString().toLowerCase()
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