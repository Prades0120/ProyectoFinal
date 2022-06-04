package org.ieselcaminas.proyectofinal.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.ieselcaminas.proyectofinal.R

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private var user = Firebase.auth.currentUser

    private var name: String? = null
    private var lastName: String? = null
    private var mail: String? = null
    private var pass: String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        checkUser()
    }

    private fun checkUser() {
        if (user!=null) {
            mail = user!!.email.toString()
            val doc = db.collection("users").document(mail!!)
            doc.get().addOnCompleteListener {
                if (it.isSuccessful) {
                    name = it.result.get("name") as String?
                    lastName = it.result.get("lastName") as String?
                    startNewMainMenu()
                }else{
                    startNewLogin()
                }
            }
        } else {
            val sharedPreferences = getSharedPreferences(
                getString(R.string.preferences_key), Context.MODE_PRIVATE)
            mail = sharedPreferences.getString(getString(R.string.storage_user_mail), null)
            pass = sharedPreferences.getString(getString(R.string.storage_user_pass), null)

            if (mail!=null && pass!=null) {
                auth.signInWithEmailAndPassword(mail!!, pass!!).addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val doc = db.collection("users").document(mail!!)
                        doc.get().addOnSuccessListener {
                            name = it.get("name") as String?
                            lastName = it.get("lastName") as String?
                            startNewMainMenu()
                        }

                    } else {
                        startNewLogin()
                    }
                }
                user = auth.currentUser
            } else {
                startNewLogin()
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        finish()
    }

    private fun startNewMainMenu() {
        val intent = Intent(this, StartActivity::class.java)
        intent.putExtra("name",name)
        intent.putExtra("lastName",lastName)
        startActivity(intent)
    }

    private fun startNewLogin() {
        startActivity(Intent(this, LogInActivity::class.java))
    }
}