package com.dicoding.githubsub.ui

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    val name: String?,
    val username: String?,
    val followers: String?,
    val following: String?,
    val photo: String
) : Parcelable
