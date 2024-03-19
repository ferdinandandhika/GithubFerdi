package com.example.githubferdi.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubferdi.R
import com.example.githubferdi.UserResponse.User
import com.example.githubferdi.databinding.FragmentUserBinding
import com.example.githubferdi.main.Adapter

class FollowersFrag : Fragment(R.layout.fragment_user) {

    private var _binding : FragmentUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FollowersView
    private lateinit var adapter: Adapter
    private lateinit var username: String


    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        username = args?.getString(DetailUser.EXTRA_USERNAME).toString()

        _binding = FragmentUserBinding.bind(view)

        adapter = Adapter()

        adapter.setOnItemClickCallBack(object : Adapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {

                val intent = Intent(activity, DetailUser::class.java).apply {
                    putExtra(DetailUser.EXTRA_USERNAME, data.login)
                    putExtra(DetailUser.EXTRA_ID, data.id)
                    putExtra(DetailUser.EXTRA_AVATAR, data.avatar_url)
                    putExtra(DetailUser.EXTRA_URL, data.html_url)
                }
                startActivity(intent)
            }
        })

        binding.apply {
            userbar.setHasFixedSize(true)
            userbar.layoutManager = LinearLayoutManager(activity)
            userbar.adapter = adapter
        }

        showLoading(true)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[FollowersView::class.java]
        viewModel.setListFollowers(username)
        viewModel.getListFollowers().observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.setList(it)
                showLoading(false)
            }
            else{
                Toast.makeText(context, "Try again, Failed Fetching", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}