package com.example.clinic_info_branch.data_base

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PatientDao {
    @Insert
    fun insertPatient(patient: Patient)

    @Delete
    fun deletePatient(patient: Patient)

    @Query("SELECT * FROM patient")
    fun getAllPatients() : List<Patient>
}