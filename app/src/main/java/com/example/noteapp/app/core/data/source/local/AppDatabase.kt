package com.example.noteapp.app.core.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.noteapp.app.core.domain.model.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
