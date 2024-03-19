package com.example.githubferdi.fav

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubferdi.UserResponse.User
import com.example.githubferdi.database.UserFav
import com.example.githubferdi.databinding.ActivityFavoriteBinding
import com.example.githubferdi.detail.DetailUser
import com.example.githubferdi.main.Adapter
import com.example.githubferdi.set.Setting

class Favorite : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: Adapter
    private lateinit var viewModel: FavoriteView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = Adapter()
        adapter.notifyDataSetChanged()

        viewModel = ViewModelProvider(this)[FavoriteView::class.java]

        binding.apply {
            rvUserFav.setHasFixedSize(true)
            rvUserFav.layoutManager = LinearLayoutManager(this@Favorite)
            rvUserFav.adapter = adapter
            back1.setOnClickListener {
                finish()
            }

        }

        binding.sett.setOnClickListener {
            val intent = Intent(this, Setting::class.java)
            startActivity(intent)
        }

        viewModel.getUserFavorite()?.observe(this) {
            if (it != null) {
                val list = mapList(it)
                adapter.setList(list)
            }
        }



        adapter.setOnItemClickCallBack(object : Adapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                Intent(this@Favorite, DetailUser::class.java).also {
                    it.putExtra(DetailUser.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailUser.EXTRA_ID, data.id)
                    it.putExtra(DetailUser.EXTRA_AVATAR, data.avatar_url)
                    startActivity(it)
                }
            }

        })


    }

    private fun mapList(users: List<UserFav>): ArrayList<User> {
        val listUsers = ArrayList<User>()
        for (user in users) {
            val userMapped = User(
                user.login,
                user.id,
                user.avatarUrl,
                user.htmlUrl
            )
            listUsers.add(userMapped)
        }
        return listUsers
    }
}