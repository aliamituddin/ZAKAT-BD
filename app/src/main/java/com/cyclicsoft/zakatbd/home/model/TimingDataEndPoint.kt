package com.cyclicsoft.zakatbd.home.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TimingDataEndPoint {

    @GET("/v1/timings")
    fun getTimings(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("method") method: Int
    ): Call<TimingData>

    @GET("/v1/calendar")
    fun getYearlyPrayerTimingsByLatLon(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("method") method: Int,
        @Query("month") month: Int,
        @Query("year") year: Int,
        @Query("annual") annual: Boolean
    ): Call<TimingData>

    @GET("/v1/calendarByCity")
    fun getYearlyPrayerTimingsByCity(
        @Query("city") city: String,
        @Query("country") country: String,
        @Query("method") method: Int,
        @Query("month") month: Int,
        @Query("year") year: Int,
        @Query("annual") annual: Boolean
    ): Call<TimingData>

}