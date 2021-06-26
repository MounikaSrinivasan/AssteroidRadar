package com.udacity.asteroidradar.work

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.db.database.AsteroidDatabase
import com.udacity.asteroidradar.network.NEoWsApi
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

class   DBRefreshWorker(
    val context: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext = context, params = params) {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        val asteroidRepository = AsteroidRepository(
            AsteroidDatabase.getInstance(context).asteroidDatabaseDao,
            NEoWsApi.retrofitService
        )
        return try {
            asteroidRepository.refreshAsteroids()
            asteroidRepository.deleteOldAsteroidData()
            Result.success()
        } catch (exception: HttpException) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "DB_REFRESH"
    }
}