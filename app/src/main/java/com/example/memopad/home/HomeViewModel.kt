package com.example.memopad.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memopad.database.Note
import com.example.memopad.database.NoteDatabaseDao
import kotlinx.coroutines.*
import timber.log.Timber

class HomeViewModel(dataSource: NoteDatabaseDao, application: Application) : ViewModel() {

    private val _navigateToNoteFragment = MutableLiveData<Note>()
    val navigateToNoteFragment: LiveData<Note>
        get() = _navigateToNoteFragment

    val database = dataSource
    private var job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    val allNotes = dataSource.getAllNotes()

    fun newNote() {
        uiScope.launch {
            val note = Note()
            insert(note)

            _navigateToNoteFragment.value = getLatestNote()
        }
    }

    fun doneNavigating() {
        _navigateToNoteFragment.value = null
        Timber.i("Done navigating!")
    }

    private suspend fun getLatestNote(): Note {
        return withContext(Dispatchers.IO) {
            var note = database.getLatestNote()
            Timber.i("Latest note retrieved from the database.")
            note
        }
    }

    private suspend fun insert(note: Note) {
        withContext(Dispatchers.IO) {
            database.insert(note)

            Timber.i("New note added to the database.")
        }
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("onCleared started")
        job.cancel()
    }
}
