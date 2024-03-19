package com.example.githubferdi.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubferdi.api.RetrofitClient
import com.example.githubferdi.UserResponse.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersView : ViewModel() {

    val listFollowers = MutableLiveData<ArrayList<User>>()

    fun setListFollowers(username : String) {
        RetrofitClient.apiInstance
            .getUserFollowers(username)
            .enqueue(object : Callback<ArrayList<User>>{
                override fun onResponse(
                    call: Call<ArrayList<User>>,
                    response: Response<ArrayList<User>>
                ) {
                    if (response.isSuccessful) {
                        listFollowers.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                    Log.d("Ferdinand", "Try again, Fetching Failed")
                }
            })
    }

    fun getListFollowers() : LiveData<ArrayList<User>>{
        return listFollowers
    }
}
