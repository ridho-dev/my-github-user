package com.example.mygithubuser.detail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubuser.main.ListUserAdapter
import com.example.mygithubuser.main.User
import com.example.mygithubuser.api.FollowingResponseItem
import com.example.mygithubuser.databinding.FragmentFollowingBinding

class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ListUserAdapter
    private lateinit var followingViewModel: FollowingViewModel

    private val listUser = ArrayList<User>()

    companion object {
        fun getInstance(username: String): Fragment {
            return FollowingFragment().apply {
                arguments = Bundle().apply {
                    putString("USERNAME", username)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        adapter = ListUserAdapter(listUser)

        val username = arguments?.getString("USERNAME").toString()

        showFollowersNotFound(false)

        followingViewModel = ViewModelProvider(this)[FollowingViewModel::class.java]
        followingViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        followingViewModel.getFollowing(username).observe(viewLifecycleOwner) {
            setFollowing(it)
        }

        binding.apply {
            rvFollowing.setHasFixedSize(true)
            rvFollowing.layoutManager = LinearLayoutManager(activity)
            rvFollowing.adapter = adapter
        }

        return binding.root
    }

    private fun setFollowing(following: List<FollowingResponseItem>) {
        listUser.clear()
        if (following.isEmpty()) {
            showFollowersNotFound(true)
        } else {
            showFollowersNotFound(false)
            for (data in following) {
                val user = User(
                    data.avatarUrl,
                    data.login,
                    data.type,
                )
                listUser.add(user)
            }
        }

        val rvFollowers = binding.rvFollowing
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
            binding.followingProgressBar.visibility = View.VISIBLE
        } else {
            binding.followingProgressBar.visibility = View.INVISIBLE
        }
    }

    private fun showFollowersNotFound(isNotFound: Boolean) {
        if (isNotFound) {
            binding.apply {
                ivFollowingNotFound.visibility = View.VISIBLE
                tvFollowingNotFound.visibility = View.VISIBLE
            }
        } else {
            binding.apply {
                ivFollowingNotFound.visibility = View.INVISIBLE
                tvFollowingNotFound.visibility = View.INVISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}