package com.example.memopad.note

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.memopad.database.Note
import com.example.memopad.database.NoteDatabaseDao
import kotlinx.coroutines.*

class NoteViewModel(dataSource: NoteDatabaseDao, application: Application) : ViewModel() {

    val database = dataSource
    private var job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    fun addNote(title: String, description: String, text: String) {
        uiScope.launch {
            val note = Note(title=title, description=description, text=text)
            insert(note)
        }
    }

    private suspend fun insert(note: Note) {
        withContext(Dispatchers.IO) {
            database.insert(note)
        }

    }
}
