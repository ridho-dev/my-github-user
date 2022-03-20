package com.example.mygithubuser.main

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var photo: String,
    var name: String,
    var userType: String,
) : Parcelable
