package com.example.noteroom.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("delete from note_table")
    fun deleteAllNotes()

    @Query("select * from note_table")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note_table WHERE title LIKE :searchQuery OR description LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): Flow<List<Note>>
}