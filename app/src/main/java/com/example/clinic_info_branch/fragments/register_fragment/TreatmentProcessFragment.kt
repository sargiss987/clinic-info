package com.example.clinic_info_branch.fragments.register_fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.clinic_info_branch.R
import com.example.clinic_info_branch.data_base.TreatmentProcess
import com.example.clinic_info_branch.fragments.BaseFragment
import com.example.clinic_info_branch.fragments.searching_fragment.*
import com.example.clinic_info_branch.view_model.ViewModel
import kotlinx.android.synthetic.main.fragment_treatment_process.*
import kotlinx.android.synthetic.main.fragment_treatment_process.spinnerDaysTreatment
import kotlinx.android.synthetic.main.fragment_treatment_process.spinnerMonthsTreatment
import kotlinx.android.synthetic.main.fragment_treatment_process.spinnerYearsTreatment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TreatmentProcessFragment : BaseFragment() {

    private lateinit var patientPhone: String
    private lateinit var viewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //create view model instance
        viewModel = ViewModelProvider(activity!!).get(ViewModel::class.java)

        return inflater.inflate(R.layout.fragment_treatment_process, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val requestAdd = arguments?.getInt(REQUEST_ADD_PROCESS)

        if (requestAdd == addRequestProcess) {
            btnCommitProcess.isEnabled = false
            patientPhone = arguments?.getString(PHONE_FROM_PERSONAL_PAGE).toString()
        } else {
            btnAddProcess.isEnabled = false
        }

        //commit treatment process
        btnCommitProcess.setOnClickListener {

            val visitDate = "${spinnerDaysTreatment.selectedItem}" +
                    " ${spinnerMonthsTreatment.selectedItem}" +
                    " ${spinnerYearsTreatment.selectedItem}"

            val subReasonVisit = etSubData.text.toString()
            val objReasonVisit = etObjData.text.toString()
            val diagnosis = etDiagnosis.text.toString()
            val treatmentPlan = etTreatmentPlan.text.toString()

            val treatmentProcess = TreatmentProcess(
                visitDate, subReasonVisit, objReasonVisit,
                diagnosis, treatmentPlan
            )

            viewModel.treatmentProcessList.clear()
            viewModel.treatmentProcessList.add(treatmentProcess)
            activity!!.supportFragmentManager.popBackStack()

        }

        //add treatment process
        btnAddProcess.setOnClickListener {

            val visitDate = "${spinnerDaysTreatment.selectedItem}" +
                    " ${spinnerMonthsTreatment.selectedItem}" +
                    " ${spinnerYearsTreatment.selectedItem}"
            val subReasonVisit = etSubData.text.toString()
            val objReasonVisit = etObjData.text.toString()
            val diagnosis = etDiagnosis.text.toString()
            val treatmentPlan = etTreatmentPlan.text.toString()
            val treatmentProcess = TreatmentProcess(
                visitDate, subReasonVisit, objReasonVisit,
                diagnosis, treatmentPlan
            )

            //update patient to database
            GlobalScope.launch(Dispatchers.Default) {

                val patient = db.patientDao().getPatient(patientPhone)
                viewModel.treatmentProcessList = patient.treatmentProcessList
                viewModel.treatmentProcessList.add(treatmentProcess)

                db.patientDao().addTreatmentProcess(viewModel.treatmentProcessList, patientPhone)

            }

            activity!!.supportFragmentManager.popBackStack()

        }
    }
}
