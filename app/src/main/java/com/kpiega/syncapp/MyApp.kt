package com.kpiega.syncapp

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import com.kpiega.syncapp.db.DbHelper

/**
 * Created by kpiega on 02.07.2017.
 */

class MyApp: Application() {

    lateinit var database: SQLiteDatabase

    override fun onCreate() {
        super.onCreate()
        database = DbHelper(this).writableDatabase
    }


}