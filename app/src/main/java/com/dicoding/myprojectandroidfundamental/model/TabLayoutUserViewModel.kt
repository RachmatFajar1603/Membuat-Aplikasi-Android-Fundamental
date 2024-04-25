package com.dicoding.myprojectandroidfundamental.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.myprojectandroidfundamental.data.response.ItemsItem
import com.dicoding.myprojectandroidfundamental.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TabLayoutUserViewModel : ViewModel() {

    private val _listItem = MutableLiveData<List<ItemsItem>>()
    val listItem: LiveData<List<ItemsItem>> = _listItem

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _showRecycleView = MutableLiveData<Boolean>()
    val showRecycleView: LiveData<Boolean> = _showRecycleView

    private val _showInformation = MutableLiveData<Boolean>()
    val showInformation: LiveData<Boolean> = _showInformation


    companion object {
        private const val TAG = "TabDetailUserViewModel"
    }

    fun getFollowers(username: String) {
        _isLoading.value = true
        _showRecycleView.value = false
        _showInformation.value = false
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (responseBody.size == 0) {
                            _showInformation.value = true

                        } else {
                            _listItem.value = response.body()
                            _showRecycleView.value = true
                        }
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                _showRecycleView.value = false
                _showInformation.value = false

            }
        })
    }

    fun getFollowings(username: String) {
        _isLoading.value = true
        _showRecycleView.value = false
        _showInformation.value = false
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (responseBody.size == 0) {
                            _showInformation.value = true

                        } else {
                            _listItem.value = response.body()
                            _showRecycleView.value = true
                        }
                    }
                } else {

                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                _showRecycleView.value = false
                _showInformation.value = false
            }
        })
    }
}