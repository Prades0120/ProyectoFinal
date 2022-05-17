package org.ieselcaminas.proyectofinal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import org.ieselcaminas.proyectofinal.databinding.ActivityMainBinding
import org.ieselcaminas.proyectofinal.ui.loginfragments.LoginTab
import org.ieselcaminas.proyectofinal.ui.loginfragments.SigninTab

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var _auth: FirebaseAuth? = null
    private val auth get () = _auth!!

    private lateinit var tabLayout : TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        _auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            startActivity(Intent(this,StartActivity::class.java))
        } else {

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
}