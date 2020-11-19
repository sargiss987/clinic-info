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
}