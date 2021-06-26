package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.*
import com.udacity.asteroidradar.work.DBRefreshWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidApplication: Application() {

    val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        scheduleAsteroidLoad()
    }

    private fun scheduleAsteroidLoad() {
        val workManager = WorkManager.getInstance(this)

        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(true)
                .apply{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        setRequiresDeviceIdle(true)
                    }
                }
                .build()

        applicationScope.launch {
            val workRequest = PeriodicWorkRequestBuilder<DBRefreshWorker>(1, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()
            workManager.enqueueUniquePeriodicWork(DBRefreshWorker.WORK_NAME, ExistingPeriodicWorkPolicy.REPLACE, workRequest)
        }
    }

}