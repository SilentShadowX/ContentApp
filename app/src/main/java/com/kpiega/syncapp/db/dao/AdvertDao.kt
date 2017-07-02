package com.kpiega.syncapp.db.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.kpiega.syncapp.MyApp
import com.kpiega.syncapp.db.model.Advert

/**
 * Created by kpiega on 02.07.2017.
 */

class AdvertDao(context: Context) {

    val database by lazy {
        (context.applicationContext as MyApp).database
    }

    companion object {
        val TABLE_NAME = "Adverts"

        val ID = "_id"
        val CONTENT = "CONTENT"
        val VERSION = "VERSION"

        val SQL_TABLE =
                "CREATE TABLE IF NOT EXISTS $TABLE_NAME ($ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$CONTENT TEXT NOT NULL, " +
                "$VERSION TEXT NOT NULL )"
    }

    fun insert(ad: Advert) {
        val content = ContentValues()
        content.put(AdvertDao.CONTENT, ad.CONTENT)
        content.put(AdvertDao.VERSION, ad.VERSION)
        database.insert(TABLE_NAME, null, content)
    }

    fun getAll(): List<Advert> {

        val list: MutableList<Advert> = ArrayList()

        database.query(TABLE_NAME, null, null, null, null, null, null).use {
            while (it.moveToNext()) {
                val advert = Advert()
                advert._id = it.getLong(0)
                advert.CONTENT = it.getString(1)
                advert.VERSION = it.getString(2)
                list.add(advert)
            }
        }

        return list
    }


}