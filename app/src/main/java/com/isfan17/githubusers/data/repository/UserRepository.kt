package com.isfan17.githubusers.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.isfan17.githubusers.data.local.UserDao
import com.isfan17.githubusers.data.remote.api.UserApi
import com.isfan17.githubusers.helper.Result
import com.isfan17.githubusers.helper.SettingPreferences
import com.isfan17.githubusers.data.model.User
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val preferences: SettingPreferences,
    private val userApi: UserApi,
    private val userDao: UserDao
) {
    fun getUsers(): LiveData<Result<List<User>>> = liveData {
        emit(Result.Loading)
        try {
            val users = userApi.getUsers()
            val userList = users.map { user ->
                val isFavorite = userDao.isFavoriteUser(user.login)
                User(
                    login = user.login,
                    htmlUrl = user.htmlUrl,
                    avatarUrl = user.avatarUrl,
                    favorite = isFavorite
                )
            }
            userDao.upsertUsers(userList)
        } catch (e: Exception) {
            Log.d("UserRepository", "getUsers: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<User>>> = userDao.getUsers().map { Result.Success(it) }
        emitSource(localData)
    }

    fun getSearchUsers(query: String): LiveData<Result<List<User>>> = liveData {
        emit(Result.Loading)
        try {
            val response = userApi.getSearchUsers(query)
            val users = response.users
            emit(Result.Success(users))
        } catch (e: Exception) {
            Log.d("UserRepository", "getSearchUsers: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUser(username: String): LiveData<Result<User>> = liveData {
        emit(Result.Loading)
        try {
            val userData = userApi.getUserDetail(username)
            val isFavorite = userDao.isFavoriteUser(userData.login)
            val user = User(
                login = userData.login,
                htmlUrl = userData.htmlUrl,
                avatarUrl = userData.avatarUrl,
                name = userData.name,
                publicRepos = userData.publicRepos,
                followers = userData.followers,
                following = userData.following,
                location = userData.location,
                company = userData.company,
                favorite = isFavorite
            )
            userDao.upsertUser(user)
        } catch (e: Exception) {
            Log.d("UserRepository", "getUser: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<User>> = userDao.getUser(username).map { Result.Success(it) }
        emitSource(localData)
    }

    fun getUserFollowers(username: String): LiveData<Result<List<User>>> = liveData {
        emit(Result.Loading)
        try {
            val followers = userApi.getUserFollowers(username)
            emit(Result.Success(followers))
        } catch (e: Exception) {
            Log.d("UserRepository", "getUserFollowers: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUserFollowing(username: String): LiveData<Result<List<User>>> = liveData {
        emit(Result.Loading)
        try {
            val following = userApi.getUserFollowing(username)
            emit(Result.Success(following))
        } catch (e: Exception) {
            Log.d("UserRepository", "getUserFollowing: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun setUserFavorite(username: String, favoriteState: Boolean) {
        userDao.setUserFavorite(username, favoriteState)
    }

    fun getFavoriteUsers(): LiveData<List<User>> {
        return userDao.getFavoriteUsers()
    }

    fun getThemeSetting(): Flow<Boolean> = preferences.getThemeSetting()

    suspend fun saveThemeSetting(isNightModeActive: Boolean) {
        preferences.saveThemeSetting(isNightModeActive)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            preferences: SettingPreferences,
            userApi: UserApi,
            userDao: UserDao
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(preferences, userApi, userDao)
            }.also { instance = it }
    }
}