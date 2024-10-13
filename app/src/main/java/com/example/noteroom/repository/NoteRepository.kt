package com.example.noteroom.repository

import android.app.Application
import com.example.noteroom.data.Note
import com.example.noteroom.data.NoteDao
import com.example.noteroom.data.NoteDatabase
import kotlinx.coroutines.flow.Flow

class NoteRepository(application: Application) {

    private var noteDao: NoteDao
    private var allNotes: Flow<List<Note>>

    private val database = NoteDatabase.getInstance(application)

    init {
        noteDao = database.noteDao()
        allNotes = noteDao.getAllNotes()
    }

    suspend fun insert(note: Note) {
            noteDao.insert(note)
    }

    suspend fun update(note: Note) {
            noteDao.update(note)
    }

     suspend fun delete(note: Note) {
            noteDao.delete(note)
    }

    fun getAllNotes(): Flow<List<Note>> {
        return allNotes
    }

    fun searchDatabase(searchQuery : String) : Flow<List<Note>>{
        return noteDao.searchDatabase(searchQuery)
    }
}