package com.example.clinic_info_branch.fragments.register_fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.clinic_info_branch.R
import com.example.clinic_info_branch.data_base.ClinicInfo
import com.example.clinic_info_branch.data_base.Patient
import com.example.clinic_info_branch.data_base.TreatmentProcess
import com.example.clinic_info_branch.fragments.searching_fragment.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_patient_personal_page.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_teeth_diagram.*
import kotlinx.android.synthetic.main.fragment_treatment_process.*
import kotlinx.android.synthetic.main.fragment_treatment_process.spinnerDaysTreatment
import kotlinx.android.synthetic.main.fragment_treatment_process.spinnerMonthsTreatment
import kotlinx.android.synthetic.main.fragment_treatment_process.spinnerYearsTreatment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val TREATMENT_INFO_MESSAGE = "treatment_info_message"

class TreatmentProcessFragment : Fragment() {

    private var db: ClinicInfo? = null
    private lateinit var treatmentProcessList : MutableList<TreatmentProcess>

    private lateinit var patientPhone: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //get database
        db = ClinicInfo.getDatabase(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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

            val bundle = Bundle()
            val gson = Gson()
            val message = gson.toJson(treatmentProcess)
            bundle.putString(TREATMENT_INFO_MESSAGE, message)

            fragmentManager?.beginTransaction()?.apply {

                replace(R.id.fragmentContainer, RegisterFragment().apply { arguments = bundle })
                commit()
            }
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

                val patient = db!!.patientDao().getPatient(patientPhone)
                treatmentProcessList = patient.treatmentProcessList
                treatmentProcessList.add(treatmentProcess)

                db?.patientDao()?.addTreatmentProcess(treatmentProcessList,patientPhone)

            }


            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragmentContainer, SearchingFragment())
                commit()
            }
        }


    }
}
