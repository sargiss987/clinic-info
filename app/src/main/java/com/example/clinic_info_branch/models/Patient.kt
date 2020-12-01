package com.example.clinic_info_branch.data_base

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity()
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
    val treatmentProcessList: MutableList<TreatmentProcess>
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
    val stenocardia: String,
    val kidneyDisease: String,
    val bloodDisease: String,
    val gastrointestinalTractDisease: String,
    val respiratoryTractDisease: String,
    val hypertension: String,
    val hypotension: String,
    val thyroidGladDisease: String,
    val nervousMentalDisorders: String,
    val epilepsy: String,
    val diabetes: String,
    val infectionsDiseases: String,
    val neoplasm: String,
    val hepatitis: String,
    val otherLiverDiseases: String,
    val sexuallyTransmittedDiseases: String,
    val skinDiseases: String,
    val otherDiseases: Boolean,
    val otherDiseasesDescription: String,
    val pregnancy: Boolean,
    val duringPregnancy: String

): Parcelable

@Parcelize
data class OralHealth(
    val hygiene: String,
    val typeOfBite: String,
    val stateOfTeeth: MutableList<StateOfTooth>
): Parcelable

@Parcelize
data class StateOfTooth(
    val toothNumber: String,
    val missingTooth: String,
    val caries: String,
    val pulpitis: String,
    val periodontitis: String,
    val root: String,
    val implant: String,
    val rootFilling: String,
    val plaque: String,
    val tartar: String,
    val artCrown: String,
    val toothMobility: String
): Parcelable

@Parcelize
data class TreatmentProcess(
    val visitDate: String,
   val subReasonVisit: String,
   val objReasonVisit: String,
   val diagnosis: String,
   val treatmentPlan: String
): Parcelable

class StateOfToothConverter{

    @TypeConverter
    fun convertJson(list : MutableList<StateOfTooth>): String? {

        return Gson().toJson(list)

    }

    @TypeConverter
    fun convertList(json : String) : MutableList<StateOfTooth>?{

        return Gson().fromJson(json, object : TypeToken<MutableList<StateOfTooth>>() {}.type)
    }
}

class TreatmentProcessConverter{

    @TypeConverter
    fun convertJson(list : MutableList<TreatmentProcess>): String? {

        return Gson().toJson(list)

    }

    @TypeConverter
    fun convertList(json : String) : MutableList<TreatmentProcess>?{

        return Gson().fromJson(json, object : TypeToken<MutableList<TreatmentProcess>>() {}.type)
    }
}

