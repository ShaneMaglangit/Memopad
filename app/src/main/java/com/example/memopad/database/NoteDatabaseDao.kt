package com.example.memopad.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDatabaseDao {
    @Insert
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Query("DELETE FROM note_table WHERE noteId = :key")
    fun delete(key: Long)

    @Query("SELECT * FROM note_table WHERE noteId = :key")
    fun getNote(key: Long) : Note?

    @Query("SELECT * FROM note_table ORDER BY noteId")
    fun getAllNotes() : LiveData<List<Note>>

}