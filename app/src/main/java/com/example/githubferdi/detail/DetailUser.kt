package com.example.githubferdi.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubferdi.R
import com.example.githubferdi.databinding.ActivityDetailBinding
import com.example.githubferdi.fav.Favorite
import com.example.githubferdi.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUser : AppCompatActivity() {

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVATAR = "extra_avatar"
        const val EXTRA_URL = "extra_url"
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailUserViewModel

    private var clicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatar = intent.getStringExtra(EXTRA_AVATAR)
        val url = intent.getStringExtra(EXTRA_URL)
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        viewModel = ViewModelProvider(this)[DetailUserViewModel::class.java]

        binding.progressBar.visibility = View.VISIBLE

        var isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main){
                if (count != null) {
                    if (count > 0) {
                        binding.fav.isChecked = true
                        isChecked = true
                    } else {
                        binding.fav.isChecked = false
                        isChecked = false
                    }
                }
            }
        }

        binding.favorite.setOnClickListener {

            val intent = Intent(this, Favorite::class.java)
            startActivity(intent)
        }

        binding.fav.setOnClickListener {
            isChecked = !isChecked
            if (isChecked) {
                if (username != null) {
                    if (avatar != null) {
                        if (url != null) {
                            viewModel.addToFavorite(username, id, avatar, url)
                            Toast.makeText(this, "Successfully Added to Favorites", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                viewModel.removeUserFromFavorite(id)
                Toast.makeText(this, "Successfully Removed to Favorites", Toast.LENGTH_SHORT).show()
            }
            binding.fav.isChecked = isChecked
        }

        if (username != null) {
            viewModel.setUserDetail(username)
            viewModel.getUserDetail().observe(this) {
                if (it != null) {
                    binding.apply {
                        nameDtl.text = it.name
                        nameaccount.text = it.login
                        usernameDtl.text = it.login
                        followersDtl.text = resources.getString(R.string.follower_many, it.followers)
                        followingDtl.text = resources.getString(R.string.following_many, it.following)

                        Glide.with(this@DetailUser)
                            .load(it.avatar_url)
                            .into(userDtl)

                        bioDtl.text = it.bio

                        progressBar.visibility = View.GONE
                    }
                }
            }
        }



        val SectionAdapter = SectionAdapter(this, supportFragmentManager, bundle)
        binding.apply {
            viewPager.adapter = SectionAdapter
            tabLayout.setupWithViewPager(viewPager)
        }

        binding.apply {

            back1.setOnClickListener {
                finish()
            }

            share.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                val message = if (url != null) {
                    "Let's join and follow on your Github: $url"
                } else {
                    "Let's join and follow on your Github: $username"
                }
                intent.putExtra(Intent.EXTRA_TEXT, message)
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, "Send To"))
            }

            binding.github.setOnClickListener {
                navigateToMainActivity()
            }

        }
    }
    
    
    
    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    
}