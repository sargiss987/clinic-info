package com.example.clinic_info_branch.data_base

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Notes::class,Patient::class], version = 1)
abstract class ClinicInfo : RoomDatabase() {
    abstract fun notesDao(): NotesDao
    abstract fun patientDao(): PatientDao


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ClinicInfo? = null

        fun getDatabase(context: Context): ClinicInfo {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClinicInfo::class.java,
                    "clinic_info"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}