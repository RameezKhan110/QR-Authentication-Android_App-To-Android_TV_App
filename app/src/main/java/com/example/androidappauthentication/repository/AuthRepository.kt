package com.example.androidappauthentication.repository

import androidx.lifecycle.LiveData
import com.example.androidappauthentication.room.Auth
import com.example.androidappauthentication.room.AuthDao

class AuthRepository(private val authDao: AuthDao) {

    suspend fun register(auth: Auth) {
        authDao.register(auth)
    }

    fun login(email: String, password: String): LiveData<Auth> {
        return authDao.login(email, password)
    }

    fun getUserDetails(email: String): LiveData<Auth> {
        return authDao.getUserDetails(email)
    }
}