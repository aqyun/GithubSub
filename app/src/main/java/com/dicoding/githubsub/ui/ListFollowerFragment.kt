// ListFollowerFragment.kt
package com.dicoding.githubsub.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.githubsub.R
import com.dicoding.githubsub.data.response.FollowersResponseItem
import com.dicoding.githubsub.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListFollowerFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FollowerAdapter
    private var username: String? = null

    companion object {
        fun newInstance(username: String): ListFollowerFragment {
            val fragment = ListFollowerFragment()
            val args = Bundle()
            args.putString("username", username)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_follower, container, false)

        recyclerView = view.findViewById(R.id.followerRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = FollowerAdapter()
        recyclerView.adapter = adapter

        // Ambil daftar pengikut dari respons API
        val apiService = ApiConfig.getApiService()
        val usernameArg = arguments?.getString("username") ?: ""
        if (!usernameArg.isNullOrEmpty()) {
            username = usernameArg
            apiService.getFollowers(usernameArg).enqueue(object : Callback<List<FollowersResponseItem>> {
                override fun onResponse(
                    call: Call<List<FollowersResponseItem>>,
                    response: Response<List<FollowersResponseItem>>
                ) {
                    if (response.isSuccessful) {
                        val followers = response.body() ?: emptyList()
                        adapter.setData(followers) // Set data pengikut ke adapter
                    } else {
                        // Handle kesalahan jika respons tidak berhasil
                    }
                }

                override fun onFailure(call: Call<List<FollowersResponseItem>>, t: Throwable) {
                    // Handle kesalahan jika permintaan gagal
                    Log.d("Kesalahan", "onFailure: ${t.message}")
                }
            })
        } else {
            // Handle jika username null atau kosong
            println("Pengguna tidak ditemukan.")
        }
        return view
    }
}
