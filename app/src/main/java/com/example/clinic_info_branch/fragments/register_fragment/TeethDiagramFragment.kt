package com.example.clinic_info_branch.fragments.register_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.clinic_info_branch.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_teeth_diagram.*

const val STATE_OF_TOOTH = "state_of_tooth"
const val STATE_OF_HYGIENE = "state_of_hygiene"
const val STATE_OF_BITE = "state_of_bite"

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


            val bundle = Bundle()
            val gson = Gson()
            val message = gson.toJson(stateOfTeethList)
            bundle.putString(STATE_OF_TOOTH,message)
            bundle.putString(STATE_OF_HYGIENE,hygiene)
            bundle.putString(STATE_OF_BITE,typeOfBite)

            fragmentManager?.beginTransaction()?.apply {

                replace(R.id.fragmentContainer, RegisterFragment().apply { arguments = bundle})
                commit()
            }
        }
    }
}