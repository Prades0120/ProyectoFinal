package org.ieselcaminas.proyectofinal.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.ieselcaminas.proyectofinal.R
import org.ieselcaminas.proyectofinal.databinding.ActivityLoginBinding
import org.ieselcaminas.proyectofinal.model.viewPager.ViewPagerAdapter
import org.ieselcaminas.proyectofinal.ui.loginfragments.LoginTab
import org.ieselcaminas.proyectofinal.ui.loginfragments.SigninTab

class LogInActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var tabLayout : TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewPager = binding.viewPager2
        tabLayout = binding.tabLayout
        val mAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        mAdapter.addFragment(LoginTab()) //0
        mAdapter.addFragment(SigninTab()) //1
        viewPager.adapter = mAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
            when (pos) {
                0 -> tab.text = resources.getText(R.string.login)
                1 -> tab.text = resources.getText(R.string.action_sign_in_short)
            }
        }.attach()
    }

}