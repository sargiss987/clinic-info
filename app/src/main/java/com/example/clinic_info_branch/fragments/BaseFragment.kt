package com.example.clinic_info_branch.fragments

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.clinic_info_branch.data_base.ClinicInfo
import com.example.clinic_info_branch.models.Notes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

open class BaseFragment : Fragment() {

    protected lateinit var db: ClinicInfo


    override fun onAttach(context: Context) {
        super.onAttach(context)
        db = ClinicInfo.getDatabase(context)

    }


}