package org.ieselcaminas.proyectofinal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.logOutButton.setOnClickListener {

            FirebaseAuth.getInstance().signOut()
        }
        super.onViewCreated(view, savedInstanceState)
    }

}