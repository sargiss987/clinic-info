package com.example.clinic_info_branch.fragments.register_fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.clinic_info_branch.R
import com.example.clinic_info_branch.data_base.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

const val TEETH_DIAGRAM_FROM_REGISTER = "teeth_diagram_from_register"
const val TREATMENT_PROCESS = "treatment_process"

class RegisterFragment : Fragment() {

    private var db: ClinicInfo? = null
    private lateinit var patientList: MutableList<Patient>
    private lateinit var stateOfTeethList: MutableList<StateOfTooth>
    private lateinit var hygiene: String
    private lateinit var typeOfBite: String
    private lateinit var job: Job
    private var validationNum = true

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //get database
        db = ClinicInfo.getDatabase(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //get patient list from database
        job = GlobalScope.launch(Dispatchers.Default) {

            if (db != null) {
                patientList = db!!.patientDao().getAllPatients().toMutableList()
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        val json = arguments?.getString(STATE_OF_TOOTH)
        val hygiene = arguments?.getString(STATE_OF_HYGIENE).toString()
        typeOfBite = arguments?.getString(STATE_OF_BITE).toString()

        if (hygiene.length > 1){
            this.hygiene = hygiene
        }else{
            this.hygiene = resources.getString(R.string.hygieneGood)
        }
        if (typeOfBite.isEmpty()) typeOfBite = resources.getString(R.string.biteNormally)

        if (json != null) {
            stateOfTeethList =
                Gson().fromJson(json, object : TypeToken<MutableList<StateOfTooth>>() {}.type)
        } else {
            stateOfTeethList = mutableListOf()
            stateOfTeethList.add(
                StateOfTooth(
                    resources.getString(R.string.stateOfTeethDefault),
                    "", "", "", "", "", "", "",
                    "", "", "", ""
                )
            )
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //navigate to teeth diagram
        btnTeethDiagram.setOnClickListener {
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragmentContainer, TeethDiagramFragment())
                addToBackStack(TEETH_DIAGRAM_FROM_REGISTER)
                commit()
            }

        }
        //navigate to treatment process editor
        btnTreatmentProcess.setOnClickListener {
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragmentContainer, TreatmentProcessFragment())
                addToBackStack(TREATMENT_PROCESS)
                commit()
            }

        }



        btnRegister.setOnClickListener {

            //Init patient fields
            //Init personal data
            val patientName = fullName.text.toString()
            val placeOfResidence = address.text.toString()
            val phone = etPhoneValidation.text.toString()
            val patientDate = "${spinnerDaysTreatment.selectedItem}" +
                    " ${spinnerMonthsTreatment.selectedItem}" +
                    " ${spinnerYearsTreatment.selectedItem}"
            val gender = if (checkMale.isChecked) "արական" else "իգական"

            //Init health info data
            val allergy =
                if (checkAllergyTrue.isChecked) resources.getString(R.string.allergyTrue)
                else resources.getString(R.string.allergyFalse)
            val allergicManifestation = etAllergy.text.toString()
            val bleeding =
                if (checkBleedingTrue.isChecked) resources.getString(R.string.anemiaTrue)
                else resources.getString(R.string.anemiaFalse)
            val rheumatism =
                if (checkRheumatismTrue.isChecked) resources.getString(R.string.rheumatism)
                else ""
            val arthritis =
                if (checkArthritisTrue.isChecked) resources.getString(R.string.arthritis)
                else ""
            val heartDefect =
                if (checkHeartDiseaseTrue.isChecked) resources.getString(R.string.heartDefect)
                else ""
            val heartAttack =
                if (checkMyocardialInfarctionTrue.isChecked) resources.getString(R.string.heartAttack)
                else ""
            val heartSurgery =
                if (checkHeartSurgeryTrue.isChecked) resources.getString(R.string.heartSurgery)
                else ""
            val stenocardia =
                if (checkStenocardiaTrue.isChecked) resources.getString(R.string.stenocardia)
                else ""
            val kidneyDisease =
                if (checkKidneyDiseasesTrue.isChecked) resources.getString(R.string.kidneyDisease)
                else ""
            val bloodDisease =
                if (checkAnemiaTrue.isChecked) resources.getString(R.string.bloodDisease)
                else ""
            val gastrointestinalTractDisease =
                if (checkGastrointestinalTrue.isChecked) resources.getString(R.string.gastrointestinalTractDisease)
                else ""
            val respiratoryTractDisease =
                if (checkRespiratoryTrue.isChecked) resources.getString(R.string.respiratoryTractDisease)
                else ""
            val hypertension =
                if (checkHypertensionTrue.isChecked) resources.getString(R.string.hypertension)
                else ""
            val hypotension =
                if (checkHypotensionTrue.isChecked) resources.getString(R.string.hypotension)
                else ""
            val thyroidGladDisease =
                if (checkThyroidTrue.isChecked) resources.getString(R.string.thyroidGladDisease)
                else ""
            val nervousMentalDisorders =
                if (checkNervousMentalTrue.isChecked) resources.getString(R.string.nervousMentalDisorders)
                else ""
            val epilepsy =
                if (checkEpilepsyTrue.isChecked) resources.getString(R.string.epilepsy)
                else ""
            val diabetes =
                if (checkDiabetesTrue.isChecked) resources.getString(R.string.diabetes)
                else ""
            val infectionsDiseases =
                if (checkInfectiousTrue.isChecked) resources.getString(R.string.infectionsDiseases)
                else ""
            val neoplasm =
                if (checkNeoplasmsTrue.isChecked) resources.getString(R.string.neoplasm)
                else ""
            val hepatitis =
                if (checkHepatitisTrue.isChecked) resources.getString(R.string.hepatitis)
                else ""
            val otherLiverDiseases =
                if (checkOtherLiverTrue.isChecked) resources.getString(R.string.otherLiverDiseases)
                else ""
            val sexuallyTransmittedDiseases =
                if (checkVenerealTrue.isChecked) resources.getString(R.string.sexuallyTransmittedDiseases)
                else ""
            val skinDiseases =
                if (checkSkinTrue.isChecked) resources.getString(R.string.skinDiseases)
                else ""
            val otherDiseases = checkOtherTrue.isChecked
            val otherDiseasesDescription = etQuestion4.text.toString()
            val pregnancy = checkPregnancyTrue.isChecked
            val duringPregnancy = etQuestion5.text.toString()

            //create patient
            val patient = Patient(
                patientName,
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
                ),
                OralHealth(
                    hygiene,
                    typeOfBite,
                    stateOfTeethList
                )
            )


            //register validation via number
            patientList.forEach {
                validationNum = it.phone != phone

            }

            when {
                etPhoneValidation.text.toString().isEmpty() -> {
                    etPhoneValidation.error = "Field cannot be empty"
                    Toast.makeText(context, "Field cannot be empty", Toast.LENGTH_LONG).show()
                }
                validationNum -> {
                    //insert patient to database
                    GlobalScope.launch(Dispatchers.Default) {

                        db?.patientDao()?.insertPatient(patient)
                    }
                    Toast.makeText(context, "Registration is successful", Toast.LENGTH_LONG).show()
                }

                else -> {
                    etPhoneValidation.error = "The patient already exist"
                    Toast.makeText(context, "The patient already exist", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
