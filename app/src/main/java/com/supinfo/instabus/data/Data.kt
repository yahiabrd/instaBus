package com.supinfo.instabus.data

import com.squareup.moshi.Json

data class Data(@Json(name = "nearstations")
                val nearstations: List<NearstationsItem>?,
                @Json(name = "transport")
                val transport: String = "")