package com.example.androidappauthentication.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auth_table")
data class Auth(

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String? = null,
    val email: String? = null,
    val password: String? = null
)
