package org.ieselcaminas.proyectofinal.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.ieselcaminas.proyectofinal.R
import org.ieselcaminas.proyectofinal.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    private var _binding: ActivityStartBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navController.setGraph(R.navigation.mobile_navigation)
        navView.background = null
        navView.menu.getItem(2).isEnabled = false
        navView.setupWithNavController(navController)



        binding.fab.setOnClickListener {
            val i = Intent(this, CreatePage::class.java)
            startActivity(i)
        }
    }
}