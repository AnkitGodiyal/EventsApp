package com.ankit.insiderapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ankit.insiderapp.fragments.AllEventsHomeFragment
import com.ankit.insiderapp.fragments.EventListingFragment
import com.ankit.insiderapp.helpers.Constants


class EventPagerAdapter(fa: FragmentActivity, private val groups: ArrayList<String>) :
    FragmentStateAdapter(fa) {

    override fun createFragment(position: Int): Fragment {
        if (Constants.ALL_EVENTS == groups[position])
            return AllEventsHomeFragment.newInstance(groups[position])
        return EventListingFragment.newInstance(groups[position])
    }

    override fun getItemCount(): Int {
        return groups.size
    }
}