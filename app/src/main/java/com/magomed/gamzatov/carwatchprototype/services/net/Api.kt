package com.magomed.gamzatov.carwatchprototype.services.net

import com.magomed.gamzatov.carwatchprototype.services.net.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface Api {
    @GET("/cameras")
    fun getCameras(): Call<List<Camera>>

    @GET("/cameras/{id}/cars")
    fun getCars(@Path("id") id: Long): Call<List<Car>>

    @POST("/monitorings")
    fun postMonitoring(@Body body: Monitoring): Call<Void>

    @GET("/monitorings/users/{id}")
    fun getMonitorings(@Path("id") id: Long): Call<List<UserMonitoring>>

    @GET("/monitorings/{id}/disable")
    fun disableMonitoring(@Path("id") id: Long): Call<Void>

    @GET("/notifications")
    fun notifications(): Call<Status>
}