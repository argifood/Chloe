package com.chloeirrigation.chloe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.activity_main.*
//import com.squareup.picasso.Picasso
//import com.squareup.picasso.OkHttp3Downloader



class MainActivity : AppCompatActivity(), TempToolbarTitleListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(main_toolbar)

        // Set up Navigation with default ActionBar and the Navigation Controller
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

//        val picasso = Picasso.Builder(this)
//            .downloader(OkHttp3Downloader(this))
//            .build()
//        Picasso.setSingletonInstance(picasso)
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()

    override fun updateTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun expandActionBar(expand: Boolean) {
        main_appbar.setExpanded(expand)
    }
}


