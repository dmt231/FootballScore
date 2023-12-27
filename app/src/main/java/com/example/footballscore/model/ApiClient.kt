package com.example.footballscore.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiClient {
   private  val BASE_URL : String = "https://api.football-data.org/v4/"
    private var retrofit: Retrofit? = null

    fun getClient() : Retrofit{
        if(retrofit == null){
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}