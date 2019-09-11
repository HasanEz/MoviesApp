package com.hasanze.moviesapp

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.hasanze.moviesapp.classes.DbManger
import com.hasanze.moviesapp.classes.Movie
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {

    //API URL

    val apiURL = "https://api.androidhive.info/json/movies.json"
    lateinit var moviesRequestResponse: JSONArray


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //checking if DataBase Exists


        if (checkDataBase()) {
            //If data Base Exists no need to download Data and move to next screen
            startActivity(Intent(this, MoviesActivity::class.java))
            finish()
            Log.d("DataBase", "DataBase Exist")
        } else {

            //If DataBase Does not Exist start downloading Movies using Volley Library

            val getMoviesRequest =
                object : JsonArrayRequest(Method.GET, apiURL, null, Response.Listener { response ->

                    Log.d("VolleyRequest", "SUCCESS")
                    moviesRequestResponse = response

                    // After getting response start process of saving to data base
                    saveToDb()

                },
                    Response.ErrorListener {

                        Log.d("VolleyRequest", "ERROR")

                    }) {
                    override fun getBodyContentType(): String {
                        return "application/json; charset=utf-8"
                    }

                }
            //Sending Request
            Volley.newRequestQueue(this).add(getMoviesRequest)
        }


    }

    private fun saveToDb() {

        val dbManger = DbManger(this)

        //iteration over request response which is of type json array


        for (i in 0 until moviesRequestResponse.length()) {

            var values = ContentValues()
            var movie = moviesRequestResponse.getJSONObject(i)


            //GET IMAGE BITMAP
            val imageRequest =
                ImageRequest(movie.getString("image"), Response.Listener<Bitmap> { response ->

                    //Getting Image and convert it to ByteArray
                    val stream = ByteArrayOutputStream()
                    response.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    val picture = stream.toByteArray()


                    //Putting Values in content Values

                    values.put("title", movie.getString("title"))
                    values.put("image", movie.getString("image"))
                    values.put("rating", movie.getDouble("rating"))
                    values.put("releaseYear", movie.getInt("releaseYear"))
                    values.put("genre", movie.getString("genre"))
                    values.put("picture", picture)


                    dbManger.addMovie(values)


                    if (i + 1 == moviesRequestResponse.length()) {
                        //End of download
                        Toast.makeText(this, "Movies Saved", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MoviesActivity::class.java))
                        finish()

                    }


                }, 0, 0, null, null,
                    Response.ErrorListener {

                        Log.d("VolleyRequest", "ERROR DOWNLOADING PICTURE")

                    })

            Volley.newRequestQueue(this).add(imageRequest)

        }

    }

    private fun checkDataBase(): Boolean {
        var checkDB: SQLiteDatabase? = null
        try {
            checkDB = SQLiteDatabase.openDatabase(
                "/data/data/com.hasanze.moviesapp/databases/MoviesAppDb", null,
                SQLiteDatabase.OPEN_READONLY
            )
            checkDB!!.close()
        } catch (e: SQLiteException) {
            // database doesn't exist yet.

        }
        checkDB?.close()
        return checkDB != null
    }


}
