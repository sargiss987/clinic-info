package com.example.clinic_info_branch.fragments

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.clinic_info_branch.data_base.ClinicInfo

open class BaseFragment : Fragment() {

    protected lateinit var db: ClinicInfo

    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = ClinicInfo.getDatabase(context)

    }
}