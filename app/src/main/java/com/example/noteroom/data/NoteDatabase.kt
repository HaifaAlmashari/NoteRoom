package com.example.noteroom.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        private var instance: NoteDatabase? = null
        private val scope: CoroutineScope? = null

        @Synchronized
        fun getInstance(ctx: Context): NoteDatabase {
            if(instance == null)
                instance = Room.databaseBuilder(ctx.applicationContext, NoteDatabase::class.java,
                    "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()

            return instance!!

        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                instance?.let { database ->
                    scope?.launch {
                        var noteDao = database.noteDao()
                        var note = Note("title", "descriptor")
                        noteDao.insert(note)
                        note = Note("title", "descriptor")
                        noteDao.insert(note)
                        note = Note("title", "descriptor")
                        noteDao.insert(note)
                    }
                }
            }
        }
    }
}