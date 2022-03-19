package com.example.mygithubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.mygithubuser.api.ApiConfig
import com.example.mygithubuser.api.UserDetailResponse
import com.example.mygithubuser.databinding.ActivityUserDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding

    companion object {
        const val EXTRA_USER = "extra_user"
        const val TAG = "UserDetailActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.tb_detail)
        setSupportActionBar(toolbar)

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User

        supportActionBar?.title = ""
        getUserDetail(user.name)
    }

    fun getUserDetail(userName: String) {
        val client = ApiConfig.getApiService().getDetailUser(userName)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUserDetail(responseBody)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun setUserDetail(userData: UserDetailResponse?) {
        binding.toolbarDetailTitle.text = userData?.name
        Glide.with(this@UserDetailActivity)
            .load(userData?.avatarUrl)
            .into(binding.imgDetailPhoto)
        binding.tvDetailUsername.text = userData?.login
        binding.tvDetailFollowersCount.text = userData?.followers.toString()
        binding.tvDetailFollowingCount.text = userData?.following.toString()
        binding.tvDetailRepositoryCount.text = userData?.publicRepos.toString()
        binding.tvDetailCompany.text = userData?.company ?: "-"
        binding.tvDetailLocation.text = userData?.location ?: "-"
    }
}