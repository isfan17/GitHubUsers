package com.isfan17.githubusers.helper

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.isfan17.githubusers.data.local.UserDatabase
import com.isfan17.githubusers.data.remote.api.RetrofitInstance
import com.isfan17.githubusers.data.repository.UserRepository

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val preferences = SettingPreferences.getInstance(context.dataStore)
        val userApi = RetrofitInstance.getUserApi()
        val userDao = UserDatabase.getInstance(context).getUserDao()
        return UserRepository.getInstance(preferences, userApi, userDao)
    }
}