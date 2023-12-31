package com.example.androidtvauthentication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidtvauthentication.model.AuthModel
import com.example.androidtvauthentication.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {

    private val authRepository = AuthRepository()

    private val _getToken: MutableLiveData<String> = MutableLiveData()
    val getToken: LiveData<String> = _getToken

    fun getToken() = viewModelScope.launch{
        _getToken.postValue(authRepository.getFCMToken())
    }
}
