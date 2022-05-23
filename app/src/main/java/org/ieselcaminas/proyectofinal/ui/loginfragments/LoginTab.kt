package org.ieselcaminas.proyectofinal.ui.loginfragments

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.ieselcaminas.proyectofinal.R
import org.ieselcaminas.proyectofinal.databinding.FragmentLoginTabBinding
import org.ieselcaminas.proyectofinal.ui.StartActivity

class LoginTab : Fragment() {
    private var _binding: FragmentLoginTabBinding? = null
    private val binding get() = _binding!!

    private var name: String? = null
    private var lastName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginTabBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.buttonLogin.setOnClickListener {
            val db = Firebase.firestore
            val auth = Firebase.auth
            val mail = binding.editTextMailLogin.text.toString()
            val pass = binding.editTextPassLogin.text.toString()

            if (mail.isBlank() || pass.isBlank()) {
                Toast.makeText(
                    context, resources.getText(R.string.login_error),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                auth.signInWithEmailAndPassword(mail, pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(ContentValues.TAG, "signInWithEmail:success")
                            try {
                                val sharedPreferences = activity?.getSharedPreferences(getString(R.string.preferences_key),
                                    Context.MODE_PRIVATE)
                                if (sharedPreferences!=null) {
                                    sharedPreferences.edit().putString(getString(R.string.storage_user_mail),mail).apply()
                                    sharedPreferences.edit().putString(getString(R.string.storage_user_pass),pass).apply()
                                }
                                db.collection("users").document(mail).get()
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            name = it.result.get("name").toString()
                                            lastName = it.result.get("lastName").toString()
                                            startNewMainMenu()
                                        } else {
                                            name = "Gest"
                                            lastName = ""
                                            startNewMainMenu()
                                        }
                                    }
                            } catch (e: Exception) {
                                Toast.makeText(context,"Restart the app to relogin.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(ContentValues.TAG, "logInWithEmail:failure", task.exception)
                            Toast.makeText(context,"logInWithEmail:failure", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun startNewMainMenu() {
        val intent = Intent(context, StartActivity::class.java)
        intent.putExtra("name",name)
        intent.putExtra("lastName",lastName)
        startActivity(intent)
    }
}