package com.example.mygithubuser.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubuser.main.ListUserAdapter
import com.example.mygithubuser.main.User
import com.example.mygithubuser.api.ApiConfig
import com.example.mygithubuser.api.FollowersResponseItem
import com.example.mygithubuser.databinding.FragmentFollowersBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersFragment : Fragment() {

    private var _binding : FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ListUserAdapter

    private val listUser = ArrayList<User>()

    companion object {
        private const val TAG = "FollowersFragment"
        fun getInstance(username : String):Fragment{
            return FollowersFragment().apply {
                arguments = Bundle().apply{
                    putString("USERNAME",username)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        adapter = ListUserAdapter(listUser)
        val username = arguments?.getString("USERNAME").toString()
        binding.apply {
            rvFollowers.setHasFixedSize(true)
            rvFollowers.layoutManager = LinearLayoutManager(activity)
            rvFollowers.adapter = adapter
        }

        getFollowers(username)
        return binding.root
    }

    private fun getFollowers(username: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getFollowersUser(username)
        client.enqueue(object : Callback<List<FollowersResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowersResponseItem>>,
                response: Response<List<FollowersResponseItem>>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null){
                        setFollowers(responseBody)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<FollowersResponseItem>>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    private fun setFollowers(followers: List<FollowersResponseItem>) {
        listUser.clear()
        for (data in followers) {
            val user = User(
                data.avatarUrl,
                data.login,
                data.type,
                data.url
            )
            listUser.add(user)
        }

        val rvFollowers = binding.rvFollowers
        rvFollowers.layoutManager = LinearLayoutManager(activity)
        val listUserAdapter = ListUserAdapter(listUser)
        rvFollowers.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallBack(object : ListUserAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: User) {
        val moveWithDataIntent = Intent(activity, UserDetailActivity::class.java)
        moveWithDataIntent.putExtra(UserDetailActivity.EXTRA_USER, user)
        startActivity(moveWithDataIntent)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.followersProgressBar.visibility = View.VISIBLE
        } else {
            binding.followersProgressBar.visibility = View.INVISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}