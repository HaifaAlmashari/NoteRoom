package com.example.noteroom.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.noteroom.repository.NoteRepository
import com.example.noteroom.data.Note

class NoteViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = NoteRepository(app)
    private val allNotes = repository.getAllNotes()

    suspend fun insert(note: Note) {
        repository.insert(note)
    }

    suspend fun update(note: Note) {
        repository.update(note)
    }

    suspend fun delete(note: Note) {
        repository.delete(note)
    }

    fun getAllNotes(): LiveData<List<Note>> {
        return allNotes.asLiveData()
    }

    fun searchDatabase(searchQuery: String) : LiveData<List<Note>> {
        return repository.searchDatabase(searchQuery).asLiveData()
    }

}