package com.example.memopad.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.memopad.database.Note
import com.example.memopad.database.NoteDatabaseDao
import kotlinx.coroutines.*

class NoteViewModel(private val noteKey: Long = 0L, dataSource: NoteDatabaseDao) : ViewModel() {

    val database = dataSource
    private var job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome

    private val _note = MutableLiveData<Note>()
    val note: LiveData<Note>
        get() = _note

    init {
        uiScope.launch {
            _note.value = getNoteFromDatabase()
        }
    }

    fun doneNavigating() {
        _navigateToHome.value = false
    }

    fun saveChanges(title: String, description: String, text: String) {
        uiScope.launch {
            _note.value?.title = title
            _note.value?.description = description
            _note.value?.text = text

            update(note.value!!)

            _navigateToHome.value = true
        }
    }

    private suspend fun update(note: Note) {
        withContext(Dispatchers.IO) {
            database.update(note)
        }
    }

    private suspend fun getNoteFromDatabase() : Note {
        return withContext(Dispatchers.IO) {
            val note = database.getNote(noteKey)
            note
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}