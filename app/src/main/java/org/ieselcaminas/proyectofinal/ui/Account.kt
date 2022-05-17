package org.ieselcaminas.proyectofinal.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import org.ieselcaminas.proyectofinal.MainActivity
import org.ieselcaminas.proyectofinal.R
import org.ieselcaminas.proyectofinal.databinding.FragmentAccountBinding

class Account : Fragment() {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n", "CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = Firebase.firestore
        val auth = Firebase.auth
        if (auth.currentUser != null) {
            val mail = auth.currentUser!!.email.toString()
            db.collection("users").document(mail).get()
                .addOnSuccessListener {
                    val name = it.get("name")
                    val lastName = it.get("lastName")
                    if (name != null && lastName!=null) {
                        binding.textView.text = "$name $lastName"
                        binding.textView2.text = mail
                    }
                }

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

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return super.onCreateAnimation(androidx.transition.R.anim.abc_grow_fade_in_from_bottom, enter, nextAnim)
    }
}