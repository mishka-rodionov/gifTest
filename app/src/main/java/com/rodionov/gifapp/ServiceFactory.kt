package com.rodionov.gifapp

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by rodionov on 17.02.2020.
 */
class ServiceFactory {

    fun buildNewRetrofit(): Retrofit {

        val client = OkHttpClient.Builder()
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.giphy.com")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return retrofit

    }
}