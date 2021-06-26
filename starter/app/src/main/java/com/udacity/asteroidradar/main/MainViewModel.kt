package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.db.dao.AsteroidDatabaseDao
import com.udacity.asteroidradar.network.NEoWsApi
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RequiresApi(Build.VERSION_CODES.O)
class MainViewModel(
    val database: AsteroidDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val asteroidRepository = AsteroidRepository(database, NEoWsApi.retrofitService)

    val asteroidList: LiveData<List<Asteroid>> = asteroidRepository.asteroidList

    private val _asteroidId = MutableLiveData<Long?>()
    val asteroidId: LiveData<Long?> = _asteroidId

    private val _property = MutableLiveData<PictureOfDay?>()
    val property: LiveData<PictureOfDay?>
        get() = _property


    init {
        getImageOfTheDay()
        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
            asteroidRepository.deleteOldAsteroidData()
        }

    }

    private fun getImageOfTheDay() {
        NEoWsApi.retrofitService.getImageOfTheDay().enqueue(object : Callback<PictureOfDay> {
            override fun onResponse(call: Call<PictureOfDay>, response: Response<PictureOfDay>) {
                _property.value = response.body()
            }

            override fun onFailure(call: Call<PictureOfDay>, t: Throwable) {
                Log.d("OkHttp Error", "Failure: " + t.message)
            }

        })
    }


    fun onAsteroidClicked(asteroidId: Long) {
        _asteroidId.value = asteroidId
    }

    fun resetAsteroidId() {
        _asteroidId.value = null
    }

}