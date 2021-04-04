package com.github.dimsmith.auraparfum.views.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.dimsmith.auraparfum.R
import com.github.dimsmith.auraparfum.common.makeToast
import com.github.dimsmith.auraparfum.common.toActivity
import com.github.dimsmith.auraparfum.views.auth.AuthActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var navController: NavController
    private val auth = FirebaseAuth.getInstance()

    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null) {
            toActivity(AuthActivity::class.java, bundleOf(), true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavView = findViewById(R.id.main_bottom_nav)
        navController = findNavController(R.id.main_nav_host)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.home_menu, R.id.cart_menu, R.id.account_menu
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private var backPressedCount = 0L
    override fun onBackPressed() {
        if (backPressedCount + 2000 > System.currentTimeMillis()) finish()
        else {
            this.makeToast("Press back again to exit")
            backPressedCount += System.currentTimeMillis()
        }
    }
}