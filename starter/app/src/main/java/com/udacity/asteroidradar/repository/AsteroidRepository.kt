package com.udacity.asteroidradar.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.db.dao.AsteroidDatabaseDao
import com.udacity.asteroidradar.db.entity.asDomainModel
import com.udacity.asteroidradar.network.NEoWsApiService
import com.udacity.asteroidradar.network.asDatabaseModel
import com.udacity.asteroidradar.network.parseAsteroidsJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class AsteroidRepository(
    private val asteroidDatabaseDao: AsteroidDatabaseDao,
    private val networkService: NEoWsApiService
) {

    val asteroidList: LiveData<List<Asteroid>> =
        Transformations.map(asteroidDatabaseDao.getAllAsteroids()) {
            it.asDomainModel()
        }

    suspend fun refreshAsteroids() {
        val date = LocalDateTime.now()

        withContext(Dispatchers.IO) {

            try {

                val asteroids = networkService.getNeoFeed(
                    date.format(DateTimeFormatter.ISO_DATE), date.plusDays(7).format(
                        DateTimeFormatter.ISO_DATE
                    )
                ).await()

                val asteroidList = parseAsteroidsJsonResult(JSONObject(asteroids))

                asteroidDatabaseDao.insertAllAsteroids(*asteroidList.asDatabaseModel())
            } catch (exception: Exception) {
                Log.d("OkHttp Error", "Failure: " + exception.message)
            }
        }
    }

    suspend fun deleteOldAsteroidData() {
        val date = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE)

        withContext(Dispatchers.IO) {
            asteroidDatabaseDao.deleteAsteroidsByDate(date)
        }
    }

}