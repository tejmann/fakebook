package com.example.myfacebook

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

open class ListFragment : Fragment(), ClickListener {
    companion object {
        private const val VIEW_MODEL_CLASS = "view_model_class"
        private const val FUNCTION_DATA = "function_data"

        fun newInstance(
            viewModelClass: Class<out ListViewModel>,
            functionData: String? = null
        ): ListFragment {
            return ListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(VIEW_MODEL_CLASS, viewModelClass)
                    putSerializable(FUNCTION_DATA, functionData)
                }
            }
        }
    }

    private val viewModelClass: Class<out ListViewModel>?
        get() = arguments?.get(VIEW_MODEL_CLASS) as? Class<out ListViewModel>
    private val viewModel: ListViewModel by viewModel { parametersOf(viewModelClass) }

    private val functionData: String?
        get() = arguments?.getString(FUNCTION_DATA)

    private val adapter: DataAdapter by lazy { DataAdapter(PostViewHolderFactory(), this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(context)
        viewModel.state()?.let {
            it.observe(viewLifecycleOwner, Observer { state ->
                when (state.first) {
                    ListViewModelState.PROFILE -> {
                        findNavController().popBackStack()
                        findNavController().navigate(R.id.logInFragment)
                    }
                    ListViewModelState.WALL_SUCCES -> {
                        Snackbar.make(
                            view,
                            R.string.status_post_successful,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    ListViewModelState.WALL_FAILED -> {
                        Snackbar.make(
                            view,
                            state.second,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            })
        }
        viewModel.items().observe(viewLifecycleOwner, Observer {
            adapter.data = it
            recyclerView.setAdapterIfNew(adapter)
        })
    }


    override fun onStart() {
        super.onStart()
        viewModel.fetchData(functionData)
    }

    //region RecyclerView
    private fun RecyclerView.setAdapterIfNew(adapter: DataAdapter) {
        // We need to set the adapter after the initial data is set to retain RecyclerView state across instances
        if (this.adapter != adapter) {
            this.adapter = adapter
        }
    }

    val PICK_PDF_FILE = 2
    var data: Intent? = null

    private fun openFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"

        }
        startActivityForResult(intent, PICK_PDF_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_PDF_FILE) {
            data?.data?.let { uri ->
                viewModel.uploadData(uri)
            }
        }
    }

    override fun onItemClick(post: Post, extras: BaseExtras?) {
        when (extras) {
            is CreateExtras -> handleCreateExtras(post, extras)
            else -> {
                //no-op
            }
        }
        when (post) {
            is Users -> {
                findNavController().navigate(
                    R.id.action_listFragment_to_listFragment2,
                    bundleOf(
                        "view_model_class" to ProfileViewModel::class.java,
                        "function_data" to post.data.uid
                    )
                )
            }
            is Profile -> {
                viewModel.onItemClick(post, extras)
            }
        }
    }

    private fun handleCreateExtras(post: Post, extras: CreateExtras) {
        when (extras.action) {
            CreatePostActions.IMAGE -> openFile()
            CreatePostActions.STATUS -> findNavController().navigate(TabHostFragmentDirections.actionTabHostFragmentToStatusFragment())
            else -> {
                //no-op
            }
        }
    }
}

