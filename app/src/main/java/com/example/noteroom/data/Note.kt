package com.example.noteroom.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(val title: String,
                val description: String,
                @PrimaryKey(autoGenerate = false) val id: Int? = null)