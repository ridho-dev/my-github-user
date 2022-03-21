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
import com.example.mygithubuser.api.FollowersResponseItem
import com.example.mygithubuser.databinding.FragmentFollowersBinding

class FollowersFragment : Fragment() {

    private var _binding : FragmentFollowersBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ListUserAdapter
    private lateinit var followersViewModel: FollowersViewModel

    private val listUser = ArrayList<User>()

    companion object {
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

        showFollowersNotFound(false)

        followersViewModel = ViewModelProvider(this)[FollowersViewModel::class.java]
        followersViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        followersViewModel.getFollowers(username).observe(viewLifecycleOwner) {
            setFollowers(it)
        }

        binding.apply {
            rvFollowers.setHasFixedSize(true)
            rvFollowers.layoutManager = LinearLayoutManager(activity)
            rvFollowers.adapter = adapter
        }

        return binding.root
    }

    private fun setFollowers(followers: List<FollowersResponseItem>) {
        listUser.clear()
        if (followers.isEmpty()) {
            showFollowersNotFound(true)
        } else {
            showFollowersNotFound(false)
            for (data in followers) {
                val user = User(
                    data.avatarUrl,
                    data.login,
                    data.type,
                )
                listUser.add(user)
            }
        }

        val listUserAdapter = ListUserAdapter(listUser)
        binding.rvFollowers.adapter = listUserAdapter

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

    private fun showFollowersNotFound(isNotFound: Boolean) {
        if (isNotFound) {
            binding.apply {
                ivFollowersNotFound.visibility = View.VISIBLE
                tvFollowersNotFound.visibility = View.VISIBLE
            }
        } else {
            binding.apply {
                ivFollowersNotFound.visibility = View.INVISIBLE
                tvFollowersNotFound.visibility = View.INVISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}