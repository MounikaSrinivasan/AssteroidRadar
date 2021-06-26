package com.udacity.asteroidradar.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.db.entity.DatabaseAsteroid

@Dao
@SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
interface AsteroidDatabaseDao {

    @Insert
    suspend fun insert(asteroid: DatabaseAsteroid)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAsteroids( vararg asteroid: DatabaseAsteroid) //asteroids: List<Asteroid>)

    @Query("SELECT * from asteroid_details_table WHERE id = :key")
    suspend fun getAsteroidById(key: Long): DatabaseAsteroid?

    @Query("SELECT * from asteroid_details_table WHERE closeApproachDate = :date")
    suspend fun getAsteroidById(date: String): DatabaseAsteroid?

    @Query("SELECT * FROM asteroid_details_table ORDER BY closeApproachDate DESC LIMIT 1")
    suspend fun getAsteroid(): DatabaseAsteroid?

    @Query("SELECT * FROM asteroid_details_table ORDER BY closeApproachDate")
    fun getAllAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("DELETE FROM asteroid_details_table")
    fun clearAll()

    @Query("DELETE FROM asteroid_details_table where id = :key")
    fun deleteAsteroidById(key: Long)

    @Query("DELETE FROM asteroid_details_table where closeApproachDate < :date")
    fun deleteAsteroidsByDate(date: String)

}