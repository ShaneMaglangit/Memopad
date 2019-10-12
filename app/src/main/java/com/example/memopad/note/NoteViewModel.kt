package com.example.memopad.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memopad.database.Note
import com.example.memopad.database.NoteDatabaseDao
import kotlinx.coroutines.*
import timber.log.Timber

class NoteViewModel(private val noteKey: Long = 0L, dataSource: NoteDatabaseDao) : ViewModel() {

    val database = dataSource
    private var job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome

    private lateinit var note: Note

    init {
        uiScope.launch {
            note = getNoteFromDatabase()
        }
    }

    fun doneNavigating() {
        _navigateToHome.value = false
    }

    fun saveChanges(title: String, description: String, text: String) {
        uiScope.launch {
            Timber.i("saveChanges() called")

            note.title = title
            note.description = description
            note.text = text

            update(note)

            _navigateToHome.value = true
        }
    }

    private suspend fun update(note: Note) {
        withContext(Dispatchers.IO) {
            Timber.i("Updating...")
            database.update(note)
        }
    }

    private suspend fun getNoteFromDatabase() : Note {
        return withContext(Dispatchers.IO) {
            var note = database.getNote(noteKey)
            Timber.i("Latest note retrieved from the database.")
            note
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}