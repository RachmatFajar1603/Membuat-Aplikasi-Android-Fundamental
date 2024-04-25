package com.dicoding.myprojectandroidfundamental.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.myprojectandroidfundamental.database.FavoriteUser
import com.dicoding.myprojectandroidfundamental.database.FavoriteUserDao
import com.dicoding.myprojectandroidfundamental.database.FavoriteUserDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {

    private val mFavoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserDatabase.getDatabase(application)
        mFavoriteUserDao = db.favoriteUserDao()
    }

    fun getAllFavUser(): LiveData<List<FavoriteUser>> = mFavoriteUserDao.getAllFavUser()

    fun getFavUserByUsername(username: String): LiveData<FavoriteUser> =
        mFavoriteUserDao.getFavUserByUsername(username)

    fun insert(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.insert(favoriteUser) }
    }

    fun delete(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.delete(favoriteUser) }
    }

    fun update(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteUserDao.update(favoriteUser) }
    }

}