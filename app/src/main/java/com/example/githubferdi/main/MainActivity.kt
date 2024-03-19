package com.example.githubferdi.main

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubferdi.set.Setting
import com.example.githubferdi.UserResponse.User
import com.example.githubferdi.databinding.ActivityMainBinding
import com.example.githubferdi.detail.DetailUser
import com.example.githubferdi.fav.Favorite
import com.example.githubferdi.set.SettingTheme
import com.example.githubferdi.set.dataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: Adapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        adapter = Adapter()
        adapter.notifyDataSetChanged()


        val pref = SettingTheme.getInstance(this.dataStore)
        pref.getThemeSetting().asLiveData().observeForever { isDarkModeActive: Boolean ->
            val mode = if (isDarkModeActive) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
            AppCompatDelegate.setDefaultNightMode(mode)
        }

        binding.sett.setOnClickListener {
            val intent = Intent(this, Setting::class.java)
            startActivity(intent)
        }


        binding.favorite.setOnClickListener {

            val intent = Intent(this, Favorite::class.java)
            startActivity(intent)
        }

        binding.author.setOnClickListener {

            val intent = Intent(this, Author::class.java)
            startActivity(intent)
        }

        adapter.setOnItemClickCallBack(object : Adapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                Intent(this@MainActivity, DetailUser::class.java).also {
                    it.putExtra(DetailUser.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailUser.EXTRA_ID, data.id)
                    it.putExtra(DetailUser.EXTRA_AVATAR, data.avatar_url)
                    it.putExtra(DetailUser.EXTRA_URL, data.html_url)
                    startActivity(it)
                }
            }

        })

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]

        binding.apply {
            userbar.layoutManager = LinearLayoutManager(this@MainActivity)
            userbar.setHasFixedSize(true)
            userbar.adapter = adapter

            userSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (!query.isNullOrEmpty()) {
                        searchUser(query)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

            })
        }
        viewModel.getSearchUsers().observe(this) {
            if (it != null) {
                adapter.setList(it)
                showLoading(false)
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.apply {
            if (state) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun searchUser(query: String) {
        showLoading(true)
        viewModel.setSearchUsers(query)
        viewModel.getSearchUsers().observe(this) { users ->
            if (users.isNullOrEmpty()) {
                Toast.makeText(this, "Data Not Found", Toast.LENGTH_SHORT).show()
            } else {
                adapter.setList(users)
            }
            lifecycleScope.launch {
                delay(1000)
                showLoading(false)
            }
        }
    }

}