package com.hasanze.moviesapp.classes

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.util.Log

class DbManger(context: Context){

    //Database Name + Table Name
    val dbName = "MoviesAppDb"
    val dbTable = "MoviesTable"

    //Table columns
    val colID = "ID"
    val colTitle = "title"
    val colImage = "image"
    val colRating = "rating"
    val colReleaseYear = "releaseYear"
    val colGenre = "genre"
    val colPicture = "picture"

    //DataBase Version
    val dbVersion = 1

    //SQL Statement
    val sqlCreateTable =
        "CREATE TABLE IF NOT EXISTS $dbTable" +
                "($colID INTEGER PRIMARY KEY,$colTitle TEXT,$colImage TEXT,$colRating DOUBLE,$colReleaseYear INTEGER,$colGenre TEXT,$colPicture BLOB)"

    //Database instance
    var sqlDb:SQLiteDatabase? = null

    //Initialize DataBase
    init {
        val dbHelper = DatabaseHelper(context)
        sqlDb = dbHelper.writableDatabase

    }



    inner class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, dbName, null, dbVersion) {

        override fun onCreate(p0: SQLiteDatabase?) {
            p0!!.execSQL(sqlCreateTable)
            Log.d("DataBase", "DataBase Created")
        }

        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
            p0!!.execSQL("DROP TABLE IF EXISTS $dbName")

        }


    }



    fun addMovie(values: ContentValues): Long {

        return sqlDb!!.insert(dbTable,"",values)
    }

    fun queryBuilder(projection:Array<String>,selection:String,selectionArgs:Array<String>,sortOrder:String):Cursor{


        val queryBuilder = SQLiteQueryBuilder()
        queryBuilder.tables = dbTable

        val cursor = queryBuilder.query(sqlDb,projection,selection,selectionArgs,null,null,sortOrder)
        return cursor
    }

}

