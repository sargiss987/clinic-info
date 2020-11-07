package com.example.clinic_info_branch.fragments.searching_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.clinic_info_branch.R
import com.example.clinic_info_branch.data_base.Patient
import kotlinx.android.synthetic.main.fragment_patient_personal_page.view.*


class PatientPersonalPage : Fragment() {

    var patient : Patient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view =  inflater.inflate(R.layout.fragment_patient_personal_page, container, false)
        patient = arguments?.getParcelable("message")

        val healthInfo = "${patient?.healthInfo?.allergy} ${patient?.healthInfo?.allergicManifestation} ${patient?.healthInfo?.bleeding}"

        view.txtFullName.text = patient?.patientName
        view.txtDate.text = patient?.patientDate
        view.txtGender.text = patient?.gender
        view.txtPlace.text = patient?.placeOfResidence
        view.txtPhone.text = patient?.phone
        view.txtHealthInfo.text = healthInfo

        return view

    }



}