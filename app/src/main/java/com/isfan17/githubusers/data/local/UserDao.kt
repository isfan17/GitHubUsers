package com.isfan17.githubusers.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.isfan17.githubusers.data.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getUsers(): LiveData<List<User>>

    @Query("SELECT * FROM users WHERE login = :username")
    fun getUser(username: String): LiveData<User>

    @Query("SELECT * FROM users WHERE favorite = 1")
    fun getFavoriteUsers(): LiveData<List<User>>

    @Query("SELECT EXISTS(SELECT * FROM users WHERE login = :username AND favorite = 1)")
    suspend fun isFavoriteUser(username: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUsers(users: List<User>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUser(user: User)

    @Query("UPDATE users SET favorite = :favorite WHERE login = :username")
    suspend fun setUserFavorite(username: String, favorite: Boolean)
}