package com.dicoding.githubsub.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubsub.data.response.GithubResponse
import com.dicoding.githubsub.data.response.ItemsItem
import com.dicoding.githubsub.data.retrofit.ApiConfig
import com.dicoding.githubsub.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter = GithubAdapter()
    private lateinit var viewModel: UserViewModel

    companion object {
        private const val TAG = "MainActivity"
        private const val GITHUB_ID = "arif"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.recyclerView.addItemDecoration(itemDecoration)

        viewModel = ViewModelProvider(this)[UserViewModel::class.java]

        // Inisialisasi adapter dan set item click listener
        adapter.setOnItemClickCallback(object : GithubAdapter.OnItemClickCallback {
            override fun onItemClicked(user: ItemsItem) {
                Log.d("RecyclerViewItemClick", "Item clicked: ${user.login}")
                val intent = Intent(this@MainActivity, DetailUserActivity::class.java)
                intent.putExtra("name", user.login)
                intent.putExtra("username", user.login)
                startActivity(intent)
            }
        })

        binding.recyclerView.adapter = adapter
        setupSearchView()
        findData()
    }

    private fun setupSearchView() {
        // Mengatur listener untuk SearchView
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Panggil fungsi untuk melakukan pencarian berdasarkan query
                query?.let { searchGithubUsers(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Dapat Anda gunakan untuk melakukan pencarian real-time saat pengguna mengetik
                return false
            }
        })
    }

    private fun searchGithubUsers(query: String) {
        viewModel.searchGithubUsers(query) { items ->
            setData(items)
        }
    }

    private fun findData() {
        showLoading(true)
        val client = ApiConfig.getApiService().getSearchGithubUsers(GITHUB_ID)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setData(responseBody.items)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setData(data: List<ItemsItem>) {
        adapter.submitList(data)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
