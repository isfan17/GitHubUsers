package com.isfan17.githubusers.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isfan17.githubusers.data.repository.UserRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val userRepository: UserRepository): ViewModel() {

    fun getUser(username: String) = userRepository.getUser(username)

    fun getUserFollowers(username: String) = userRepository.getUserFollowers(username)

    fun getUserFollowing(username: String) = userRepository.getUserFollowing(username)

    fun addToFavorite(username: String) {
        viewModelScope.launch {
            userRepository.setUserFavorite(username, true)
        }
    }

    fun removeFromFavorite(username: String) {
        viewModelScope.launch {
            userRepository.setUserFavorite(username, false)
        }
    }
}