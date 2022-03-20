package com.example.mygithubuser.detail

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.mygithubuser.R
import com.example.mygithubuser.main.User
import com.example.mygithubuser.api.ApiConfig
import com.example.mygithubuser.api.UserDetailResponse
import com.example.mygithubuser.databinding.ActivityUserDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding

    companion object {
        const val EXTRA_USER = "extra_user"
        const val TAG = "UserDetailActivity"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
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


        val sectionsPagerAdapter = SectionsPagerAdapter(this, user.name)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

//    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//
//        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
//            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show()
//        } else if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
//            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show()
//        }
//    }

    private fun getUserDetail(userName: String) {
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