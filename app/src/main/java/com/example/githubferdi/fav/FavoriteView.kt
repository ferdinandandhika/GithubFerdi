package com.example.githubferdi.fav

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.githubferdi.database.UserDB
import com.example.githubferdi.database.UserDao
import com.example.githubferdi.database.UserFav

class FavoriteView(application: Application): AndroidViewModel(application) {

    private var userDao: UserDao?
    private var userDb: UserDB?

    init {
        userDb = UserDB.getDatabase(application)
        userDao = userDb?.userDao()
    }

    fun getUserFavorite(): LiveData<List<UserFav>>? {
        return userDao?.getFavoriteUsers()
    }
}