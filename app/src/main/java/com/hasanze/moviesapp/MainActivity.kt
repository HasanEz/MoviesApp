package com.hasanze.moviesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.hasanze.moviesapp.classes.Movie
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //API URL

    val apiURL = "https://api.androidhive.info/json/movies.json"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val getMoviesRequest = object:JsonArrayRequest(Method.GET,apiURL,null,Response.Listener { response ->

            Log.d("VolleyRequest","SUCCESS")

            for (i in 0..response.length() -1){

               // Log.d("Movie", response.getJSONObject(i).getString("title"))

            }
        },
            Response.ErrorListener {

            Log.d("VolleyRequest","ERROR")

        })
        {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

        }

        Volley.newRequestQueue(this).add(getMoviesRequest)
    }
}
