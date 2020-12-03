package com.example.clinic_info_branch.view_model

import androidx.lifecycle.ViewModel
import com.example.clinic_info_branch.data_base.HealthInfo
import com.example.clinic_info_branch.data_base.OralHealth
import com.example.clinic_info_branch.data_base.StateOfTooth
import com.example.clinic_info_branch.data_base.TreatmentProcess
import kotlin.properties.Delegates

class ViewModel() : ViewModel() {

     lateinit var stateOfTeethList: MutableList<StateOfTooth>
     lateinit var treatmentProcessList: MutableList<TreatmentProcess>
     lateinit var oralHealth: OralHealth
     lateinit var healthInfo: HealthInfo
     var validationNum = false


}