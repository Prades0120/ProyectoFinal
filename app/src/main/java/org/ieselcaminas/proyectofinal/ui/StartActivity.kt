package org.ieselcaminas.proyectofinal.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
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
        navController.setGraph(R.navigation.mobile_navigation,intent.extras)
        navView.background = null
        navView.menu.getItem(2).isEnabled = false
        navView.setupWithNavController(navController)

        val i = Intent(this,CreatePage::class.java)
        i.putExtra("array",this.intent.getSerializableExtra("array"))
        val getResult =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val value = it.data?.getSerializableExtra("array")
                    this.intent.putExtra("array",value)
                }
            }

        binding.fab.setOnClickListener {
            getResult.launch(i)
        }
    }
}