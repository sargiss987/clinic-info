package com.example.clinic_info_branch.data_base

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Patient(
    val patientName: String,
    val patientDate: String,
    val gender: String,
    val placeOfResidence: String,
    @PrimaryKey
    val phone: String,
    @Embedded
    val healthInfo: HealthInfo? = null,
    @Embedded
    val oralHealth: OralHealth? = null,
    @Embedded
    val treatmentProcess: TreatmentProcess? = null
): Parcelable

@Parcelize
data class HealthInfo(
    val allergy: String,
    val allergicManifestation: String,
    val bleeding: String,
    val rheumatism: String,
    val arthritis: String,
    val heartDefect: String,
    val heartAttack: String,
    val heartSurgery: String,
    val stenocardia: Boolean,
    val kidneyDisease: Boolean,
    val bloodDisease: Boolean,
    val gastrointestinalTractDisease: Boolean,
    val respiratoryTractDisease: Boolean,
    val hypertension: Boolean,
    val hypotension: Boolean,
    val thyroidGladDisease: Boolean,
    val nervousMentalDisorders: Boolean,
    val epilepsy: Boolean,
    val diabetes: Boolean,
    val infectionsDiseases: Boolean,
    val neoplasm: Boolean,
    val hepatitis: Boolean,
    val otherLiverDiseases: Boolean,
    val sexuallyTransmittedDiseases: Boolean,
    val skinDiseases: Boolean,
    val otherDiseases: Boolean,
    val otherDiseasesDescription: String,
    val pregnancy: Boolean,
    val duringPregnancy: String

): Parcelable

@Parcelize
data class OralHealth(
    val hygiene: String,
    val typeOfBite: Boolean,
    @Embedded
    val stateOfTeeth: StateOfTeeth
): Parcelable

@Parcelize
data class StateOfTeeth(
    val missingTooth: Boolean,
    val caries: Boolean,
    val pulpitis: Boolean,
    val periodontitis: Boolean,
    val root: Boolean,
    val implant: Boolean,
    val rootFilling: Boolean,
    val plaque: Boolean,
    val tartar: Boolean,
    val artCrown: Boolean,
    val toothMobility: Int
): Parcelable

@Parcelize
data class TreatmentProcess(
   val visitDate: String,
   val subReasonVisit: String,
   val objReasonVisit: String,
   val diagnosis: String,
   val treatmentPlan: String

): Parcelable

