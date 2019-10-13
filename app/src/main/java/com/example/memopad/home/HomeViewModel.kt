package com.example.memopad.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memopad.database.Note
import com.example.memopad.database.NoteDatabaseDao
import kotlinx.coroutines.*

class HomeViewModel(dataSource: NoteDatabaseDao, application: Application) : ViewModel() {

    private val _navigateToNoteFragment = MutableLiveData<Note>()
    val navigateToNoteFragment: LiveData<Note>
        get() = _navigateToNoteFragment

    val database = dataSource
    private var job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    val allNotes = dataSource.getAllNotes()

    fun openNote(noteId: Long) {
        uiScope.launch {
            _navigateToNoteFragment.value = getNote(noteId)
        }
    }

    fun newNote() {
        uiScope.launch {
            val note = Note()
            insert(note)

            _navigateToNoteFragment.value = getLatestNote()
        }
    }

    fun removeNote(note: Note) {
        uiScope.launch {
            delete(note)
        }
    }

    fun doneNavigating() {
        _navigateToNoteFragment.value = null
    }

    private suspend fun getNote(noteId: Long): Note {
        return withContext(Dispatchers.IO) {
            val note = database.getNote(noteId)
            note
        }
    }

    private suspend fun getLatestNote(): Note {
        return withContext(Dispatchers.IO) {
            val note = database.getLatestNote()
            note
        }
    }

    private suspend fun insert(note: Note) {
        withContext(Dispatchers.IO) {
            database.insert(note)
        }
    }

    private suspend fun delete(note: Note) {
        withContext(Dispatchers.IO) {
            database.delete(note.noteId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
