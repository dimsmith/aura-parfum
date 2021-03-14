package com.github.dimsmith.auraparfum.views.address

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.github.dimsmith.auraparfum.R

class AddressActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navController = findNavController(R.id.address_nav_host)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}