package com.example.githubferdi.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.githubferdi.api.RetrofitClient
import com.example.githubferdi.UserResponse.DetailRes
import com.example.githubferdi.database.UserDao
import com.example.githubferdi.database.UserDB
import com.example.githubferdi.database.UserFav
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : AndroidViewModel(application) {

    private val user = MutableLiveData<DetailRes>()

    private val userDao: UserDao
    private val userDb: UserDB?

    init {
        userDb = UserDB.getDatabase(application)
        userDao = userDb!!.userDao()
    }

    fun setUserDetail(username: String) {
        RetrofitClient.apiInstance
            .getUserDetail(username)
            .enqueue(object : Callback<DetailRes> {
                override fun onResponse(call: Call<DetailRes>, response: Response<DetailRes>) {
                    if (response.isSuccessful) {
                        user.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<DetailRes>, t: Throwable) {
                    Log.d("Ferdinand", "Failed to fetch user details: ${t.message}")
                }
            })
    }

    fun getUserDetail(): LiveData<DetailRes> {
        return user
    }

    fun addToFavorite(username: String, id: Int, avatarUrl: String, htmlUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val userFav = UserFav(
                username,
                id,
                avatarUrl,
                htmlUrl
            )
            userDao.addToFavorite(userFav)
        }
    }

    suspend fun checkUser(id: Int): Int {
        return userDao.checkUserFavorite(id)
    }

    fun removeUserFromFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao.removeUserFromFavorite(id)
        }
    }
}
