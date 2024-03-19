package com.example.githubferdi.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubferdi.UserResponse.User
import com.example.githubferdi.databinding.UserListBinding

class Adapter : RecyclerView.Adapter<Adapter.UserViewHolder>(){

    private val list = ArrayList<User>()

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallBack(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(users: ArrayList<User>) {
        list.clear()
        list.addAll(users)
            notifyDataSetChanged()
    }

    inner class UserViewHolder(private val binding: UserListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User){
            binding.apply {

                binding.root.setOnClickListener {
                    onItemClickCallback?.onItemClicked(user)
                }

                usernameText.text = user.login

                Glide.with(itemView)
                    .load(user.avatar_url)
                    .into(userimg)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = UserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(list[position])
    }
    override fun getItemCount(): Int {
        return list.size
    }
    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}