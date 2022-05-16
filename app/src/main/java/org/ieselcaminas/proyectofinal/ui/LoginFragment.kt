package org.ieselcaminas.proyectofinal.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.ieselcaminas.proyectofinal.R
import org.ieselcaminas.proyectofinal.ViewPagerAdapter
import org.ieselcaminas.proyectofinal.databinding.FragmentLoginBinding
import org.ieselcaminas.proyectofinal.ui.loginfragments.LoginTab
import org.ieselcaminas.proyectofinal.ui.loginfragments.SigninTab


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var tabLayout : TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = binding.viewPager2
        tabLayout = binding.tabLayout
        val mAdapter = ViewPagerAdapter(this)
        mAdapter.addFragment(LoginTab()) //0
        mAdapter.addFragment(SigninTab()) //1

        viewPager.adapter = mAdapter

        TabLayoutMediator(tabLayout,viewPager) { tab , pos ->
            when (pos) {
                0 -> tab.text = resources.getText(R.string.login)
                1 -> tab.text = resources.getText(R.string.action_sign_in_short)
            }
        }.attach()
    }
}
