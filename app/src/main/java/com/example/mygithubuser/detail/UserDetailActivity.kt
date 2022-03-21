package com.example.mygithubuser.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.mygithubuser.R
import com.example.mygithubuser.main.User
import com.example.mygithubuser.api.UserDetailResponse
import com.example.mygithubuser.databinding.ActivityUserDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding

    private lateinit var userDetailViewModel: UserDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.tb_detail)
        setSupportActionBar(toolbar)

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User

        supportActionBar?.title = ""

        userDetailViewModel = ViewModelProvider(this)[UserDetailViewModel::class.java]
        userDetailViewModel.getUserDetail(user.name).observe(this) {
            setUserDetail(it)
        }
        userDetailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, user.name)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun setUserDetail(userData: UserDetailResponse?) {
        binding.apply {
            toolbarDetailTitle.text = userData?.name
            Glide.with(this@UserDetailActivity)
                .load(userData?.avatarUrl)
                .circleCrop()
                .into(binding.imgDetailPhoto)
            tvDetailUsername.text = userData?.login
            tvDetailFollowersCount.text = userData?.followers.toString()
            tvDetailFollowingCount.text = userData?.following.toString()
            tvDetailRepositoryCount.text = userData?.publicRepos.toString()
            tvDetailCompany.text = userData?.company ?: "-"
            tvDetailLocation.text = userData?.location ?: "-"
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.userDetailProgressBar.visibility = View.VISIBLE
        } else {
            binding.userDetailProgressBar.visibility = View.INVISIBLE
        }
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        const val TAG = "UserDetailActivity"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}