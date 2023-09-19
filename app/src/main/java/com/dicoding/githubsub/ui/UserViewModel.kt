package com.dicoding.githubsub.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubsub.data.response.GithubResponse
import com.dicoding.githubsub.data.response.ItemsItem
import com.dicoding.githubsub.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()

    companion object {
        private const val TAG = "UserViewModel"
    }

    fun searchGithubUsers(query: String, onDataReceived: (List<ItemsItem>) -> Unit) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getSearchGithubUsers(query)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        onDataReceived(responseBody.items)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}

