package com.server

import com.server.pojo.Image
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface ApiService {
    @GET("/items/list")
    fun getItems() : Deferred<Image>
}