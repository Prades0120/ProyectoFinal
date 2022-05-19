package org.ieselcaminas.proyectofinal.model.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager,lifecycle) {

    private val arrayListFragments = ArrayList<Fragment>()

    override fun getItemCount(): Int {
        return  arrayListFragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return arrayListFragments[position]
    }

    fun addFragment(fragment: Fragment) { arrayListFragments.add(fragment) }
}