package com.example.clinic_info_branch.data_base

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NotesDao {

    @Insert
    fun insertNote(note: Notes)

    @Delete
    fun deleteNote(note: Notes)

    @Query("SELECT * FROM notes")
    fun getAllNotes() : MutableList<Notes>

    @Query("SELECT * FROM notes WHERE date=:currentDate")
    fun getNotesByDate(currentDate : String) : List<Notes>
}