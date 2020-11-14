package com.example.clinic_info_branch.fragments.searching_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.clinic_info_branch.R
import com.example.clinic_info_branch.data_base.ClinicInfo
import com.example.clinic_info_branch.data_base.Notes
import com.example.clinic_info_branch.data_base.Patient
import com.example.clinic_info_branch.fragments.home_fragment.RecNoteAdapter
import com.example.clinic_info_branch.fragments.register_fragment.TREATMENT_PROCESS
import com.example.clinic_info_branch.fragments.register_fragment.TreatmentProcessFragment
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_patient_personal_page.*
import kotlinx.android.synthetic.main.fragment_searching.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val PATIENT_PERSONAL_PAGE = "patient_personal_page"
const val PATIENT_INFO = "patient_info"

class SearchingFragment : Fragment(),RecPatientAdapter.RecViewClickListener {

    private var db: ClinicInfo? = null
    private lateinit var patientList: MutableList<Patient>
    private lateinit var viewAdapter: RecPatientAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //build database
        db = context?.let {
            Room.databaseBuilder(
                it,
                ClinicInfo::class.java, "clinic_info"
            ).build()
        }

        //get patient list from database
        GlobalScope.launch(Dispatchers.Default) {

            if (db != null) {
                patientList = db!!.patientDao().getAllPatients() as MutableList<Patient>
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_searching, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewAdapter = RecPatientAdapter(this)
        viewAdapter.setList(patientList)



        //show list of patients
      recViewPatient.apply {
          layoutManager = LinearLayoutManager(context)
          adapter = viewAdapter
      }
    }

    //delete patient from database
    override fun delete(position: Int) {
       val patient = patientList[position]

        //delete patient from database
        GlobalScope.launch(Dispatchers.Default) {

            db?.patientDao()?.deletePatient(patient)
        }

        //update list
        patientList.remove(patient)
        viewAdapter.setList(patientList)
    }

    override fun onClick(position: Int) {

        val bundle = Bundle()
        bundle.putParcelable(PATIENT_INFO,patientList[position])

        fragmentManager?.beginTransaction()?.apply {

            replace(R.id.fragmentContainer, PatientPersonalPage().apply { arguments = bundle})
            addToBackStack(PATIENT_PERSONAL_PAGE)
            commit()
        }

    }
}