package com.example.clinic_info_branch.fragments.register_fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.clinic_info_branch.R
import com.example.clinic_info_branch.data_base.HealthInfo

import com.example.clinic_info_branch.fragments.BaseFragment
import com.example.clinic_info_branch.fragments.searching_fragment.*
import com.example.clinic_info_branch.view_model.ViewModel
import kotlinx.android.synthetic.main.fragment_health_info.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HealthInfoFragment : BaseFragment() {

    private lateinit var patientPhone: String
    private lateinit var viewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //create view model instance
        viewModel = ViewModelProvider(activity!!).get(ViewModel::class.java)

        return inflater.inflate(R.layout.fragment_health_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val requestUpdate = arguments?.getInt(REQUEST_UPDATE_HEALTH)

        if (requestUpdate == updateRequestHealth) {
            btnCommitHealth.isEnabled = false
            patientPhone = arguments?.getString(PHONE_FROM_PERSONAL_PAGE).toString()
        } else {
            btnUpdateHealthInfo.isEnabled = false
        }

        //commit data
        btnCommitHealth.setOnClickListener {

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

            //create health info object
            viewModel.healthInfo = HealthInfo(
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

            activity!!.supportFragmentManager.popBackStack()

        }

        //update health info
        btnUpdateHealthInfo.setOnClickListener {

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

            //update patient to database
            GlobalScope.launch(Dispatchers.Default) {

                db?.patientDao()?.updateHealth(
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
            }

            activity!!.supportFragmentManager.popBackStack()

        }
    }
}
