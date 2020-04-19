package com.cyclicsoft.zakatbd.home.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TimingDataEndPoint {

    @GET("/v1/timings")
    fun getTimings(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("method") method: Long
    ): Call<TimingData>

}