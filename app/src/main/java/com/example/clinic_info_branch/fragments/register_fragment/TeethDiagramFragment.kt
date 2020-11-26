package com.example.clinic_info_branch.fragments.register_fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.clinic_info_branch.R
import com.example.clinic_info_branch.data_base.ClinicInfo
import com.example.clinic_info_branch.data_base.OralHealth
import com.example.clinic_info_branch.fragments.searching_fragment.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_teeth_diagram.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


const val ORAL_HEALTH = "oral_health"

class TeethDiagramFragment : Fragment() {

    private var db: ClinicInfo? = null
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
        return inflater.inflate(R.layout.fragment_teeth_diagram, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val requestRegister = arguments?.getInt(REQUEST_TO_TEETH_DIAGRAM)
        val requestUpdate = arguments?.getInt(REQUEST_UPDATE_ORAL_HEALTH)


        if (requestRegister == registerRequestTeeth) btnUpdate.isEnabled = false
        if (requestUpdate == updateRequestOralHealth) {
            btnCommitTeeth.isEnabled = false
            patientPhone = arguments?.getString(PHONE_FROM_PERSONAL_PAGE).toString()
        }

        btnCommitTeeth.setOnClickListener {
            val hygiene =
                when {
                    checkGood.isChecked -> resources.getString(R.string.hygieneGood)
                    checkEnough.isChecked -> resources.getString(R.string.hygieneEnough)
                    checkBad.isChecked -> resources.getString(R.string.hygieneBad)
                    else -> resources.getString(R.string.hygieneGood)
                }
            val typeOfBite =
                if (checkPathological.isChecked) resources.getString(R.string.bitePathological)
                else resources.getString(R.string.biteNormally)

            val oralHealth = OralHealth(hygiene, typeOfBite, stateOfTeethList)
            val bundle = Bundle()
            val gson = Gson()
            val message = gson.toJson(oralHealth)
            bundle.putString(ORAL_HEALTH, message)


            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragmentContainer, RegisterFragment().apply { arguments = bundle })
                addToBackStack(TREATMENT_PROCESS)
                commit()
            }
        }

        btnUpdate.setOnClickListener() {

            val hygiene =
                when {
                    checkGood.isChecked -> resources.getString(R.string.hygieneGood)
                    checkEnough.isChecked -> resources.getString(R.string.hygieneEnough)
                    checkBad.isChecked -> resources.getString(R.string.hygieneBad)
                    else -> resources.getString(R.string.hygieneGood)
                }
            val typeOfBite =
                if (checkPathological.isChecked) resources.getString(R.string.bitePathological)
                else resources.getString(R.string.biteNormally)

            //update patient to database
            GlobalScope.launch(Dispatchers.Default) {

                db?.patientDao()?.updateOralHealth(hygiene,typeOfBite, stateOfTeethList,patientPhone)
            }


            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragmentContainer, SearchingFragment())
                commit()
            }
        }
    }
}