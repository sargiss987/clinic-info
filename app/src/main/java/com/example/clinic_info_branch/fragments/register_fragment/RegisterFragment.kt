package com.example.clinic_info_branch.fragments.register_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.clinic_info_branch.R
import com.example.clinic_info_branch.Views.stateOfTeethList
import com.example.clinic_info_branch.data_base.*
import com.example.clinic_info_branch.fragments.BaseFragment
import com.example.clinic_info_branch.view_model.ViewModel
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.*

class RegisterFragment : BaseFragment() {

    private lateinit var patientList: MutableList<Patient>
    private lateinit var job: Job
    private lateinit var viewModel: ViewModel
    private var fullNameList: MutableSet<String> = mutableSetOf()
    private var countryList: MutableSet<String> = mutableSetOf()

    //get patient list
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //get patient list from database
        job = GlobalScope.launch(Dispatchers.Default) {

            patientList = db.patientDao().getAllPatients().toMutableList()

            //init fullNameList
            //init countryList

            fullNameList = mutableSetOf()
            db.notesDao().getAllNotes().forEach {
                fullNameList.add(it.name)
            }
            patientList.forEach {
                fullNameList.add(it.patientName)
                countryList.add(it.placeOfResidence)
            }
        }

        //create view model instance
        viewModel = ViewModelProvider(activity!!).get(ViewModel::class.java)

        //init view model data
        viewModel.stateOfTeethList = mutableListOf(
            StateOfTooth(
                resources.getString(R.string.stateOfTeethDefault),
                "", "", "", "", "", "", "",
                "", "", "", ""
            )
        )

        viewModel.treatmentProcessList = mutableListOf(
            TreatmentProcess(
                "", "", "",
                "", ""
            )
        )

        viewModel.oralHealth = OralHealth(
            resources.getString(R.string.hygieneGood),
            resources.getString(R.string.biteNormally),
            stateOfTeethList
        )

        viewModel.healthInfo = HealthInfo(
            resources.getString(R.string.allergyFalse), "",
            resources.getString(R.string.anemiaFalse), "", "", "", "",
            "", "", "", "", "",
            "", "", "", "", "",
            "", "", "", "", "", "",
            "", "", false, "",
            false, ""
        )
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

        //set adapter to fullName
        fullName.setAdapter(context?.let { it1 ->
            ArrayAdapter(
                it1,
                android.R.layout.simple_list_item_1,
                fullNameList.toList()
            )
        })
        //set adapter to address
        address.setAdapter(context?.let { it1 ->
            ArrayAdapter(
                it1,
                android.R.layout.simple_list_item_1,
                countryList.toList()
            )
        })

        //navigate to health info
        btnHealthInfo.setOnClickListener {

            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragmentContainer, HealthInfoFragment())
                addToBackStack(null)
                commit()
            }
        }

        //navigate to teeth diagram
        btnOralHealth.setOnClickListener {
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragmentContainer, TeethDiagramFragment())
                addToBackStack(null)
                commit()
            }
        }

        //navigate to treatment process editor
        btnTreatmentProcess.setOnClickListener {
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragmentContainer, TreatmentProcessFragment())
                addToBackStack(null)
                commit()
            }
        }

        //register patient
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
            viewModel.validationNum = true

            //create patient
            val patient = Patient(
                patientName,
                patientDate,
                gender,
                placeOfResidence,
                phone,
                viewModel.healthInfo,
                viewModel.oralHealth,
                viewModel.treatmentProcessList
            )

            //register validation via number,name and place
            patientList.forEach {
                if (it.phone == phone) {
                    viewModel.validationNum = false
                }
            }

            when {
                fullName.text.toString().isEmpty() -> {
                    fullName.error = "Field cannot be empty"
                    Toast.makeText(context, "Field cannot be empty", Toast.LENGTH_LONG).show()
                }
                address.text.toString().isEmpty() -> {
                    address.error = "Field cannot be empty"
                    Toast.makeText(context, "Field cannot be empty", Toast.LENGTH_LONG).show()
                }

                etPhoneValidation.text.toString().isEmpty() -> {
                    etPhoneValidation.error = "Field cannot be empty"
                    Toast.makeText(context, "Field cannot be empty", Toast.LENGTH_LONG).show()
                }
                viewModel.validationNum -> {
                    //insert patient to database
                    GlobalScope.launch(Dispatchers.Default) {

                        db.patientDao().insertPatient(patient)

                        withContext(Dispatchers.Main) {
                            fragmentManager?.beginTransaction()?.apply {
                                replace(R.id.fragmentContainer, RegisterFragment())
                                commit()
                            }
                        }
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

