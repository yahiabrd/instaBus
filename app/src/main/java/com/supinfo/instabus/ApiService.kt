package com.supinfo.instabus

import com.supinfo.instabus.data.Response
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("/bus/nearstation/latlon/41.3985182/2.1917991/1.json")
    fun fetchAllBus(): Call<Response>
}