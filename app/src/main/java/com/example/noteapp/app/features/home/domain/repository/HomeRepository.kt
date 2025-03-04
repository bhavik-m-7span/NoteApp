package com.example.noteapp.app.features.home.domain.repository

import com.example.noteapp.app.core.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getAllNotes(): Flow<List<Note>>

    suspend fun deleteNote(note: Note)
}