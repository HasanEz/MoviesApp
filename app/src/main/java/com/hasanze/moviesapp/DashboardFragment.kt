package com.hasanze.moviesapp


import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley
import com.google.zxing.integration.android.IntentIntegrator
import com.hasanze.moviesapp.classes.DbManger
import kotlinx.android.synthetic.main.fragment_dashboard.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream

class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)


        view.findViewById<Button>(R.id.btnScan).setOnClickListener {

            // Init scanner : using zxing Library to scan..

            val scanner = IntentIntegrator.forSupportFragment(this)
            scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            scanner.setBeepEnabled(false)
            scanner.initiateScan()

        }
        // Inflate the layout for this fragment
        return view
    }


    // results after clicking scan

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_LONG).show()
                } else {

                    //getting movie content -> converting result content to JsonObject
                    val json = JSONObject(result.contents)
                    Toast.makeText(
                        requireContext(),
                        json.get("title").toString(),
                        Toast.LENGTH_LONG
                    ).show()
                    // search for movie on database
                    searchMovieInDb(json)
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun searchMovieInDb(movie: JSONObject) {


        //creating instance of database manger

        val dbManger = DbManger(requireContext())

        //parameters for search query

        val projections = arrayOf("title")
        val selectionArgs = arrayOf(movie.getString("title"))

        // check if cursor have at least 1 result

        val movieExist =
            dbManger.checkMovieExist(projections, "title like ?", selectionArgs, "title")


        if (movieExist) {

            Log.e("movieExist", "movie exist")
            Toast.makeText(requireContext(), "Movie Exists in Data Base", Toast.LENGTH_SHORT).show()

        } else {
            //if movie does not exist download it..
            Log.e("movie", "movie does not exist")

            //requesting image..
            val newImageRequest =
                ImageRequest(movie.getString("image"), Response.Listener<Bitmap> { response ->

                    //Request completed successfully
                    //Getting Image and convert it to ByteArray to save it in Data Base
                    val stream = ByteArrayOutputStream()
                    response.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    val picture = stream.toByteArray()

                    // put values in content values

                    val values = ContentValues()

                    values.put("title", movie.getString("title"))
                    values.put("image", movie.getString("image"))
                    values.put("rating", movie.getDouble("rating"))
                    values.put("releaseYear", movie.getInt("releaseYear"))
                    values.put("genre", movie.getString("genre"))
                    values.put("picture", picture)

                    //saving movie in db

                    dbManger.addMovie(values)

                    Log.d("movie", "movie added")

                    Toast.makeText(requireContext(), "movie added", Toast.LENGTH_LONG).show()


                }, 0, 0, null, null, Response.ErrorListener {

                    Log.e("VolleyRequest", "ERROR DOWNLOADING PICTURE" + it.message)


                })

            Volley.newRequestQueue(requireContext()).add(newImageRequest)

        }


    }


}



