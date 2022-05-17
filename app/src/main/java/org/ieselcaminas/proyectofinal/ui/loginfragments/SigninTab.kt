package org.ieselcaminas.proyectofinal.ui.loginfragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import org.ieselcaminas.proyectofinal.AuthClass
import org.ieselcaminas.proyectofinal.StartActivity
import org.ieselcaminas.proyectofinal.databinding.FragmentSigninTabBinding
import org.ieselcaminas.proyectofinal.ui.Home

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SigninTab : Fragment() {
    private var navigator: NavController? = null
    private var _binding: FragmentSigninTabBinding? = null
    private val binding get() = _binding!!
    private var _auth: FirebaseAuth? = null
    private val auth get() = _auth!!
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
        _binding = FragmentSigninTabBinding.inflate(layoutInflater)
        _auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSignIn.setOnClickListener {
            val pass = binding.editTextPassSign.text.toString()
            val mail = binding.editTextMailSign.text.toString()
            val name = binding.editTextName.text.toString()
            val lastName = binding.editTextLastName.text.toString()

            val valid = if (name.isBlank() || lastName.isBlank() || mail.isBlank() || pass.isBlank())
                false
            else !(pass.length < 8 || pass != binding.editTextPassRepeat.text.toString())

            val login = AuthClass.signIn(mail,pass)
            if (valid) {
                if (login==null) {
                    Toast.makeText(context, "Authentication error.",
                        Toast.LENGTH_SHORT).show()
                } else if (login) {
                    Toast.makeText(context, "Authentication success.",
                        Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this.context, StartActivity::class.java))
                } else {
                    Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }

            binding.editTextName.text.clear()
            binding.editTextLastName.text.clear()
            binding.editTextMailSign.text.clear()
            binding.editTextPassSign.text.clear()
            binding.editTextPassRepeat.text.clear()
        }
    }

    fun addNavigator(navigation: NavController) {
        this.navigator = navigation
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Home.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}