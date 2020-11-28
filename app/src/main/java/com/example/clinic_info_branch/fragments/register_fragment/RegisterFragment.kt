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


const val TREATMENT_PROCESS = "treatment_process"
const val HEALTH_INFO = "health_info"
const val REQUEST_TO_TEETH_DIAGRAM = "request_to_teeth_diagram"
const val REQUEST_TO_HEALTH_INFO = "request_to_health_info_information"
const val registerRequestTeeth = 122
const val registerRequestHealth = 119
const val REQUEST_TO_PROCESS_FROM_REGISTER = "request_to_process_from_register"
const val registerRequestProcess = 156


class RegisterFragment : Fragment() {

    private var db: ClinicInfo? = null
    private lateinit var patientList: MutableList<Patient>
    private lateinit var stateOfTeethList: MutableList<StateOfTooth>
    private lateinit var treatmentProcessList: MutableList<TreatmentProcess>
    private lateinit var oralHealth: OralHealth
    private lateinit var job: Job
    private lateinit var healthInfo: HealthInfo
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

        //get data from teeth diagram fragment
        val jsonFromTeethDiagram = arguments?.getString(ORAL_HEALTH)


        if (jsonFromTeethDiagram != null) {
            oralHealth =
                Gson().fromJson(jsonFromTeethDiagram, object : TypeToken<OralHealth>() {}.type)
        } else {
            stateOfTeethList = mutableListOf()
            stateOfTeethList.add(
                StateOfTooth(
                    resources.getString(R.string.stateOfTeethDefault),
                    "", "", "", "", "", "", "",
                    "", "", "", ""
                )
            )
            oralHealth = OralHealth(
                resources.getString(R.string.hygieneGood),
                resources.getString(R.string.biteNormally), stateOfTeethList
            )
        }

        //get data from treatment process fragment
        val jsonFromTreatmentProcess = arguments?.getString(TREATMENT_INFO_MESSAGE)
        treatmentProcessList = mutableListOf()
        if (jsonFromTreatmentProcess != null){
            val treatmentProcess: TreatmentProcess = Gson().fromJson(jsonFromTreatmentProcess, object : TypeToken<TreatmentProcess>() {}.type)
            treatmentProcessList.add(treatmentProcess)
        }else{
            treatmentProcessList.add(TreatmentProcess("","","",
                "",""))
        }





        //get data from health info fragment
        val jsonFromHealthInfo = arguments?.getString(HEALTH_INFO_MESSAGE)

        if (jsonFromHealthInfo != null) {
            healthInfo =
                Gson().fromJson(jsonFromHealthInfo, object : TypeToken<HealthInfo>() {}.type)
        } else {
            healthInfo = HealthInfo(
                resources.getString(R.string.allergyFalse),
                "", resources.getString(R.string.anemiaFalse),
                "", "", "", "", "",
                "", "", "", "",
                "", "", "", "",
                "", "", "", "", "",
                "", "", "", "", false,
                "", false, ""
            )
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //navigate to health info
        btnHealthInfo.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(REQUEST_TO_HEALTH_INFO, registerRequestHealth)
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragmentContainer, HealthInfoFragment())
                addToBackStack(HEALTH_INFO)
                commit()
            }
        }


        //navigate to teeth diagram
        btnOralHealth.setOnClickListener {
           val bundle = Bundle()
           bundle.putInt(REQUEST_TO_TEETH_DIAGRAM, registerRequestTeeth)
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragmentContainer, TeethDiagramFragment().apply { arguments = bundle })
                addToBackStack(TEETH_DIAGRAM_FROM_REGISTER)
                commit()
            }

        }
        //navigate to treatment process editor
        btnTreatmentProcess.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(REQUEST_TO_PROCESS_FROM_REGISTER, registerRequestProcess)
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


            //create patient
            val patient = Patient(
                patientName,
                patientDate,
                gender,
                placeOfResidence,
                phone,
                healthInfo,
                oralHealth,
                treatmentProcessList
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

