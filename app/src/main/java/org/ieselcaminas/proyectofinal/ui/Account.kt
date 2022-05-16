package org.ieselcaminas.proyectofinal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.button1.setOnClickListener {
            val bottomAppBar = requireActivity().findViewById<BottomAppBar>(R.id.bottomAppBar)
            val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)

            bottomAppBar.isVisible = false
            bottomAppBar.isEnabled = false
            fab.isVisible = false
            fab.isEnabled = false

            Navigation.findNavController(view).navigate(R.id.action_navigation_account_to_loginFragment)
        }
        super.onViewCreated(view, savedInstanceState)
    }

}