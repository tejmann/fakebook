package com.example.myfacebook

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { PagerViewModel(get()) }
    viewModel { (viewModelClass: Class<out ListViewModel>) ->
        get(viewModelClass)
    }
    viewModel {
        WallViewModel(get(), get())
    }
    viewModel {
        LoginViewModel(get())
    }
    viewModel {
        StatusViewModel(get())
    }
    viewModel {
        ProfileViewModel(get(), get())
    }
    viewModel {
        DPViewModel(get(), get())
    }
    viewModel {
        SignUpViewModel(get(), get())
    }
    viewModel {
        SearchViewModel(get())
    }
    viewModel {
        UsersViewModel(get())
    }
    single {
        Firebase.auth
    }
    single {
        Firebase.storage
    }
    single {
        FirebaseFunctions.getInstance()
    }
    single {
        FirebaseFirestore.getInstance()
    }


}

