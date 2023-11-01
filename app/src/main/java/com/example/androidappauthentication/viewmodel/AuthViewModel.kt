package com.example.androidappauthentication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidappauthentication.repository.AuthRepository
import com.example.androidappauthentication.room.Auth
import com.example.androidappauthentication.room.AuthDatabase
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {

    private val authDao = AuthDatabase.getDatabase().authDao()
    private val authRepo = AuthRepository(authDao)

    private val _registerUser: MutableLiveData<Auth> = MutableLiveData()
    val registerUser: LiveData<Auth> = _registerUser

    fun register(auth: Auth) = viewModelScope.launch {
        authRepo.register(auth)
    }

    fun login(email: String, password: String): LiveData<Auth> {
        return authRepo.login(email, password)
    }

    fun getUserDetails(email: String): LiveData<Auth> {
        return authRepo.getUserDetails(email)
    }
}