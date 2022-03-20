package com.example.mygithubuser.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mygithubuser.R
import com.example.mygithubuser.api.ItemsItem
import com.example.mygithubuser.databinding.ActivityMainBinding
import com.example.mygithubuser.detail.UserDetailActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var rvUsers: RecyclerView
    private lateinit var mainViewModel: MainViewModel

    private val listUser = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.tb_main)
        setSupportActionBar(toolbar)

        rvUsers = findViewById(R.id.rv_users)
        rvUsers.setHasFixedSize(true)

        showUserNotFound(false)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "Search user"
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    mainViewModel.findUser(query).observe(this@MainActivity) {
                        setUser(it)
                    }
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    mainViewModel.findUser(newText).observe(this@MainActivity) {
                        setUser(it)
                    }
                }
                return true
            }
        })
        return true
    }

    private fun setUser(userData: List<ItemsItem>){
        listUser.clear()
        if (userData.isEmpty()) {
            showUserNotFound(true)
        } else {
            showUserNotFound(false)
            for (data in userData) {
                val user = User(
                    data.avatarUrl,
                    data.login,
                    data.type,
                )
                listUser.add(user)
            }
        }

        rvUsers.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter(listUser)
        binding.rvUsers.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallBack(object : ListUserAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: User) {
        val moveWithDataIntent = Intent(this@MainActivity, UserDetailActivity::class.java)
        moveWithDataIntent.putExtra(UserDetailActivity.EXTRA_USER, user)
        startActivity(moveWithDataIntent)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.mainProgressBar.visibility = View.VISIBLE
        } else {
            binding.mainProgressBar.visibility = View.INVISIBLE
        }
    }

    private fun showUserNotFound(isNotFound: Boolean) {
        if (isNotFound) {
            binding.apply {
                ivUserNotFound.visibility = View.VISIBLE
                tvUserNotFound.visibility = View.VISIBLE
            }
        } else {
            binding.apply {
                ivUserNotFound.visibility = View.INVISIBLE
                tvUserNotFound.visibility = View.INVISIBLE
            }
        }
    }
}