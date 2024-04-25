package com.dicoding.myprojectandroidfundamental.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.myprojectandroidfundamental.R
import com.dicoding.myprojectandroidfundamental.adapter.UserAdapter
import com.dicoding.myprojectandroidfundamental.data.response.ItemsItem
import com.dicoding.myprojectandroidfundamental.databinding.ActivityMainBinding
import com.dicoding.myprojectandroidfundamental.model.MainViewModel
import com.dicoding.myprojectandroidfundamental.ui.insert.FavoriteUsersActivity
import com.dicoding.myprojectandroidfundamental.utils.SettingPreferences
import com.dicoding.myprojectandroidfundamental.utils.dataStore
import com.dicoding.myprojectandroidfundamental.viewmodelfactory.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var userName: String = "A"

    private lateinit var mainViewModel: MainViewModel

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = obtainViewModel(this@MainActivity)

        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        }

        supportActionBar?.hide()

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java
        )
        mainViewModel.listItem.observe(this) { userList ->
            setUserData(userList as List<ItemsItem>)
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        with(binding) {
            val layoutManager = LinearLayoutManager(this@MainActivity)
            binding.rvUser.layoutManager = layoutManager
            val itemDecoration = DividerItemDecoration(this@MainActivity, layoutManager.orientation)
            binding.rvUser.addItemDecoration(itemDecoration)

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchView.clearFocus()
                    query?.let {
                        mainViewModel.updateUsername(it)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText?.length ?: 0 >= 3) {
                        newText?.let {
                            mainViewModel.updateUsername(newText)
                        }
                    }

                    if (newText.isNullOrEmpty()) {
                        searchView.clearFocus()
                        searchView.isFocusable = false
                        searchView.isFocusableInTouchMode = false
                        mainViewModel.updateUsername(userName)
                    }
                    return true
                }
            })

            fabFav.setImageTintList(
                ContextCompat.getColorStateList(
                    this@MainActivity,
                    R.color.colorPrimary
                )
            )
            fabFav.setBackgroundTintList(
                ContextCompat.getColorStateList(
                    this@MainActivity,
                    R.color.greyMuda
                )
            )
            fabFav.setOnClickListener(View.OnClickListener {
                startActivity(Intent(this@MainActivity, FavoriteUsersActivity::class.java))
            })

            switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                mainViewModel.saveThemeSetting(isChecked)
            }
        }
    }


    private fun setUserData(users: List<ItemsItem>) {
        val adapter = UserAdapter()
        adapter.submitList(users)
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                val intent = Intent(this@MainActivity, DetailUserActivity::class.java)
                intent.putExtra("USERS_CLICKED", data)
                startActivity(intent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        val factory = ViewModelFactory.getInstance(
            application,
            SettingPreferences.getInstance(application.dataStore)
        )
        return ViewModelProvider(this, factory).get(MainViewModel::class.java)
    }
}