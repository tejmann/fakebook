package com.example.myfacebook

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storageMetadata

abstract class ListViewModel() : ViewModel() {
    abstract fun items(): LiveData<List<Post>>
    open fun state(): LiveData<Pair<ListViewModelState, String>>? = null
    abstract fun fetchData(data: String? = null)
    abstract fun onItemClick(post: Post, extras: BaseExtras? = null)
    open fun uploadData(uri: Uri) {}
}

enum class ListViewModelState {
    WALL_SUCCES,
    WALL_FAILED,
    PROFILE
}


class WallViewModel(val functions: FirebaseFunctions, val storage: FirebaseStorage) :
    ListViewModel() {

    private val state = MutableLiveData<Pair<ListViewModelState, String>>()
    override fun state(): LiveData<Pair<ListViewModelState, String>>? = state

    private val list = listOf(Progress())

    private val items = MutableLiveData<List<Post>>(list)
    override fun items(): LiveData<List<Post>> = items

    override fun fetchData(data: String?) {
        val images = mutableListOf<Post>()
        functions.getHttpsCallable(GET_DATA).call().continueWith { task ->
            val result = task.result?.data as List<HashMap<*, *>>
            result.forEach { map ->
                val post = map.toPost()
                post?.let { post ->
                    images.add(post)
                }
            }
            items.postValue(images)
        }

    }

    override fun uploadData(uri: Uri) {
        val storageRef = storage.reference
        val metadata = storageMetadata {
            setCustomMetadata(OWNER_ID, FirebaseAuth.getInstance().uid)
        }
        val imageRef = storageRef.child("images/${uri.lastPathSegment}")
        val uploadTask = imageRef.putFile(uri, metadata)
        uploadTask.addOnFailureListener {
            state.postValue(Pair(ListViewModelState.WALL_FAILED, it.localizedMessage))
        }.addOnSuccessListener {
            state.postValue(Pair(ListViewModelState.WALL_SUCCES, emptyString()))
        }
    }

    override fun onItemClick(post: Post, extras: BaseExtras?) {

    }

    companion object {
        private const val GET_DATA = "getData"
        private const val OWNER_ID = "ownerId"
    }
}

class UsersViewModel(val functions: FirebaseFunctions) : ListViewModel() {
    private val list = listOf(Progress())
    private val items = MutableLiveData<List<Post>>(list)
    override fun items(): LiveData<List<Post>> = items

    override fun fetchData(data: String?) {
        data?.let {
            val users = mutableListOf<Post>()
            functions.getHttpsCallable(GET_USERS).call(data).continueWith {
                val result = it.result?.data as List<HashMap<*, *>>
                result.forEach { map ->
                    val post = map.toPost()
                    post?.let {
                        users.add(it)
                    }
                }
                items.postValue(users)
            }
        }
    }

    override fun onItemClick(post: Post, extras: BaseExtras?) {
    }

    companion object {
        private const val GET_USERS = "getUsers"
    }

}


class ProfileViewModel(val functions: FirebaseFunctions, val auth: FirebaseAuth) : ListViewModel() {
    private val list = listOf(Progress())
    private val items = MutableLiveData<List<Post>>(list)
    override fun items(): LiveData<List<Post>> = items

    private val state = MutableLiveData<Pair<ListViewModelState, String>>()
    override fun state(): LiveData<Pair<ListViewModelState, String>>? = state

    override fun fetchData(data: String?) {
        data?.let {
            val users = mutableListOf<Post>()
            functions.getHttpsCallable(GET_USERS_DATA).call(data).continueWith {
                val result = it.result?.data as List<HashMap<*, *>>
                result.forEach { map ->
                    val post = map.toPost()
                    post?.let {
                        users.add(it)
                    }
                }
                items.postValue(users)
            }
        }
    }

    private fun followUser(uid: String) {
        functions.getHttpsCallable(FOLLOW).call(uid).continueWith {
        }
    }

    private fun signOut() {
        auth.signOut()
        state.postValue(Pair(ListViewModelState.PROFILE, emptyString()))
    }

    private fun handleProfileCLick(post: Profile, extras: BaseExtras?) {
        (extras as? ProfileExtras)?.let {
            when (it.action) {
                ProfileActions.FOLLOW -> followUser(post.data.uid)
                ProfileActions.SIGNOUT -> signOut()
            }
        }
    }

    override fun onItemClick(post: Post, extras: BaseExtras?) = when (post) {
        is Profile -> handleProfileCLick(post, extras)
        else -> {
            //no-op
        }
    }

    companion object {
        private const val GET_USERS_DATA = "getUsersData"
        private const val FOLLOW = "follow"
    }

}