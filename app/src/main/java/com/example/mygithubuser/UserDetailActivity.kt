package com.example.mygithubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

class UserDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        val toolbar: Toolbar = findViewById(R.id.tb_detail)
        setSupportActionBar(toolbar)

        val toolbarDetailTitle: TextView = findViewById(R.id.toolbar_detail_title)
        val imgDetailPhoto: ImageView = findViewById(R.id.img_detail_photo)
        val tvDetailUsername: TextView = findViewById(R.id.tv_detail_username)
        val tvDetailLocation: TextView = findViewById(R.id.tv_detail_location)
        val tvDetailRepository: TextView = findViewById(R.id.tv_detail_repository_count)
        val tvDetailCompany: TextView = findViewById(R.id.tv_detail_company)
        val tvDetailFollowers: TextView = findViewById(R.id.tv_detail_followers_count)
        val tvDetailFollowing: TextView = findViewById(R.id.tv_detail_following_count)

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User

        supportActionBar?.title = ""
        toolbarDetailTitle.text = user.name
        imgDetailPhoto.setImageResource(user.photo)
        tvDetailUsername.text = user.username
        tvDetailLocation.text = user.location
        tvDetailRepository.text = user.repository
        tvDetailCompany.text = user.company
        tvDetailFollowers.text = user.followers
        tvDetailFollowing.text = user.following

    }
}