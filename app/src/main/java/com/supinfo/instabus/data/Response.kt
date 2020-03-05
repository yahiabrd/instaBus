package com.supinfo.instabus.data

import com.squareup.moshi.Json

data class Response(@Json(name = "code")
                    val code: Int = 0,
                    @Json(name = "data")
                    val data: Data)