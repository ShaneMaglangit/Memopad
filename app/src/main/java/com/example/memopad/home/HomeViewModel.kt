package com.example.memopad.home

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.memopad.database.NoteDatabaseDao

class HomeViewModel(dataSource: NoteDatabaseDao, application: Application) : ViewModel() {
    val database = dataSource

    val allNotes = dataSource.getAllNotes()
}
