package com.example.clinic_info_branch.fragments.searching_fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable

import com.example.clinic_info_branch.R
import com.example.clinic_info_branch.data_base.Patient
import kotlinx.android.synthetic.main.fragment_patient_personal_page.view.*


class PatientPersonalPage : Fragment() {

    var patient: Patient? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_patient_personal_page, container, false)
        patient = arguments?.getParcelable(PATIENT_INFO)

        //health info

        var healthInfo: String

        if (patient?.healthInfo?.allergicManifestation!!.isEmpty()) {
            healthInfo = "${patient?.healthInfo?.allergy} ${patient?.healthInfo?.bleeding}"
        } else {
            healthInfo =
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
            healthInfoTxt.equals(healthInfoTxtInitial) -> healthInfoTxt = ""
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
        oralHealthTxt = oralHealthTxt.substring(0, oralHealthTxt.length - 2) + ":"
        oralHealth += oralHealthTxt

        view.txtFullName.text = patient?.patientName
        view.txtDate.text = patient?.patientDate
        view.txtGender.text = patient?.gender
        view.txtPlace.text = patient?.placeOfResidence
        view.txtPhone.text = patient?.phone
        view.txtHealthInfo.text = healthInfo
        view.txtoralHealthInfo.text = oralHealth

        return view

    }


}