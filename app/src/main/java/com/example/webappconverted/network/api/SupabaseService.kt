package com.example.webappconverted.network.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Header
import retrofit2.http.Path

data class DailyQuestion(val username:String, val q1:String, val q2:String, val q3:String, val created_at:String)

interface SupabaseService {
    @POST("/rest/v1/daily_questions")
    suspend fun insertDaily(@Body body: DailyQuestion, @Header("apikey") key:String): Response<Any>

    @GET("/rest/v1/secrets?select=*")
    suspend fun getSecrets(@Header("apikey") key:String): Response<List<Map<String,Any>>>
}
