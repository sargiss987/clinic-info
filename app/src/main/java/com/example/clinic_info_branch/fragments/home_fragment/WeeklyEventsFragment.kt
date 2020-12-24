package com.example.clinic_info_branch.fragments.home_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.clinic_info_branch.R
import com.example.clinic_info_branch.fragments.BaseFragment

class WeeklyEventsFragment:  BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weekly_events, container, false)
    }
}