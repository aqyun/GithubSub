// ListFollowingFragment.kt
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
import com.dicoding.githubsub.data.response.FollowingResponse
import com.dicoding.githubsub.data.response.FollowingResponseItem
import com.dicoding.githubsub.data.response.ItemsItem // Ganti dengan ItemsItem yang sesuai dengan respons API Anda
import com.dicoding.githubsub.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListFollowingFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FollowingAdapter
    private var username: String? = null

    companion object {
        fun newInstance(username: String): ListFollowingFragment {
            val fragment = ListFollowingFragment()
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
        val view = inflater.inflate(R.layout.fragment_list_following, container, false)

        recyclerView = view.findViewById(R.id.followingRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = FollowingAdapter()
        recyclerView.adapter = adapter

        // Ambil daftar pengikut dari respons API
        val apiService = ApiConfig.getApiService()
        val usernameArg = arguments?.getString("username") ?: ""
        if (!usernameArg.isNullOrEmpty()) {
            username = usernameArg
            apiService.getFollowing(usernameArg).enqueue(object : Callback<List<FollowingResponseItem>> {
                override fun onResponse(
                    call: Call<List<FollowingResponseItem>>,
                    response: Response<List<FollowingResponseItem>>
                ) {
                    if (response.isSuccessful) {
                        val following = response.body() ?: emptyList()
                        adapter.setData(following) // Set data pengikut ke adapter
                    } else {
                        // Handle kesalahan jika respons tidak berhasil
                    }
                }

                override fun onFailure(call: Call<List<FollowingResponseItem>>, t: Throwable) {
                    // Handle kesalahan jika permintaan gagal
                    Log.d("Kesalahan", "onFailure: ${t.message}")
                }
            })
        } else {
            // Handle jika username null atau kosong
        }
        return view
    }
}
