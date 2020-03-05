package com.supinfo.instabus.DAO

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class StationsDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    @Throws(SQLiteConstraintException::class)
    fun insertStation(station: StationModel): Boolean {
        val db = writableDatabase

        val values = ContentValues()
        values.put(DBContract.StationEntry.COLUMN_ID_STATION, station.idStation)
        values.put(DBContract.StationEntry.COLUMN_PICTURE_NAME, station.pictureName)
        values.put(DBContract.StationEntry.COLUMN_PICTURE_PATH, station.picturePath)
        val newRowId = db.insert(DBContract.StationEntry.TABLE_NAME, null, values)

        return true
    }

    fun readAllStations(id: String): ArrayList<StationModel> {
        val stations = ArrayList<StationModel>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.StationEntry.TABLE_NAME + " where " + DBContract.StationEntry.COLUMN_ID_STATION + "=" + id, null)

        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var idStation: String
        var pictureName: String
        var picturePath: String
        if (cursor!!.moveToFirst()) {
            while (!cursor.isAfterLast) {
                idStation = cursor.getString(cursor.getColumnIndex(DBContract.StationEntry.COLUMN_ID_STATION))
                pictureName = cursor.getString(cursor.getColumnIndex(DBContract.StationEntry.COLUMN_PICTURE_NAME))
                picturePath = cursor.getString(cursor.getColumnIndex(DBContract.StationEntry.COLUMN_PICTURE_PATH))

                stations.add(StationModel(idStation, pictureName, picturePath))
                cursor.moveToNext()
            }
        }
        cursor.close()
        return stations
    }


    fun deleteDetailStation(picturePath: String) {
        val db = this.writableDatabase
        val retVal = db.delete("stations", "picturePath = '${picturePath}'", null)
    }

    companion object {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "instaBus.db"

        private val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBContract.StationEntry.TABLE_NAME + "(" +
                        DBContract.StationEntry.COLUMN_ID_STATION + " TEXT NOT NULL," +
                        DBContract.StationEntry.COLUMN_PICTURE_NAME + " TEXT NOT NULL," +
                        DBContract.StationEntry.COLUMN_PICTURE_PATH + " TEXT NOT NULL)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBContract.StationEntry.TABLE_NAME
    }

}