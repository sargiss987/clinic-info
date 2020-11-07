package com.example.clinic_info_branch.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.clinic_info_branch.R
import kotlinx.android.synthetic.main.fragment_register.*

const val TEETH_DIAGRAM = "teeth_diagram"
const val TREATMENT_PROCESS = "treatment_process"

class RegisterFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnTeethDiagram.setOnClickListener {
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragmentContainer, TeethDiagramFragment())
                addToBackStack(TEETH_DIAGRAM)
                commit()
            }

        }

        btnTreatmentProcess.setOnClickListener {
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragmentContainer,TreatmentProcessFragment())
                addToBackStack(TREATMENT_PROCESS)
                commit()
            }

        }
    }
}
