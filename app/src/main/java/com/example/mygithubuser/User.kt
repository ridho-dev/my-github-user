package com.example.mygithubuser

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var photo: String,
    var name: String,
    var userType: String
//    var username: String,
//    var location: String,
//    var repository: String,
//    var company: String,
//    var followers: String,
//    var following: String,
) : Parcelable
