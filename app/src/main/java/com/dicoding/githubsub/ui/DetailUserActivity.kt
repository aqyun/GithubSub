package com.dicoding.githubsub.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.bumptech.glide.Glide
import com.dicoding.githubsub.R
import com.dicoding.githubsub.data.response.DetailUserResponse
import com.dicoding.githubsub.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserActivity : AppCompatActivity() {

    private lateinit var followerFragment: ListFollowerFragment
    private lateinit var followingFragment: ListFollowingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        // Inisialisasi komponen UI
        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        val viewPager: ViewPager = findViewById(R.id.viewPager)
        val imageViewAvatar = findViewById<ImageView>(R.id.avatarImageView)
        val textViewUsername = findViewById<TextView>(R.id.usernameTextView)
        val textViewName = findViewById<TextView>(R.id.nameTextView)
        val textFollowersCount = findViewById<TextView>(R.id.followersCountTextView)
        val textFollowingCount = findViewById<TextView>(R.id.followingCountTextView)

        // Ambil data dari intent
        val userName = intent.getStringExtra("username") ?: "DefaultUsername"
        val avatarUrl = intent.getStringExtra("avatarUrl") ?: "DefaultAvatarUrl"

        // Inisialisasi ApiService
        val apiService = ApiConfig.getApiService()

        // Lakukan permintaan untuk mendapatkan detail pengguna
        apiService.getDetailUser(userName).enqueue(object : Callback<DetailUserResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                if (response.isSuccessful) {
                    val detailUserResponse = response.body()

                    // Ambil data detail pengguna dari respons
                    val name = detailUserResponse?.name
                    val followersCount = detailUserResponse?.followers ?: 0
                    val followingCount = detailUserResponse?.following ?: 0

                    // Tampilkan data
                    textViewUsername.text = userName
                    textViewName.text = name
                    textFollowersCount.text = "$followersCount Followers"
                    textFollowingCount.text = "$followingCount Following"

                    // Menggunakan Glide untuk memuat dan menampilkan gambar avatar
                    Glide.with(this@DetailUserActivity)
                        .load(avatarUrl)
                        .into(imageViewAvatar)
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                // Handle kesalahan jika permintaan gagal
            }
        })

        // Inisialisasi fragment dan adapter untuk ViewPager
        followerFragment = ListFollowerFragment.newInstance(userName)
        followingFragment = ListFollowingFragment.newInstance(userName)

        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        sectionsPagerAdapter.addFragment(followerFragment, "Followers")
        sectionsPagerAdapter.addFragment(followingFragment, "Following")

        // Atur adapter untuk ViewPager
        viewPager.adapter = sectionsPagerAdapter

        // Hubungkan Tab Layout dengan ViewPager
        tabLayout.setupWithViewPager(viewPager)
    }
}
