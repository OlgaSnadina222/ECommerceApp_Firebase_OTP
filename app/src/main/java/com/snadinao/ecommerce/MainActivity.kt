package com.snadinao.ecommerce

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.snadinao.ecommerce.databinding.ActivityMainBinding
import com.snadinao.ecommerce.databinding.ActivityWelcomeBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sendingBack()
    }

    private fun sendingBack() {
        if (supportActionBar != null){
            navController = Navigation.findNavController(this, R.id.fragment)
            NavigationUI.setupActionBarWithNavController(this, navController)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }
}