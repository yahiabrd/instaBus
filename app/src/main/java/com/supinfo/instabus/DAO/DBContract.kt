package com.supinfo.instabus.DAO

import android.provider.BaseColumns

object DBContract {

    class StationEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "stations"
            val COLUMN_ID_STATION = "idStation"
            val COLUMN_PICTURE_NAME = "pictureName"
            val COLUMN_PICTURE_PATH = "picturePath"
        }
    }
}