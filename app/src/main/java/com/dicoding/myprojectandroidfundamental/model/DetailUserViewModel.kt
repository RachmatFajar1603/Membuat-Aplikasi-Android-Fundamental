package com.dicoding.myprojectandroidfundamental.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.myprojectandroidfundamental.data.response.DetailUserResponse
import com.dicoding.myprojectandroidfundamental.data.retrofit.ApiConfig
import com.dicoding.myprojectandroidfundamental.database.FavoriteUser
import com.dicoding.myprojectandroidfundamental.repository.FavoriteUserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : ViewModel() {

    private val _detaillUser = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detaillUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val mFavUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun getDetailUsers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        responseBody.let {
                            _detaillUser.value = it
                        }
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

    fun insert(favoriteUser: FavoriteUser) {
        mFavUserRepository.insert(favoriteUser)
    }

    fun delete(favoriteUser: FavoriteUser) {
        mFavUserRepository.delete(favoriteUser)
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> {
        return mFavUserRepository.getFavUserByUsername(username)
    }


    companion object {
        private const val TAG = "DetailUserViewModel"
    }
}