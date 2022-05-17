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
import com.google.firebase.ktx.Firebase
import org.ieselcaminas.proyectofinal.MainActivity
import org.ieselcaminas.proyectofinal.R
import org.ieselcaminas.proyectofinal.databinding.FragmentLoginTabBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginTab.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginTab : Fragment() {
    private var _binding: FragmentLoginTabBinding? = null
    private val binding get() = _binding!!
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

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
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(ContentValues.TAG, "signInWithEmail:success")
                            try {
                                val sharedPreferences = activity?.getSharedPreferences(getString(R.string.preferences_key),
                                    Context.MODE_PRIVATE)!!
                                sharedPreferences.edit().putString(getString(R.string.storage_user_mail),mail).apply()
                                sharedPreferences.edit().putString(getString(R.string.storage_user_pass),pass).apply()
                                startActivity(Intent(context, MainActivity::class.java))
                            } catch (e: Exception) {
                                Toast.makeText(context,"Restart the app to relogin.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(ContentValues.TAG, "logInWithEmail:failure", it.exception)
                            Toast.makeText(context,"logInWithEmail:failure", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    companion object {

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginTab.
         */

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginTab().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}