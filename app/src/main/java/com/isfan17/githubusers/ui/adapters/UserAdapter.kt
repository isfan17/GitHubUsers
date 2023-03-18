package com.isfan17.githubusers.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.isfan17.githubusers.R
import com.isfan17.githubusers.data.model.User
import com.isfan17.githubusers.databinding.ItemUserBinding

class UserAdapter: RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder (private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            Glide.with(itemView)
                .load(user.avatarUrl)
                .placeholder(R.drawable.ic_person_24)
                .into(binding.ivAvatar)
            binding.tvUsername.text = user.login
            binding.tvUrl.text = user.htmlUrl

            binding.root.setOnClickListener {
                onItemClickListener?.let { it(user) }
            }

        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.login == newItem.login
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size

    private var onItemClickListener: ((User) -> Unit)? = null

    fun setOnItemClickListener(listener: (User) -> Unit) {
        onItemClickListener = listener
    }
}