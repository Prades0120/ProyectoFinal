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
import org.ieselcaminas.proyectofinal.LogInActivity
import org.ieselcaminas.proyectofinal.R
import org.ieselcaminas.proyectofinal.StartActivity
import org.ieselcaminas.proyectofinal.databinding.FragmentSigninTabBinding

class SigninTab : Fragment() {
    private var _binding: FragmentSigninTabBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSigninTabBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSignIn.setOnClickListener {
            val db = Firebase.firestore
            val auth = Firebase.auth
            val pass = binding.editTextPassSign.text.toString()
            val mail = binding.editTextMailSign.text.toString()
            val name = binding.editTextName.text.toString()
            val lastName = binding.editTextLastName.text.toString()
            startActivity(Intent(this.context, StartActivity::class.java))

            val valid = if (name.isBlank() || lastName.isBlank() || mail.isBlank() || pass.isBlank())
                false
            else !(pass.length < 8 || pass != binding.editTextPassRepeat.text.toString())

            if (valid) {
                auth.createUserWithEmailAndPassword(mail, pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(ContentValues.TAG, "signInWithEmail:success")
                            val user = hashMapOf(
                                "name" to name,
                                "lastName" to lastName,
                                "image" to null,
                                "phone" to null,
                            )
                            db.collection("users").document(mail).set(user)
                            try {
                                auth.signInWithEmailAndPassword(mail,pass)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            val sharedPreferences = activity?.getSharedPreferences(getString(
                                                R.string.preferences_key),
                                                Context.MODE_PRIVATE)
                                            if (sharedPreferences!=null) {
                                                sharedPreferences.edit().putString(getString(R.string.storage_user_mail),mail).apply()
                                                sharedPreferences.edit().putString(getString(R.string.storage_user_pass),pass).apply()
                                            }
                                            val intent = Intent(context, LogInActivity::class.java)
                                            intent.putExtra("name",name)
                                            intent.putExtra("lastName",name)
                                            startActivity(intent)
                                            activity?.finish()
                                        } else {
                                            Log.w(ContentValues.TAG, "logIn failure.", it.exception)
                                        }
                                    }
                            } catch (e: Exception) {
                                Toast.makeText(context,"Restart the app to relogin.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                        }
                    }
            }

            binding.editTextName.text.clear()
            binding.editTextLastName.text.clear()
            binding.editTextMailSign.text.clear()
            binding.editTextPassSign.text.clear()
            binding.editTextPassRepeat.text.clear()
        }
    }
}