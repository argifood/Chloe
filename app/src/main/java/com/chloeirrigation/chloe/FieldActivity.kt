package com.chloeirrigation.chloe

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.chloeirrigation.chloe.Objects.Field
import kotlinx.android.synthetic.main.activity_field.*

class FieldActivity : AppCompatActivity(), TempToolbarTitleListener {
    lateinit var field: Field

//    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
//        when (item.itemId) {
//            R.id.navigation_home -> {
////                message.setText(R.string.title_home)
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.navigation_dashboard -> {
////                message.setText(R.string.title_dashboard)
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.navigation_notifications -> {
////                message.setText(R.string.title_notifications)
//                return@OnNavigationItemSelectedListener true
//            }
//        }
//        false
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_field)

        setSupportActionBar(field_toolbar)

        // Set up Navigation with default ActionBar and the Navigation Controller
        val navController = findNavController(R.id.nav_host_fragment_field)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottomNavigation.setupWithNavController(findNavController(R.id.nav_host_fragment_field))

        field = intent.getParcelableExtra<Field>("field")
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment_field).navigateUp()

    override fun updateTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun expandActionBar(expand: Boolean) {}
}
