package com.udacity.asteroidradar.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.db.entity.DatabaseAsteroid


@JsonClass(generateAdapter = true)
data class NetworkAsteroid(

    @Json(name = "id")
    val id: Long,
    @Json(name = "code_name")
    val codename: String,
    @Json(name = "close_approach_date")
    val closeApproachDate: String,
    @Json(name = "absolute_magnitude")
    val absoluteMagnitude: Double,
    @Json(name = "estimated_diameter_max")
    val estimatedDiameter: Double,
    @Json(name = "relative_velocity")
    val relativeVelocity: Double,
    @Json(name = "miss_distance")
    val distanceFromEarth: Double,
    @Json(name = "is_potentially_hazardous_asteroid")
    val isPotentiallyHazardous: Boolean
)


/**
 * Convert Network results to domain objects
 */
fun List<NetworkAsteroid>.asDomainModel(): List<Asteroid> {
    return this.map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

/**
 * Convert Network results to database objects
 */
fun List<NetworkAsteroid>.asDatabaseModel(): Array<DatabaseAsteroid> {
    return this.map {
        DatabaseAsteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}