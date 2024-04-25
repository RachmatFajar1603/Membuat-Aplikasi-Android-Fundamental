package com.dicoding.myprojectandroidfundamental.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.myprojectandroidfundamental.adapter.UserAdapter
import com.dicoding.myprojectandroidfundamental.data.response.ItemsItem
import com.dicoding.myprojectandroidfundamental.databinding.FragmentTabLayoutUserBinding
import com.dicoding.myprojectandroidfundamental.model.TabLayoutUserViewModel
import com.dicoding.myprojectandroidfundamental.ui.DetailUserActivity


class TabLayoutUserFragment : Fragment() {

    private lateinit var binding: FragmentTabLayoutUserBinding
    private var position: Int = 0
    private var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTabLayoutUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        val tabLayoutUserViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                TabLayoutUserViewModel::class.java
            )

        tabLayoutUserViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        tabLayoutUserViewModel.showRecycleView.observe(viewLifecycleOwner) {
            showRecycler(it)
        }

        tabLayoutUserViewModel.showInformation.observe(viewLifecycleOwner) {
            showInformation(it)
        }

        tabLayoutUserViewModel.listItem.observe(viewLifecycleOwner) { usersList ->
            setUsersData(usersList)
            usersList?.let {
                if (it.size == 0) {
                    binding.tvInformation.visibility = View.VISIBLE
                }
            }
        }

        with(binding) {
            rvUsers.isNestedScrollingEnabled = false
            val layoutManager = LinearLayoutManager(context)
            binding.rvUsers.layoutManager = layoutManager
            val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
            binding.rvUsers.addItemDecoration(itemDecoration)

            if (position == 1) {
                tabLayoutUserViewModel.getFollowers("$username")
                binding.tvInformation.text = "Doesn't have followers"
            } else {
                tabLayoutUserViewModel.getFollowings("$username")
                binding.tvInformation.text = "Doesn't have followings"

            }
        }
    }

    private fun setUsersData(usersItem: List<ItemsItem>) {
        val adapter = UserAdapter()
        adapter.submitList(usersItem)
        binding.rvUsers.adapter = adapter
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                val intent = Intent(context, DetailUserActivity::class.java)
                intent.putExtra("USERS_CLICKED", data)
                startActivity(intent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.divLoading.visibility = View.VISIBLE
        } else {
            binding.divLoading.visibility = View.GONE
        }
    }

    private fun showRecycler(isLoading: Boolean) {
        if (isLoading) {
            binding.rvUsers.visibility = View.VISIBLE
        } else {
            binding.rvUsers.visibility = View.GONE
        }
    }

    private fun showInformation(isLoading: Boolean) {
        if (isLoading) {
            binding.tvInformation.visibility = View.VISIBLE
        } else {
            binding.tvInformation.visibility = View.GONE
        }
    }

    companion object {
        const val ARG_POSITION = "arg_position"
        const val ARG_USERNAME = "arg_username"
        private const val TAG = "TabDetailFragment"
    }
}