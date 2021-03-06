package com.example.clinic_info_branch.fragments.searching_fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clinic_info_branch.R
import com.example.clinic_info_branch.data_base.Patient
import com.example.clinic_info_branch.adapters.RecPatientAdapter
import com.example.clinic_info_branch.fragments.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_searching.*
import kotlinx.android.synthetic.main.fragment_searching.searchView
import kotlinx.coroutines.*
import java.util.*


const val PATIENT_INFO = "patient_info"

class SearchingFragment : BaseFragment(), RecPatientAdapter.RecViewClickListener {

    private lateinit var patientList: MutableList<Patient>
    private var searchingList: MutableList<Patient> = mutableListOf()
    private lateinit var viewAdapter: RecPatientAdapter
    private lateinit var job: Job



    override fun onResume() {
        super.onResume()
        viewAdapter = RecPatientAdapter(this)

        //get patient list from database
        job = GlobalScope.launch(Dispatchers.Default) {

            if (db != null) {
                patientList = db!!.patientDao().getAllPatients()
                delay(2000)
            }
            withContext(Dispatchers.Main) {
                progressBarSearching.visibility = View.GONE
                viewAdapter.setList(patientList)

                //show list of patients
                recViewPatient.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = viewAdapter
                }
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

        //searching by patient name,place of residence or phone
        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                if (::patientList.isInitialized) {
                    searchingList.clear()
                    if (p0!!.isNotEmpty()) {
                        searchingList = patientList.filter {
                            it.patientName.toLowerCase(Locale.getDefault())
                                .contains(p0.toLowerCase(Locale.getDefault())) or
                                    it.phone.contains(p0) or
                                    it.placeOfResidence.toLowerCase(Locale.getDefault())
                                        .contains(p0)
                        }.toMutableList()
                        viewAdapter.setList(searchingList)
                    } else {
                        viewAdapter.setList(patientList)
                    }
                }
                return true
            }
        })


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

    //navigate patient personal page
    override fun onClick(position: Int) {

        val bundle = Bundle()
        bundle.putInt(PATIENT_INFO, position)

        fragmentManager?.beginTransaction()?.apply {
            replace(
                R.id.fragmentContainer,
                PatientPersonalPageFragment().apply { arguments = bundle })
            addToBackStack(null)
            commit()
        }
    }

    //job cancel
    override fun onStop() {
        super.onStop()
        job.cancel()
    }
}