package com.isfan17.githubusers.data.remote.response

import com.google.gson.annotations.SerializedName
import com.isfan17.githubusers.data.model.User

data class SearchUserResponse(
    @field:SerializedName("total_count")
    val totalCount: Int,

    @field:SerializedName("items")
    val users: List<User>
)
