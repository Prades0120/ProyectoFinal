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
import org.ieselcaminas.proyectofinal.model.recyclerView.Item
import java.io.Serializable

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
        Thread.sleep(100)
        checkUser()
        val sharedPreferences = getSharedPreferences(getString(R.string.preferences_key),
            MODE_PRIVATE)
        sharedPreferences.edit().putString("deletedFile",null).apply()
        sharedPreferences.edit().putString("newText",null).apply()
        sharedPreferences.edit().putString("newDate",null).apply()
    }

    private fun checkUser() {
        if (user!=null) {
            mail = user!!.email.toString()
            val doc = db.collection("users").document(mail!!)
            doc.get().addOnCompleteListener {
                if (it.isSuccessful) {
                    name = it.result.get("name") as String?
                    lastName = it.result.get("lastName") as String?
                    addDocs()
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
                            addDocs()
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

    private fun startNewMainMenu(arrayList: ArrayList<Item>) {
        val intent = Intent(this, StartActivity::class.java)
        intent.putExtra("name",name)
        intent.putExtra("lastName",lastName)
        intent.putExtra("array",arrayList as Serializable)
        startActivity(intent)
    }

    private fun startNewLogin() {
        startActivity(Intent(this, LogInActivity::class.java))
    }

    private fun addDocs(){
        val array = ArrayList<Item>(0)

        user?.email?.let { mail ->
            db.collection("docs").document(mail).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val data = task.result.data.orEmpty()
                        if (data.isNotEmpty()) {
                            val list = data as HashMap<String, String>
                            for (i in list.keys) {
                                array.add(Item(list[i].toString(),i))
                            }
                            array.sortByDescending { it.date.toLong() }
                        }
                        startNewMainMenu(array)
                    }
                }
        }
    }
}