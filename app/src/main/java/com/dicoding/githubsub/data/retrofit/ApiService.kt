package com.dicoding.githubsub.data.retrofit

import com.dicoding.githubsub.data.response.DetailUserResponse
import com.dicoding.githubsub.data.response.FollowersResponseItem
import com.dicoding.githubsub.data.response.FollowingResponseItem
import com.dicoding.githubsub.data.response.GithubResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchGithubUsers(
        @Query("q") query: String)
    : Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String)
    : Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<FollowersResponseItem>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<FollowingResponseItem>>

}