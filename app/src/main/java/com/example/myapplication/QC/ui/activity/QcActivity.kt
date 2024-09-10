package com.example.myapplication.QC.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityQcBinding

class QcActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQcBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQcBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the navigation host
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)

        // Set up the toolbar if you have one
        // val toolbar: Toolbar = findViewById(R.id.toolbar)
        // setSupportActionBar(toolbar)

        // Set up navigation UI (optional)
        // NavigationUI.setupActionBarWithNavController(this, navController)
    }

    // Optional: Handle the back button press
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
