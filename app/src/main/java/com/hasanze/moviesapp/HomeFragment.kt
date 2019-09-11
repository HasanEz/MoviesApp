package com.hasanze.moviesapp


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hasanze.moviesapp.adapters.MoviesAdapter
import com.hasanze.moviesapp.classes.DbManger
import com.hasanze.moviesapp.classes.Movie
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    var listOfMovies = mutableListOf<Movie>()
    lateinit var myView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        loadMovies("%", view)
        myView= view
        return view


    }



    private fun loadMovies(title: String, view: View) {

        val dbManger = DbManger(requireContext())

        val projections = arrayOf("ID", "title", "rating", "releaseYear", "genre", "picture")
        val selectionArgs = arrayOf(title)
        val cursor = dbManger.queryBuilder(projections, "title like ?", selectionArgs, "title")


        if (cursor.moveToFirst()) {

            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val title = cursor.getString(cursor.getColumnIndex("title"))
                val rating = cursor.getDouble(cursor.getColumnIndex("rating"))
                val releaseYear = cursor.getInt(cursor.getColumnIndex("releaseYear"))
                val genre = cursor.getString(cursor.getColumnIndex("genre"))
                val picture = cursor.getBlob(cursor.getColumnIndex("picture"))

                listOfMovies.add(Movie(title, "", rating, releaseYear, genre, picture))

//                Log.d("cursor", "$ID $title $rating $releaseYear $genre $picture")
            } while (cursor.moveToNext())
        }

        //for sorting List

        val comperator = compareBy<Movie>{it.releaseYear}


        //set adapter

        val moviesAdapter = MoviesAdapter(requireContext(), listOfMovies.sortedWith(comperator).reversed())
        val homeList = view.findViewById(R.id.homeList) as RecyclerView
        homeList.adapter = moviesAdapter
        val layoutManger = LinearLayoutManager(requireContext())
        homeList.layoutManager = layoutManger as RecyclerView.LayoutManager?


    }


}
