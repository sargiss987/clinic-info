package com.example.clinic_info_branch.fragments.register_fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.clinic_info_branch.R
import com.example.clinic_info_branch.Views.stateOfTeethList
import com.example.clinic_info_branch.data_base.OralHealth
import com.example.clinic_info_branch.fragments.BaseFragment
import com.example.clinic_info_branch.fragments.searching_fragment.*
import com.example.clinic_info_branch.view_model.ViewModel
import kotlinx.android.synthetic.main.fragment_teeth_diagram.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TeethDiagramFragment : BaseFragment() {

    private lateinit var patientPhone: String
    private lateinit var viewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //create view model instance
        viewModel = ViewModelProvider(activity!!).get(ViewModel::class.java)

        return inflater.inflate(R.layout.fragment_teeth_diagram, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val requestRegister = arguments?.getInt(REQUEST_TO_TEETH_DIAGRAM)
        val requestUpdate = arguments?.getInt(REQUEST_UPDATE_ORAL_HEALTH)

        //if (requestRegister == registerRequestTeeth) btnUpdate.isEnabled = false
        if (requestUpdate == updateRequestOralHealth) {
            btnCommitTeeth.isEnabled = false
            patientPhone = arguments?.getString(PHONE_FROM_PERSONAL_PAGE).toString()
        } else {
            btnUpdate.isEnabled = false
        }

        //commit data
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

            viewModel.oralHealth = OralHealth(hygiene, typeOfBite, stateOfTeethList)

            activity!!.supportFragmentManager.popBackStack()
        }

        //update data
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
                db.patientDao()
                    .updateOralHealth(hygiene, typeOfBite, stateOfTeethList, patientPhone)
            }

            activity!!.supportFragmentManager.popBackStack()

        }
    }
}