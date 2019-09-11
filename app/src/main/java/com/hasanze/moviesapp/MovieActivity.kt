package com.hasanze.moviesapp

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_movie.*

class MovieActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        //getting data from intent

        val title = intent.getStringExtra("title")
        val releaseYear = intent.getIntExtra("releaseYear",0)
        val genre = intent.getStringExtra("genre")
        val rating = intent.getDoubleExtra("rating",0.0)
        val picture = intent.getByteArrayExtra("picture")

        // assigning values to views

        movieActivityTitle.text = title
        movieActivityYear.text = releaseYear.toString()
        movieActivityGenre.text = genre.replace("[","").replace("]","").replace("\"","")
        movieActivityRating.text = rating.toString()

        //converting byte array to bitmap
        val bmap = BitmapFactory.decodeByteArray(picture,0,picture.size)

        //assign image

        movieActivityImage.setImageBitmap(bmap)





    }
}
