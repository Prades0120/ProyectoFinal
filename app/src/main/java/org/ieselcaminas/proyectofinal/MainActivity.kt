package org.ieselcaminas.proyectofinal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.ieselcaminas.proyectofinal.databinding.ActivityMainBinding
import org.ieselcaminas.proyectofinal.ui.loginfragments.LoginTab
import org.ieselcaminas.proyectofinal.ui.loginfragments.SigninTab

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private var user = Firebase.auth.currentUser

    private var name: String? = null
    private var lastName: String? = null
    private var mail: String? = null
    private var pass: String? =null

    private lateinit var tabLayout : TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkUser()

        Thread.sleep(1000)
        if (user != null) {
            startNewMenu()
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

    private fun checkUser() {
        if (user!=null) {
            mail = user!!.email.toString()
            val doc = db.collection("users").document(mail!!)
            doc.get().addOnCompleteListener {
                if (it.isSuccessful) {
                    name = it.result.get("name") as String?
                    lastName = it.result.get("lastName") as String?
                }else{
                    name = "unknown"
                    lastName = "unknown"
                }
            }
        } else {
            val sharedPreferences = getSharedPreferences(
                getString(R.string.preferences_key), Context.MODE_PRIVATE)
            mail = sharedPreferences.getString(resources.getString(R.string.storage_user_mail), null)
            pass = sharedPreferences.getString(resources.getString(R.string.storage_user_pass), null)

            if (mail!=null && pass!=null) {
                val signin = auth.signInWithEmailAndPassword(mail!!, pass!!)
                signin.addOnSuccessListener { _ ->
                    val  doc = db.collection("users").document(mail!!)
                    doc.get().addOnCompleteListener {
                        if (it.isSuccessful) {
                            name = it.result.get("name") as String?
                            lastName = it.result.get("lastName") as String?
                        }else{
                            name = "unknown"
                            lastName = "unknown"
                        }
                    }
                }
                user = auth.currentUser
            }
        }
    }

    private fun startNewMenu() {
        val intent = Intent(this, StartActivity::class.java)
        intent.putExtra("name",name)
        intent.putExtra("lastName",lastName)
        startActivity(intent)
    }

}