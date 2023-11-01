package com.example.androidappauthentication.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.google.firebase.messaging.Constants.MessagePayloadKeys.FROM

@Dao
interface AuthDao {

    @Insert
    suspend fun register(auth: Auth)

    @Query("SELECT * FROM auth_table WHERE email = :email AND password = :password")
    fun login(email: String, password: String): LiveData<Auth>

    @Query("SELECT * FROM auth_table WHERE email = :email")
    fun getUserDetails(email: String): LiveData<Auth>

}