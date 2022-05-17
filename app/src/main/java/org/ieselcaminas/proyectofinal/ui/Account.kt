package org.ieselcaminas.proyectofinal.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import org.ieselcaminas.proyectofinal.MainActivity
import org.ieselcaminas.proyectofinal.R
import org.ieselcaminas.proyectofinal.databinding.FragmentAccountBinding

class Account : Fragment() {
    private lateinit var name: String
    private lateinit var lastName: String
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            name = it.getString("name").toString()
            lastName = it.getString("lastName").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = Firebase.auth.currentUser
        if (user != null) {
            binding.textView.text = name+lastName
            binding.textView2.text = user.email.toString()
        }

        binding.logOutButton.setOnClickListener {
            FirebaseAuth.getInstance(Firebase.app).signOut()
            try {
                val sharedPreferences = activity?.getSharedPreferences(getString(R.string.preferences_key),
                    Context.MODE_PRIVATE)!!
                sharedPreferences.edit().putString(getString(R.string.storage_user_mail),null).apply()
                sharedPreferences.edit().putString(getString(R.string.storage_user_pass),null).apply()
                activity?.finish()
                startActivity(Intent(context, MainActivity::class.java))
            } catch (e: Exception) {
                Toast.makeText(context,"Restart the app to relogin.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param name Parameter 1.
         * @param lastName Parameter 2.
         * @return A new instance of fragment Statics.
         */

        @JvmStatic
        fun newInstance(name: String, lastName: String) =
            Statics().apply {
                arguments = Bundle().apply {
                    putString("name", name)
                    putString("lastName", lastName)
                }
            }
    }
}