package com.example.clinic_info_branch.data_base

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.clinic_info_branch.models.Notes

@Dao
interface NotesDao {

    @Insert
    fun insertNote(note: Notes): Long

    @Delete
    fun deleteNote(note: Notes)

    @Query("SELECT * FROM notes")
    fun getAllNotes() : MutableList<Notes>

    @Query("SELECT * FROM notes WHERE date=:currentDate")
    fun getNotesByDate(currentDate : String) : List<Notes>

    @Query("SELECT * FROM notes WHERE date = :p0")
    fun getNotes(p0: String) : MutableList<Notes>
}