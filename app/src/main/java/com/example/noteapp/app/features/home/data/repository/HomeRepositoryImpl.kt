package com.example.noteapp.app.features.home.data.repository

import com.example.noteapp.app.features.home.domain.repository.HomeRepository
import com.example.noteapp.app.core.data.source.local.NoteDao
import com.example.noteapp.app.core.domain.model.Note
import kotlinx.coroutines.flow.Flow

class HomeRepositoryImpl(
    private val noteDao: NoteDao,
) : HomeRepository {
    override fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()

    override suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)
}