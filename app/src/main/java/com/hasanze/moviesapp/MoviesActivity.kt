package com.hasanze.moviesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_movies.*

class MoviesActivity : AppCompatActivity() {


    //for fragment selection
    private lateinit var selectedFrag: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)

        //selecting home fragment as start ...
        supportFragmentManager.beginTransaction().replace(R.id.fragContainer, HomeFragment())
            .commit()

        //BOTTOM NAVIGATION

        bottomNavigationView.setOnNavigationItemSelectedListener {

            // no comments needed the code is self explanatory
            when (it.itemId) {

                R.id.navigation_home -> {
                    selectedFrag = HomeFragment()
                }

                R.id.navigation_dashboard -> {
                    selectedFrag = DashboardFragment()
                }

                R.id.navigation_notifications -> {
                    selectedFrag = NotificationFragment()
                }
            }
            supportFragmentManager.beginTransaction().replace(R.id.fragContainer, selectedFrag)
                .commit()
            true
        }

    }


}
