package com.example.clinic_info_branch.data_base

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Notes(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val phone: String,
    val date: String,
    //val timeInMillis: Long,
    val time: String


)// {
//
//
//    fun getCalendar(): Calendar =
//
//        Calendar.getInstance().apply {
//            this.timeInMillis = timeInMillis
//        }
//
//}
