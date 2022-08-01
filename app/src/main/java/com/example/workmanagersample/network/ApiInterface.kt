package com.fitness.modval.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiInterface {
    @GET
    fun sendRequest(
        @Url url:String
    ): Call<ResponseBody>


    companion object{
        operator fun invoke() : ApiInterface{
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.github.com/")
                .build()
                .create(ApiInterface :: class.java)
        }
    }
}