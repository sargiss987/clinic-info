package com.example.clinic_info_branch.fragments.home_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.clinic_info_branch.R
import com.example.clinic_info_branch.fragments.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_daily_events.*

class DailyEventsFragment :  BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_events, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addNote.setOnClickListener {
            Toast.makeText(context, "${dailyEvents.cells}", Toast.LENGTH_SHORT).show()
        }
    }
}
