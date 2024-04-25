package com.dicoding.myprojectandroidfundamental.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.myprojectandroidfundamental.fragment.TabLayoutUserFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var username: String = ""

    override fun createFragment(position: Int): Fragment {
        val fragment = TabLayoutUserFragment()
        fragment.arguments = Bundle().apply {
            putInt(TabLayoutUserFragment.ARG_POSITION, position + 1)
            putString(TabLayoutUserFragment.ARG_USERNAME, username)
        }
        return fragment
    }

    override fun getItemCount(): Int {
        return 2
    }

}