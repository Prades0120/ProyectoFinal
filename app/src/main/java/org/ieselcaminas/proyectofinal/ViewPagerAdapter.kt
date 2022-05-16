package org.ieselcaminas.proyectofinal

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val arrayListFragments = ArrayList<Fragment>()

    override fun getItemCount(): Int {
        return  arrayListFragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return arrayListFragments[position]
    }

    fun addFragment(fragment: Fragment) { arrayListFragments.add(fragment) }
}