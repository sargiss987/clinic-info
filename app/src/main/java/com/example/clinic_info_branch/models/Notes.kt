package com.example.clinic_info_branch.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Notes(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val phone: String,
    val date: String,
    val time: String


){
    var uniqueData: String = ""



}
// {
//
//
//    fun getCalendar(): Calendar =
//
//        Calendar.getInstance().apply {
//            this.timeInMillis = timeInMillis
//        }
//
//}
