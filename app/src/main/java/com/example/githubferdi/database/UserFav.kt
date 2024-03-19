package com.example.githubferdi.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "favorite_table")
data class UserFav(
    val login: String,
    @PrimaryKey
    val id: Int,
    val avatarUrl: String,
    val htmlUrl: String

): Serializable
