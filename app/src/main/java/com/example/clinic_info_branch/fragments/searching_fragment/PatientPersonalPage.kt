package com.example.clinic_info_branch.fragments.searching_fragment


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clinic_info_branch.R
import com.example.clinic_info_branch.data_base.ClinicInfo
import com.example.clinic_info_branch.data_base.Patient
import com.example.clinic_info_branch.data_base.TreatmentProcess
import com.example.clinic_info_branch.fragments.register_fragment.HealthInfoFragment
import com.example.clinic_info_branch.fragments.register_fragment.TeethDiagramFragment
import com.example.clinic_info_branch.fragments.register_fragment.TreatmentProcessFragment
import kotlinx.android.synthetic.main.fragment_patient_personal_page.*
import kotlinx.android.synthetic.main.fragment_patient_personal_page.view.*
import kotlinx.coroutines.*



const val REQUEST_UPDATE_ORAL_HEALTH = "request_update_oral_health"
const val REQUEST_UPDATE_HEALTH = "request_from_register"
const val updateRequestOralHealth = 123
const val updateRequestHealth = 130
const val PHONE_FROM_PERSONAL_PAGE = "phone_from_personal_page"
const val REQUEST_ADD_PROCESS = "request_add_process"
const val addRequestProcess = 188
const val TREATMENT_PROCESS_INFO = "treatment_process_info"



class PatientPersonalPage : Fragment(), RecTreatmentProcessAdapter.RecViewClickListener {

    var patient: Patient? = null
    private var db: ClinicInfo? = null
    private lateinit var viewAdapter: RecTreatmentProcessAdapter
    private lateinit var job: Job
    private lateinit var treatmentProcessList: MutableList<TreatmentProcess>
    private lateinit var patientList: MutableList<Patient>
    private var position: Int = 0

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

        val view = inflater.inflate(R.layout.fragment_patient_personal_page, container, false)

        viewAdapter = RecTreatmentProcessAdapter(this)
        position = arguments?.getInt(PATIENT_INFO)!!

