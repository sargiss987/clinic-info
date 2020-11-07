package com.example.clinic_info_branch.fragments.register_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.example.clinic_info_branch.R
import com.example.clinic_info_branch.data_base.ClinicInfo
import com.example.clinic_info_branch.data_base.HealthInfo
import com.example.clinic_info_branch.data_base.Patient
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.rec_patient_item.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

const val TEETH_DIAGRAM = "teeth_diagram"
const val TREATMENT_PROCESS = "treatment_process"

class RegisterFragment : Fragment() {

    private var db: ClinicInfo? = null
    private lateinit var patientList: MutableList<Patient>

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
                replace(R.id.fragmentContainer, TreatmentProcessFragment())
                addToBackStack(TREATMENT_PROCESS)
                commit()
            }

        }



        btnRegister.setOnClickListener {

            //Init patient fields
            val patientName = fullName.text.toString()
            val placeOfResidence = address.text.toString()
            val phone = phone.text.toString()
            val patientDate = "${spinnerDaysTreatment.selectedItem}" +
                    " ${spinnerMonthsTreatment.selectedItem}"+
                    " ${spinnerYearsTreatment.selectedItem}"
            val gender = if (checkMale.isChecked) "արական" else "իգական"

            val allergy =
                if (checkAllergyTrue.isChecked) resources.getString(R.string.allergyTrue)
                else resources.getString(R.string.allergyFalse)
            val allergicManifestation = etAllergy.text.toString()
            val bleeding =
                if (checkBleedingTrue.isChecked) resources.getString(R.string.anemiaTrue)
                else resources.getString(R.string.anemiaFalse)
            val rheumatism = checkRheumatismTrue.isChecked
            val arthritis = checkArthritisTrue.isChecked
            val heartDefect = checkHeartDiseaseTrue.isChecked
            val heartAttack = checkMyocardialInfarctionTrue.isChecked
            val heartSurgery = checkHeartSurgeryTrue.isChecked
            val stenocardia = checkStenocardiaTrue.isChecked
            val kidneyDisease = checkKidneyDiseasesTrue.isChecked
            val bloodDisease = checkAnemiaTrue.isChecked
            val gastrointestinalTractDisease = checkGastrointestinalTrue.isChecked
            val respiratoryTractDisease = checkRespiratoryTrue.isChecked
            val hypertension = checkHypertensionTrue.isChecked
            val hypotension = checkHypotensionTrue.isChecked
            val thyroidGladDisease = checkThyroidTrue.isChecked
            val nervousMentalDisorders = checkNervousMentalTrue.isChecked
            val epilepsy = checkEpilepsyTrue.isSelected
            val diabetes = checkDiabetesTrue.isSelected
            val infectionsDiseases = checkInfectiousTrue.isSelected
            val neoplasm = checkNeoplasmsTrue.isSelected
            val hepatitis = checkHepatitisTrue.isSelected
            val otherLiverDiseases = checkOtherLiverTrue.isChecked
            val sexuallyTransmittedDiseases = checkVenerealTrue.isChecked
            val skinDiseases = checkSkinTrue.isSelected
            val otherDiseases = checkOtherTrue.isSelected
            val otherDiseasesDescription = etQuestion4.text.toString()
            val pregnancy = checkPregnancyTrue.isSelected
            val duringPregnancy = etQuestion5.text.toString()

            val patient = Patient(patientName,
                                  patientDate,
                                  gender,
                                  placeOfResidence,
                                  phone,
                                  HealthInfo(
                                      allergy,
                                      allergicManifestation,
                                      bleeding,
                                      rheumatism,
                                      arthritis,
                                      heartDefect,
                                      heartAttack,
                                      heartSurgery,
                                      stenocardia,
                                      kidneyDisease,
                                      bloodDisease,
                                      gastrointestinalTractDisease,
                                      respiratoryTractDisease,
                                      hypertension,
                                      hypotension,
                                      thyroidGladDisease,
                                      nervousMentalDisorders,
                                      epilepsy,
                                      diabetes,
                                      infectionsDiseases,
                                      neoplasm,
                                      hepatitis,
                                      otherLiverDiseases,
                                      sexuallyTransmittedDiseases,
                                      skinDiseases,
                                      otherDiseases,
                                      otherDiseasesDescription,
                                      pregnancy,
                                      duringPregnancy

                                  )
            )

            Toast.makeText(context, "$patient",Toast.LENGTH_SHORT).show()

            //insert patient to database
            GlobalScope.launch(Dispatchers.Default) {

                db?.patientDao()?.insertPatient(patient)
            }
        }
    }
}
