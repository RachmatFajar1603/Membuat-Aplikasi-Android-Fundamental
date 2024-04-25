package com.dicoding.myprojectandroidfundamental.ui.insert

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.myprojectandroidfundamental.database.FavoriteUser
import com.dicoding.myprojectandroidfundamental.repository.FavoriteUserRepository

class FavoriteUserViewModel(application: Application) : ViewModel() {

    private val mFavUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getAllFavUser(): LiveData<List<FavoriteUser>> = mFavUserRepository.getAllFavUser()
}