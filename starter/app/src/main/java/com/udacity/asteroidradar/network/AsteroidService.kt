package com.udacity.asteroidradar.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.PictureOfDay
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val interceptor = HttpLoggingInterceptor()
    .setLevel(HttpLoggingInterceptor.Level.BODY)


private val builder: OkHttpClient.Builder = OkHttpClient().newBuilder()
    .readTimeout(10, TimeUnit.SECONDS)
    .connectTimeout(5, TimeUnit.SECONDS)
    .addInterceptor(interceptor)

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .client(builder.build())
    .baseUrl(BASE_URL)
    .build()

interface NEoWsApiService {
    @GET("neo/rest/v1/feed")
    fun getNeoFeed(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String = API_KEY
    ):
            Deferred<String>

    @GET("planetary/apod")
    fun getImageOfTheDay(@Query("api_key") apiKey: String = API_KEY):
            Call<PictureOfDay>

}

object NEoWsApi {
    val retrofitService: NEoWsApiService by lazy {
        retrofit.create(NEoWsApiService::class.java)
    }
}