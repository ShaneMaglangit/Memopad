package com.example.memopad.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.memopad.database.NoteDatabaseDao

class NoteViewModelFactory (
    private val noteKey: Long,
    private val dataSource: NoteDatabaseDao
    ) : ViewModelProvider.Factory {
        @Suppress("unchecked_cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
                return NoteViewModel(noteKey, dataSource) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}