package com.example.androidtvauthentication

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.androidtvauthentication.model.AuthModel
import com.example.androidtvauthentication.viewmodel.AuthViewModel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
open class FirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        try {

            val userEmail = remoteMessage.data["email"]
            val userName = remoteMessage.data["name"]
            Log.d("TAG", "User email and name: $userEmail$userName")
            val intent = Intent("com.example.androidtvauthentication.onMessageReceived")
            intent.putExtra("email", userEmail)
            intent.putExtra("name", userName)
            sendBroadcast(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("TAG", "exception: $e")

        }

    }
}