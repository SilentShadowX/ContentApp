package com.kpiega.syncapp.provider

import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import com.kpiega.syncapp.db.DbHelper
import com.kpiega.syncapp.db.dao.AdvertDao
import android.database.sqlite.SQLiteDatabase
import com.kpiega.syncapp.MyApp


/**
 * Created by kpiega on 02.07.2017.
 */

class AdvertProvider : ContentProvider() {

    private lateinit var database: SQLiteDatabase

    companion object {
        private val ADVERTS = 100
        private val ADVERTS_TYPE = "/adverts"

        private val ADVERT_ID = 101
        private val ADVERT_TYPE_ID = "/advert"

        private val AUTHORITY = "com.kpiega.syncapp"

        private val PATH = "advert"

        val CONTENT_URI = Uri.parse("content://$AUTHORITY/$PATH")

        val CONTENT_TYPE_ADVERTS = ContentResolver.CURSOR_DIR_BASE_TYPE + ADVERTS_TYPE
        val CONTENT_TYPE_ADVERT_ID = ContentResolver.CURSOR_ITEM_BASE_TYPE + ADVERT_TYPE_ID

        val uriMatcher: UriMatcher = UriMatcher(UriMatcher.NO_MATCH)
    }

    init {
        uriMatcher.addURI(AUTHORITY, PATH, ADVERTS)
        uriMatcher.addURI(AUTHORITY, "$PATH/#", ADVERT_ID)
    }

    override fun onCreate(): Boolean {
        database = DbHelper(context).writableDatabase
        return database != null
    }

    override fun query(
            uri: Uri?,
            projection: Array<out String>?,
            selection: String?,
            selectionArgs: Array<out String>?,
            sortOrder: String?
    ): Cursor {

        val uriType = uriMatcher.match(uri)
        val queryBuilder = SQLiteQueryBuilder()

        queryBuilder.tables = AdvertDao.TABLE_NAME

        when (uriType) {
            ADVERT_ID ->
                queryBuilder.appendWhere("${AdvertDao.ID} = ${uri?.lastPathSegment}")
            else -> throw IllegalArgumentException("Unknown URI: " + uri)
        }

        val cursor = queryBuilder.query(database, projection, selection,
                selectionArgs, null, null, sortOrder)

        cursor.setNotificationUri(context.contentResolver, uri)

        return cursor
    }

    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        val uriType = uriMatcher.match(uri)
        var id = 0L
        when (uriType) {
            ADVERTS -> {

            }
            ADVERT_ID -> id = database.insert(AdvertDao.TABLE_NAME, null, values)
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        context.contentResolver.notifyChange(uri, null)
        return Uri.parse(PATH + "/" + id)
    }


    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        val uriType = uriMatcher.match(uri)
        var updateRow = 0

        when (uriType) {
            ADVERTS -> {
                updateRow = database.update(AdvertDao.TABLE_NAME, values, selection, selectionArgs)
            }
            ADVERT_ID -> {
                val id = uri?.lastPathSegment

                if (selection.isNullOrEmpty()) {
                    updateRow = database.update(
                            AdvertDao.TABLE_NAME,
                            values,
                            "${AdvertDao.ID} = $id",
                            null
                    )
                } else {
                    updateRow = database.update(
                            AdvertDao.TABLE_NAME,
                            values,
                            "${AdvertDao.ID} = $id and $selection",
                            selectionArgs)

                }
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

        context.contentResolver.notifyChange(uri, null)
        return updateRow.toInt()
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        val uriType = uriMatcher.match(uri)
        var deletedRow = 0
        when (uriType) {
            ADVERTS -> {
                deletedRow = database.delete(AdvertDao.TABLE_NAME, selection, selectionArgs)
            }
            ADVERT_ID -> {
                val id = uri?.lastPathSegment

                if (selection.isNullOrEmpty()) {
                    deletedRow = database.delete(
                            AdvertDao.TABLE_NAME,
                            "${AdvertDao.ID} = $id",
                            null
                    )
                } else {
                    deletedRow = database.delete(
                            AdvertDao.TABLE_NAME,
                            "${AdvertDao.ID} = $id and $selection",
                            selectionArgs);

                }
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

        context.contentResolver.notifyChange(uri, null)
        return deletedRow
    }

        override fun getType(uri: Uri?): String {
            throw NotImplementedError("Not implemented yet...")
        }
    }