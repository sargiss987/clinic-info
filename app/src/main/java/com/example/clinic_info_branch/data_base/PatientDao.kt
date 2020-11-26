package com.example.clinic_info_branch.data_base

import androidx.room.*

@Dao
@TypeConverters(StateOfToothConverter::class)
interface PatientDao {
    @Insert
    fun insertPatient(patient: Patient)

    @Delete
    fun deletePatient(patient: Patient)

    @Query("SELECT * FROM patient")
    fun getAllPatients() : MutableList<Patient>

    @Query("UPDATE patient SET hygiene = :p0,typeOfBite = :p1,stateOfTeeth = :p3 WHERE phone= :p4")
    fun updateOralHealth(p0: String,p1 : String,p3 :  MutableList<StateOfTooth>,p4: String)

    @Query("UPDATE patient SET allergy = :p0,allergicManifestation = :p1," +
            "bleeding = :p2,rheumatism = :p3,arthritis = :p4,heartDefect = :p5," +
            "heartAttack = :p6,heartSurgery = :p7,stenocardia = :p8,kidneyDisease = :p9," +
            "bloodDisease = :p10,gastrointestinalTractDisease = :p11,respiratoryTractDisease = :p12," +
            "hypertension = :p13,hypotension = :p14,thyroidGladDisease = :p15,nervousMentalDisorders = :p16," +
            "epilepsy = :p17,diabetes = :p18,infectionsDiseases = :p19,neoplasm = :p20,hepatitis = :p21," +
            "otherLiverDiseases = :p22,sexuallyTransmittedDiseases= :p23,skinDiseases = :p24," +
            "otherDiseases = :p25,otherDiseasesDescription = :p26,pregnancy = :p27,duringPregnancy = :p28")
    fun updateHealth(p0: String,p1 : String,p2 : String,p3: String,p4: String,p5: String,
    p6: String,p7: String,p8: String,p9: String,p10: String,p11: String,p12: String,p13: String,
    p14: String,p15: String,p16: String,p17: String,p18: String,p19: String,p20: String,p21: String,
    p22: String,p23: String,p24: String,p25: Boolean,p26: String,p27: Boolean,p28: String)
}