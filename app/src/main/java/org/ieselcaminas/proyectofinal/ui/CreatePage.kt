package org.ieselcaminas.proyectofinal.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.ieselcaminas.proyectofinal.databinding.ActivityCreatePageBinding

class CreatePage : AppCompatActivity() {

    private var _binding: ActivityCreatePageBinding? = null
    private val binding get() = _binding!!

    private val db = Firebase.firestore
    private var _user: FirebaseUser? = null
    private val user get() = _user!!

    override fun onCreate(savedInstanceState: Bundle?) {
        _user = Firebase.auth.currentUser
        _binding = ActivityCreatePageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val text = intent.getStringExtra("text")

        if (text!=null){
            binding.editTextTextMultiLine.text.append(text)
        }

        binding.floatingActionButton.setOnClickListener {
            binding.floatingActionButton.extend()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}