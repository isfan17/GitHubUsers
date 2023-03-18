package com.isfan17.githubusers.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.isfan17.githubusers.data.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(private val userRepository: UserRepository): ViewModel() {
    fun getUsers() = userRepository.getUsers()

    fun getFavoriteUsers() = userRepository.getFavoriteUsers()

    fun getThemeSetting(): LiveData<Boolean> = userRepository.getThemeSetting().asLiveData()

    fun saveThemeSetting(newSetting: Boolean) {
        viewModelScope.launch {
            userRepository.saveThemeSetting(newSetting)
        }
    }
}