package com.isfan17.githubusers.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.isfan17.githubusers.data.repository.UserRepository

class SearchViewModel(private val userRepository: UserRepository): ViewModel() {
    fun getSearchUsers(query: String) = userRepository.getSearchUsers(query)
}