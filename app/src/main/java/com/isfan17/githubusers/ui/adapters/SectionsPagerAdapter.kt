package com.isfan17.githubusers.ui.adapters

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.isfan17.githubusers.ui.detail.fragments.DescriptionFragment
import com.isfan17.githubusers.ui.detail.fragments.FollowersFragment
import com.isfan17.githubusers.ui.detail.fragments.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity, data: Bundle): FragmentStateAdapter(activity) {

    private var fragmentBundle: Bundle

    init {
        fragmentBundle = data
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position) {
            0 -> fragment = DescriptionFragment()
            1 -> fragment = FollowersFragment()
            2 -> fragment = FollowingFragment()
        }
        fragment?.arguments = this.fragmentBundle
        return fragment as Fragment
    }

    override fun getItemCount() = 3

}