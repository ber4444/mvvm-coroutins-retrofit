package com.server

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.server.pojo.Image
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Fake server to return json responses
 */
class Server {
    private val mockWebServer = MockWebServer()
    private val apiService : ApiService

    init {
        mockWebServer.start()
        enqueueResponse("http/CodeChallengeImages.json")

        val baseUrl = mockWebServer.url("").url().toString()

        val retrofit = buildRetrofit(baseUrl)
        apiService = retrofit.create(ApiService::class.java)

    }

    fun getItemsAsync() : Deferred<Image> {
        return apiService.getItems()
    }

    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            builder.addInterceptor(interceptor)
        }

        return builder.build()
    }

    private fun buildRetrofit(baseUrl: String): Retrofit {
        val gson = GsonBuilder()
            .create()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getOkHttpClient())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private fun enqueueResponse(bodyFileName: String, statusCode: Int = 200) {
        val resource = this::class.java.classLoader!!.getResource(bodyFileName)
        val body = resource.readText()
        mockWebServer.enqueue(
            MockResponse().setResponseCode(statusCode).setBody(body))
    }
}