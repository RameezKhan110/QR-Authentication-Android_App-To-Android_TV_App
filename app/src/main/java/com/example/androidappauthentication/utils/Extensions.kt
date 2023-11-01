package com.example.androidappauthentication.utils

import android.content.Context
import com.example.androidappauthentication.room.Auth
import com.google.gson.Gson

class Extensions {
    companion object {

        fun saveUser(auth: Auth) {
            val sharedPref = ApplicationClass.getContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()

            val gson = Gson()
            val userDetails = gson.toJson(auth)
            editor.putString("user_obj", userDetails)
            editor.apply()
        }

        fun getUser(): Auth? {
            val sharedPref = ApplicationClass.getContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE)
            val gson = Gson()
            val userDetailsJson = sharedPref.getString("user_obj", "")
            return gson.fromJson(userDetailsJson, Auth::class.java)
        }

        fun clearUser() {
            val sharedPref = ApplicationClass.getContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.clear()
            editor.apply()
        }
    }
}
