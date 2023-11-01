package com.example.androidtvauthentication.repository

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

class AuthRepository {

    suspend fun getFCMToken(): String {
        var result  = ""
        try {
            result = FirebaseMessaging.getInstance().token.await()
        }catch (e: Exception) {
            e.printStackTrace()
            Log.d("TAG", "error: " + e.message)
        }
        return result
    }
}