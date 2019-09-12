package com.hasanze.moviesapp.adapters

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hasanze.moviesapp.MovieActivity
import com.hasanze.moviesapp.R
import com.hasanze.moviesapp.classes.Movie

class MoviesAdapter (private val context:Context , private val movies : List<Movie>) :
        RecyclerView.Adapter<MoviesAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        //Inflating Layout
        val view =  LayoutInflater.from(context).inflate(R.layout.item_movie,parent,false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        //Item count..
        return movies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(context,movies[position])
    }


    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        //pointers to movie card layout view

        private val movieImage :ImageView = itemView.findViewById(R.id.movieCardImage)
        private val movieName : TextView = itemView.findViewById(R.id.movieCardTitle)
        private val movieYear : TextView = itemView.findViewById(R.id.movieCardYear)
        private val movieGenre : TextView = itemView.findViewById(R.id.movieCardGenre)
        private val movieRating : TextView = itemView.findViewById(R.id.movieCardRating)

        // Binding Data

        fun bindData(context: Context, movie : Movie){

            movieName.text = movie.title
            movieYear.text = movie.releaseYear.toString()
            movieGenre.text = movie.genre.replace("[","").replace("]","").replace("\"","")
            movieRating.text = movie.rating.toString()

            //converting byte array to bitmap

            val bmap = BitmapFactory.decodeByteArray(movie.picture,0,movie.picture.size)


            movieImage.setImageBitmap(bmap)


            itemView.setOnClickListener {

                //Clicking an item on the list will open up new activity

                val intent = Intent(context,MovieActivity::class.java)
                intent.putExtra("title",movie.title)
                intent.putExtra("releaseYear",movie.releaseYear)
                intent.putExtra("genre",movie.genre)
                intent.putExtra("rating",movie.rating)
                intent.putExtra("picture",movie.picture)

                context.startActivity(intent)

            }
        }
    }



}