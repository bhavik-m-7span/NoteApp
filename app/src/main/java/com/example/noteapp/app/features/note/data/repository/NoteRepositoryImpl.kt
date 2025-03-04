package com.example.noteapp.app.features.note.data.repository

import NoteRepository
import com.example.noteapp.app.core.data.source.local.NoteDao
import com.example.noteapp.app.core.domain.model.Note

class NoteRepositoryImpl(
    private val noteDao: NoteDao,
) : NoteRepository {
    override suspend fun insert(note: Note) = noteDao.insertNote(note)

    override suspend fun update(note: Note) = noteDao.updateNote(note)

    override suspend fun getNote(id: Int): Note? = noteDao.getNote(id)
}