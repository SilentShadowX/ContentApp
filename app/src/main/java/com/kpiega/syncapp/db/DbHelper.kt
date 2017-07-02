package com.kpiega.syncapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.kpiega.syncapp.db.dao.AdvertDao

/**
 * Created by kpiega on 02.07.2017.
 */

class DbHelper(val context: Context): SQLiteOpenHelper(context, NAME, null, VERSION) {

    companion object {
        val NAME = "sync.db"
        val VERSION = 1
    }

    override fun onCreate(database: SQLiteDatabase) {
        database.execSQL(AdvertDao.SQL_TABLE)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        //updates
    }

}