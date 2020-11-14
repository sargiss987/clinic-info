package com.example.clinic_info_branch.fragments.register_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.clinic_info_branch.R
import com.example.clinic_info_branch.fragments.searching_fragment.PATIENT_INFO
import com.example.clinic_info_branch.fragments.searching_fragment.PATIENT_PERSONAL_PAGE
import com.example.clinic_info_branch.fragments.searching_fragment.PatientPersonalPage
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_teeth_diagram.*

const val STATE_OF_TOOTH = "state_of_tooth"

class TeethDiagramFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_teeth_diagram, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnCommit.setOnClickListener {
            val bundle = Bundle()
            val gson = Gson()
            val message = gson.toJson(stateOfTeethList)
            bundle.putString(STATE_OF_TOOTH,message)

            fragmentManager?.beginTransaction()?.apply {

                replace(R.id.fragmentContainer, RegisterFragment().apply { arguments = bundle})
                commit()
            }
        }
    }
}