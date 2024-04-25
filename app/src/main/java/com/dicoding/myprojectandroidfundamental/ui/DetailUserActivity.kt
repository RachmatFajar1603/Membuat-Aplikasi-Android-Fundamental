package com.dicoding.myprojectandroidfundamental.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.myprojectandroidfundamental.R
import com.dicoding.myprojectandroidfundamental.adapter.SectionsPagerAdapter
import com.dicoding.myprojectandroidfundamental.data.response.ItemsItem
import com.dicoding.myprojectandroidfundamental.database.FavoriteUser
import com.dicoding.myprojectandroidfundamental.databinding.ActivityDetailUserBinding
import com.dicoding.myprojectandroidfundamental.model.DetailUserViewModel
import com.dicoding.myprojectandroidfundamental.utils.SettingPreferences
import com.dicoding.myprojectandroidfundamental.utils.dataStore
import com.dicoding.myprojectandroidfundamental.viewmodelfactory.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding

    private var _isFavorite: Boolean = false
    private lateinit var detailUserViewModel: DetailUserViewModel


    companion object {
        private const val TAG = "DetailUserActivity"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_followers,
            R.string.tab_text_following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailUserViewModel = obtainViewModel(this@DetailUserActivity)

        detailUserViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        with(binding) {
            val users: ItemsItem? = intent.getParcelableExtra("USERS_CLICKED")
            val usersname = intent.getStringExtra("UERNAME_USERS_CLICKED")

            if (users != null) {

                detailUserViewModel.getDetailUsers(users.login)

                detailUserViewModel.detailUser.observe(
                    this@DetailUserActivity,
                    Observer { detailUser ->
                        detailUser?.let {
                            binding.detailName.text = it.login.toString()
                            binding.detailUsername.text = it.name
                            binding.detailFollower.text = it.followers.toString()
                            binding.detailFollowing.text = it.following.toString()
                            Glide.with(binding.root)
                                .load(it.avatarUrl)
                                .into(binding.detailImage)
                        }
                    })

                detailUserViewModel.getFavoriteUserByUsername(users.login)
                    .observe(this@DetailUserActivity, Observer { userFav ->
                        if (userFav != null) {
                            ivBookmark.setImageResource(R.drawable.baseline_favorite_24)
                            _isFavorite = false
                        } else {
                            ivBookmark.setImageResource(R.drawable.baseline_favorite_border_24)
                            _isFavorite = true
                        }
                    })

                ivBookmark.setOnClickListener(View.OnClickListener {
                    val userFavClick = FavoriteUser(
                        username = users.login,
                        avatar = users.avatarUrl
                    )
                    if (_isFavorite) {
                        detailUserViewModel.insert(userFavClick)
                    } else {
                        detailUserViewModel.delete(userFavClick)
                    }
                })

                ivBack.setOnClickListener(View.OnClickListener { finish() })

                val sectionsPagerAdapter = SectionsPagerAdapter(this@DetailUserActivity)
                sectionsPagerAdapter.username = users.login
                val viewPager: ViewPager2 = findViewById(R.id.view_pager)
                viewPager.adapter = sectionsPagerAdapter
                val tabs: TabLayout = findViewById(R.id.tabs)
                TabLayoutMediator(tabs, viewPager) { tab, position ->
                    tab.text = resources.getString(TAB_TITLES[position])
                }.attach()

            } else {
                Toast.makeText(this@DetailUserActivity, "Data Tidak Ditemukan", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }

            ivBookmark.setImageTintList(
                ContextCompat.getColorStateList(
                    this@DetailUserActivity,
                    R.color.colorPrimary
                )
            )
            ivBookmark.setBackgroundTintList(
                ContextCompat.getColorStateList(
                    this@DetailUserActivity,
                    R.color.greyMuda
                )
            )


        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailUserViewModel {
        val factory = ViewModelFactory.getInstance(
            activity.application,
            SettingPreferences.getInstance(application.dataStore)
        )
        return ViewModelProvider(activity, factory).get(DetailUserViewModel::class.java)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}