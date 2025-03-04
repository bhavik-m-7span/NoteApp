package com.example.noteapp.app.features.home.domain.usecase

import com.example.noteapp.app.features.home.domain.repository.HomeRepository
import com.example.noteapp.app.core.domain.model.Note

class DeleteNoteUseCase(private val repository: HomeRepository) {

    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}