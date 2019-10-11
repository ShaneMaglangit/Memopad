package com.example.memopad.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note (
    @PrimaryKey(autoGenerate = true) var noteId: Long = 0L,
    var title: String,
    var description: String,
    var text: String

    // TODO: Add date and time created
)