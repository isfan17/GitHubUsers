package com.isfan17.githubusers.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity( tableName = "users" )
@Parcelize
data class User(
    @PrimaryKey
    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("html_url")
    val htmlUrl: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    // Detail User Attribute
    @field:SerializedName("name")
    var name: String? = "-",

    @field:SerializedName("public_repos")
    @ColumnInfo("public_repos")
    var publicRepos: Int? = 0,

    @field:SerializedName("followers")
    var followers: Int? = 0,

    @field:SerializedName("following")
    var following: Int? = 0,

    @field:SerializedName("location")
    var location: String? = "-",

    @field:SerializedName("company")
    var company: String? = "-",

    var favorite: Boolean
): Parcelable