package com.dicoding.myprojectandroidfundamental.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.myprojectandroidfundamental.data.response.ItemsItem
import com.dicoding.myprojectandroidfundamental.data.response.UsersResponse
import com.dicoding.myprojectandroidfundamental.data.retrofit.ApiConfig
import com.dicoding.myprojectandroidfundamental.utils.SettingPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel(private val pref: SettingPreferences) : ViewModel() {

    private val _user = MutableLiveData<UsersResponse>()
    val user: LiveData<UsersResponse> = _user

    private val _listItem = MutableLiveData<List<ItemsItem>>()
    val listItem: LiveData<List<ItemsItem>> = _listItem

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userName = MutableLiveData<String>("A")


    companion object {
        private const val TAG = "MainViewModel"
    }

    init {
        findUser(_userName.value!!)
    }

    fun updateUsername(newUsername: String) {
        _userName.value = newUsername
        findUser(newUsername)
    }

    private fun findUser(userName: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers(userName)
        client.enqueue(object : Callback<UsersResponse> {
            override fun onResponse(
                call: Call<UsersResponse>,
                response: Response<UsersResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listItem.value = responseBody.items
                    }
                } else {

                }
            }

            override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }


}


