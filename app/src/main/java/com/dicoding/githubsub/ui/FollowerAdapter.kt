package com.dicoding.githubsub.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubsub.data.response.FollowersResponseItem
import com.dicoding.githubsub.databinding.ItemRowUserBinding

class FollowerAdapter : ListAdapter<FollowersResponseItem, FollowerAdapter.FollowerViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private var followers: List<FollowersResponseItem> = emptyList() // Tambahkan properti followers

    fun setData(newFollowers: List<FollowersResponseItem>) {
        followers = newFollowers
        notifyDataSetChanged()
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FollowersResponseItem>() {
            override fun areItemsTheSame(oldItem: FollowersResponseItem, newItem: FollowersResponseItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: FollowersResponseItem, newItem: FollowersResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    class FollowerViewHolder(private val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(follower: FollowersResponseItem) {
            binding.tvUsername.text = follower.login
            Glide.with(binding.root.context)
                .load(follower.avatarUrl)
                .into(binding.ivAvatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        val follower = getItem(position)
        holder.bind(follower)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(follower)
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            // Buat Intent untuk membuka halaman DetailUserActivity (ganti dengan nama aktivitas yang sesuai)
            val intent = Intent(context, DetailUserActivity::class.java)

            // Sertakan data yang diperlukan ke Intent (misalnya, username, avatarUrl)
            intent.putExtra("username", follower.login)
            intent.putExtra("avatarUrl", follower.avatarUrl)

            // Mulai aktivitas DetailUserActivity dengan Intent yang telah dikonfigurasi
            context.startActivity(intent)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(follower: FollowersResponseItem)
    }
}
