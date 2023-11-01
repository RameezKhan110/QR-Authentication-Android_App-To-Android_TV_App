package com.example.androidappauthentication.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.androidappauthentication.utils.ApplicationClass

@Database(entities = [Auth::class], version = 1)
abstract class AuthDatabase: RoomDatabase() {

    abstract fun authDao(): AuthDao

    companion object {
        private var INSTANCE: AuthDatabase? = null
        fun getDatabase(): AuthDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        ApplicationClass.application.baseContext,
                        AuthDatabase::class.java,
                        "myauth_database"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}