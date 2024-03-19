package com.example.githubferdi.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubferdi.UserResponse.User
import com.example.githubferdi.UserResponse.UserRes
import com.example.githubferdi.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    val listUsers = MutableLiveData<ArrayList<User>>()

    fun setSearchUsers(query : String) {
        RetrofitClient.apiInstance
            .getSearchUser(query)
            .enqueue(object : Callback<UserRes>{
                override fun onResponse(
                    call: Call<UserRes>,
                    response: Response<UserRes>
                ) {
                    if(response.isSuccessful) {
                        listUsers.postValue(response.body()?.items)
                    }
                }

                override fun onFailure(call: Call<UserRes>, t: Throwable) {
                    Log.d("Ferdinand", "Try again. Failed to get User")
                }

            })
    }

    fun getSearchUsers(): LiveData<ArrayList<User>>{
        return listUsers
    }
}