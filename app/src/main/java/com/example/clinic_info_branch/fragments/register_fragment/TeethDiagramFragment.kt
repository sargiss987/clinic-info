package com.example.clinic_info_branch.fragments.register_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.clinic_info_branch.R
import com.example.clinic_info_branch.data_base.OralHealth
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_teeth_diagram.*


const val ORAL_HEALTH = "oral_health"

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
                commit()
            }
        }
    }
}