package com.dicoding.githubsub.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubsub.data.response.FollowingResponseItem
import com.dicoding.githubsub.databinding.ListItemFollowBinding

class FollowingAdapter : ListAdapter<FollowingResponseItem, FollowingAdapter.FollowingViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    private var following: List<FollowingResponseItem> = emptyList() // Tambahkan properti followers

    fun setData(newFollowing: List<FollowingResponseItem>) {
        following = newFollowing
        notifyDataSetChanged()
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FollowingResponseItem>() {
            override fun areItemsTheSame(oldItem: FollowingResponseItem, newItem: FollowingResponseItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: FollowingResponseItem, newItem: FollowingResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    class FollowingViewHolder(private val binding: ListItemFollowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(following: FollowingResponseItem) {
            binding.tvUsername.text = following.login
            Glide.with(binding.root.context)
                .load(following.avatarUrl)
                .into(binding.ivAvatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder {
        val binding = ListItemFollowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        val following = getItem(position)
        holder.bind(following)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(following)
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            // Buat Intent untuk membuka halaman DetailUserActivity (ganti dengan nama aktivitas yang sesuai)
            val intent = Intent(context, DetailUserActivity::class.java)

            // Sertakan data yang diperlukan ke Intent (misalnya, username, avatarUrl)
            intent.putExtra("username", following.login)
            intent.putExtra("avatarUrl", following.avatarUrl)

            // Mulai aktivitas DetailUserActivity dengan Intent yang telah dikonfigurasi
            context.startActivity(intent)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(following: FollowingResponseItem)
    }
}
