package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.db.dao.AsteroidDatabaseDao

class MainViewModelFactory(val application: Application, val asteroidDatabaseDao: AsteroidDatabaseDao): ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return  MainViewModel(asteroidDatabaseDao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}