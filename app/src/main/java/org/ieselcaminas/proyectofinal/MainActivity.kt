package org.ieselcaminas.proyectofinal

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.ieselcaminas.proyectofinal.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.background = null
        navView.menu.getItem(2).isEnabled = false
        navView.setupWithNavController(navController)

        val bottomAppBar = binding.bottomAppBar
        val fab = binding.fab

        bottomAppBar.isVisible = false
        bottomAppBar.isEnabled = false
        fab.isVisible = false
        fab.isEnabled = false
    }
}