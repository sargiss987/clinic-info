package com.example.clinic_info_branch.data_base

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Notes::class,Patient::class], version = 1)
abstract class ClinicInfo : RoomDatabase() {
    abstract fun notesDao(): NotesDao
    abstract fun patientDao(): PatientDao

}