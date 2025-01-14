package com.example.template.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.template.data.local.daos.UserDao
import com.example.template.data.local.entities.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        const val NAME_DB = "app_database"
    }
}