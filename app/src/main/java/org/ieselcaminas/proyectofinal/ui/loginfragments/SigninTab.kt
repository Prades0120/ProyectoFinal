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
import org.ieselcaminas.proyectofinal.databinding.FragmentSigninTabBinding
import org.ieselcaminas.proyectofinal.ui.LoadingDialog
import org.ieselcaminas.proyectofinal.ui.StartActivity

class SigninTab : Fragment() {
    private var _binding: FragmentSigninTabBinding? = null
    private val binding get() = _binding!!

    private var name: String? = null
    private var lastName: String? = null

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
            val loading = activity?.let { LoadingDialog(it) }!!
            loading.startLoading()
            val db = Firebase.firestore
            val auth = Firebase.auth
            val pass = binding.editTextPassSign.text.toString()
            val mail = binding.editTextMailSign.text.toString()
            val name = binding.editTextName.text.toString()
            val lastName = binding.editTextLastName.text.toString()

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
                            try {
                            db.collection("users").document(mail).set(user).
                                addOnSuccessListener {
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
                                                this.name = name
                                                this.lastName = lastName
                                                loading.dismissDialog()
                                                startNewMainMenu()
                                            } else {
                                                loading.dismissDialog()
                                                Log.w(ContentValues.TAG, "logIn failure.", it.exception)
                                            }
                                    }
                                }
                            } catch (e: Exception) {
                                loading.dismissDialog()
                                Toast.makeText(context,"Restart the app to relogin.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            loading.dismissDialog()
                            Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                        }
                    }
            } else {
                loading.dismissDialog()
                Toast.makeText(context,"You need to fill in all the fields",Toast.LENGTH_SHORT).show()
            }

            binding.editTextName.text.clear()
            binding.editTextLastName.text.clear()
            binding.editTextMailSign.text.clear()
            binding.editTextPassSign.text.clear()
            binding.editTextPassRepeat.text.clear()
        }
    }

    private fun startNewMainMenu() {
        val intent = Intent(context, StartActivity::class.java)
        intent.putExtra("name",name)
        intent.putExtra("lastName",lastName)
        startActivity(intent)
    }
}