package com.isfan17.githubusers.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.isfan17.githubusers.data.model.User

@Database(
    entities = [User::class],
    version = 10
)
abstract class UserDatabase: RoomDatabase() {
    abstract fun getUserDao(): UserDao

    companion object {
        @Volatile
        private var instance: UserDatabase? = null
        fun getInstance(context: Context): UserDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java, "github_user.db"
                ).fallbackToDestructiveMigration().build()
            }
    }
}