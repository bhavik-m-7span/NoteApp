package com.example.noteapp.app.features.home.domain.usecase

import com.example.noteapp.app.features.home.domain.repository.HomeRepository
import com.example.noteapp.app.core.domain.model.Note
import kotlinx.coroutines.flow.Flow

class GetNotesUseCase(private val repository: HomeRepository) {
    operator fun invoke(): Flow<List<Note>> {
        return repository.getAllNotes()
    }
}