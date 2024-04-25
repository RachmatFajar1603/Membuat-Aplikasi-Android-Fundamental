package com.dicoding.myprojectandroidfundamental.ui.insert

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.myprojectandroidfundamental.data.response.ItemsItem
import com.dicoding.myprojectandroidfundamental.database.FavoriteUser
import com.dicoding.myprojectandroidfundamental.databinding.ActivityFavoriteUserBinding
import com.dicoding.myprojectandroidfundamental.ui.DetailUserActivity
import com.dicoding.myprojectandroidfundamental.utils.SettingPreferences
import com.dicoding.myprojectandroidfundamental.utils.dataStore
import com.dicoding.myprojectandroidfundamental.viewmodelfactory.ViewModelFactory

class FavoriteUsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding

    private lateinit var adapter: FavoriteUsersAdapter

    private lateinit var favoriteUsersViewModel: FavoriteUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            toolbar.navigationIcon?.setTint(resources.getColor(android.R.color.white))

            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }

            adapter = FavoriteUsersAdapter()
            rvUser.layoutManager = LinearLayoutManager(this@FavoriteUsersActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter

            favoriteUsersViewModel = obtainViewModel(this@FavoriteUsersActivity)

            favoriteUsersViewModel.getAllFavUser()
                .observe(this@FavoriteUsersActivity) { usersFavList ->
                    if (usersFavList != null) {
                        adapter.setListUsersFavorite(usersFavList)
                    }
                }

            adapter.setOnItemClickCallback(object : FavoriteUsersAdapter.OnItemClickCallback {
                override fun onItemClicked(data: FavoriteUser) {
                    val userFavClick = ItemsItem(
                        login = data.username,
                        avatarUrl = data.avatar!!
                    )
                    val intent = Intent(this@FavoriteUsersActivity, DetailUserActivity::class.java)
                    intent.putExtra("USERS_CLICKED", userFavClick)
                    startActivity(intent)
                }
            })
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteUserViewModel {
        val factory = ViewModelFactory.getInstance(
            activity.application,
            SettingPreferences.getInstance(application.dataStore)
        )
        return ViewModelProvider(activity, factory).get(FavoriteUserViewModel::class.java)
    }


}