        //get treatment process list from database
        //update display
        job = GlobalScope.launch(Dispatchers.Default) {

            if (db != null) {
                patientList = db!!.patientDao().getAllPatients()
                treatmentProcessList = patientList[position].treatmentProcessList
            }
            withContext(Dispatchers.Main) {

                viewAdapter.setList(treatmentProcessList)

                //show list of notes
                recViewTreatmentProcess.apply {
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = viewAdapter
                }


                patient = patientList[position]

                var healthInfo: String = if (patient?.healthInfo?.allergicManifestation!!.isEmpty()) {
                    "${patient?.healthInfo?.allergy} ${patient?.healthInfo?.bleeding}"
                } else {
                    "${patient?.healthInfo?.allergy} ${patient?.healthInfo?.allergicManifestation} ${patient?.healthInfo?.bleeding}"
                }

                val healthInfoList = mutableListOf(
                    "${patient?.healthInfo?.rheumatism}",
                    "${patient?.healthInfo?.arthritis}",
                    "${patient?.healthInfo?.heartDefect}",
                    "${patient?.healthInfo?.heartAttack}",
                    "${patient?.healthInfo?.heartSurgery}",
                    "${patient?.healthInfo?.stenocardia}",
                    "${patient?.healthInfo?.kidneyDisease}",
                    "${patient?.healthInfo?.bloodDisease}",
                    "${patient?.healthInfo?.gastrointestinalTractDisease}",
                    "${patient?.healthInfo?.respiratoryTractDisease}",
                    "${patient?.healthInfo?.hypertension}",
                    "${patient?.healthInfo?.hypotension}",
                    "${patient?.healthInfo?.thyroidGladDisease}",
                    "${patient?.healthInfo?.nervousMentalDisorders}",
                    "${patient?.healthInfo?.epilepsy}",
                    "${patient?.healthInfo?.diabetes}",
                    "${patient?.healthInfo?.infectionsDiseases}",
                    "${patient?.healthInfo?.neoplasm}",
                    "${patient?.healthInfo?.hepatitis}",
                    "${patient?.healthInfo?.otherLiverDiseases}",
                    "${patient?.healthInfo?.sexuallyTransmittedDiseases}",
                    "${patient?.healthInfo?.skinDiseases}",
                    "${patient?.healthInfo?.otherDiseasesDescription}"
                )


                val healthInfoTxtInitial = resources.getString(R.string.healthInfoTxtInitial)
                var healthInfoTxt = healthInfoTxtInitial
                var i = 0
                while (i < healthInfoList.size) {

                    if (healthInfoList[i].isNotEmpty()) {
                        healthInfoTxt += healthInfoList[i] + ", "
                    }
                    i++
                }

                when {
                    healthInfoTxt == healthInfoTxtInitial -> healthInfoTxt = ""
                    patient?.healthInfo?.duringPregnancy!!.isNotEmpty() -> healthInfoTxt += "${patient?.healthInfo?.duringPregnancy} շաբաթական հղիություն։"
                    else -> {
                        healthInfoTxt = healthInfoTxt.subSequence(0, healthInfoTxt.length - 2) as String
                        healthInfoTxt += ":"
                    }
                }

                healthInfo += healthInfoTxt

                //oral info
                var oralHealth = "${patient?.oralHealth?.hygiene} ${patient?.oralHealth?.typeOfBite}"
                val oralHealthList = patient?.oralHealth?.stateOfTeeth
                var oralHealthTxt = ""
                i = 0
                if (oralHealthList != null) {
                    while (i < oralHealthList.size) {

                        oralHealthTxt += oralHealthList[i].toothNumber + " "
                        oralHealthTxt += oralHealthList[i].missingTooth
                        oralHealthTxt += oralHealthList[i].caries
                        oralHealthTxt += oralHealthList[i].pulpitis
                        oralHealthTxt += oralHealthList[i].periodontitis
                        oralHealthTxt += oralHealthList[i].root
                        oralHealthTxt += oralHealthList[i].implant
                        oralHealthTxt += oralHealthList[i].rootFilling
                        oralHealthTxt += oralHealthList[i].plaque
                        oralHealthTxt += oralHealthList[i].tartar
                        oralHealthTxt += oralHealthList[i].artCrown
                        oralHealthTxt += oralHealthList[i].toothMobility
                        oralHealthTxt += ", "
                        i++
                    }
                }
                if (oralHealthTxt.length > 2) {
                    oralHealthTxt = oralHealthTxt.substring(0, oralHealthTxt.length - 2) + ":"
                }

                oralHealth += oralHealthTxt

                view.txtFullName.text = patient?.patientName
                view.txtDate.text = patient?.patientDate
                view.txtGender.text = patient?.gender
                view.txtPlace.text = patient?.placeOfResidence
                view.txtPhone.text = patient?.phone
                view.txtHealthInfo.text = healthInfo
                view.txtOralHealthInfo.text = oralHealth

            }
        }


        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //update oral health information
        btnTeethDiagram.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(REQUEST_UPDATE_ORAL_HEALTH, updateRequestOralHealth)
            bundle.putString(PHONE_FROM_PERSONAL_PAGE, patient?.phone)
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragmentContainer, TeethDiagramFragment().apply { arguments = bundle })
                commit()
            }
        }

        //update health info information
        btnHealthInfoPersonal.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(REQUEST_UPDATE_HEALTH, updateRequestHealth)
            bundle.putString(PHONE_FROM_PERSONAL_PAGE, patient?.phone)
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragmentContainer, HealthInfoFragment().apply { arguments = bundle })
                commit()
            }
        }

        //add treatment process
        btnAdd.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(REQUEST_ADD_PROCESS, addRequestProcess)
            bundle.putString(PHONE_FROM_PERSONAL_PAGE, patient?.phone)
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragmentContainer, TreatmentProcessFragment().apply { arguments = bundle })
                commit()
            }
        }
    }

    //navigate to patient treatment process page
    override fun onClick(position: Int) {
        val bundle = Bundle()

        bundle.putParcelable(TREATMENT_PROCESS_INFO,treatmentProcessList[position])

        fragmentManager?.beginTransaction()?.apply {
            replace(R.id.fragmentContainer, PatientTreatmentProcessFragment().apply { arguments = bundle })
            addToBackStack(PATIENT_PERSONAL_PAGE)
            commit()
        }
    }

    //job cancel
    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


}