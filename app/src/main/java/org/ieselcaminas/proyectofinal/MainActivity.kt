package org.ieselcaminas.proyectofinal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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
        _auth = Firebase.auth
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (auth.currentUser != null) {
            startActivity(Intent(this,StartActivity::class.java))
        } else {
            val sharedPreferences = getSharedPreferences(getString(R.string.preferences_key),
                Context.MODE_PRIVATE)!!
            val mail = sharedPreferences.getString(resources.getString(R.string.storage_user_mail), null)
            val pass = sharedPreferences.getString(resources.getString(R.string.storage_user_pass), null)

            if (mail!= null && pass != null) {
                auth.signInWithEmailAndPassword(mail,pass)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
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
}