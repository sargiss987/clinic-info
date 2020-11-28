package com.example.clinic_info_branch.fragments.searching_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.clinic_info_branch.R
import com.example.clinic_info_branch.data_base.TreatmentProcess
import kotlinx.android.synthetic.main.fragment_patient_treatment_process.view.*


class PatientTreatmentProcessFragment : Fragment() {

    private var treatmentProcess: TreatmentProcess? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_patient_treatment_process, container, false)

                treatmentProcess = arguments?.getParcelable(TREATMENT_PROCESS_INFO)

                view.txtDate.text = treatmentProcess?.visitDate
                view.subDescription.text = treatmentProcess?.subReasonVisit
                view.objDescription.text = treatmentProcess?.objReasonVisit
                view.diagnosis.text = treatmentProcess?.diagnosis
                view.treatmentPlan.text = treatmentProcess?.treatmentPlan

        return view
    }
}