package com.example.mygithubuser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mygithubuser.api.ApiConfig
import com.example.mygithubuser.api.ItemsItem
import com.example.mygithubuser.api.UserResponse
import com.example.mygithubuser.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var rvUsers: RecyclerView
    private val listUser = ArrayList<User>()

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.tb_main)
        setSupportActionBar(toolbar)

        rvUsers = findViewById(R.id.rv_users)
        rvUsers.setHasFixedSize(true)
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
                    findUser(query)
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    findUser(newText)
                }
                return true
            }

        })

        searchView.setOnCloseListener(object : SearchView.OnCloseListener,
            android.widget.SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                Toast.makeText(this@MainActivity, "closed", Toast.LENGTH_SHORT).show()
                return false
            }
        })

        return true
    }

    private fun findUser(query: String) {
        val client = ApiConfig.getApiService().getSearchUsers(query)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null){
                        setUser(responseBody.items)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    private fun setUser(userData: List<ItemsItem>){
        listUser.clear()
        for (data in userData) {
            val user = User(
                data.avatarUrl,
                data.login,
                data.type,
                data.url
            )
            listUser.add(user)
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


}