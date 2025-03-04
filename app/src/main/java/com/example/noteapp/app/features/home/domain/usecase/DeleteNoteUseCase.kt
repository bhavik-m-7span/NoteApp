package com.example.noteapp.app.features.home.domain.usecase

import com.example.noteapp.app.features.home.domain.repository.HomeRepository

class DeleteNoteUseCase(private val repository: HomeRepository) {

    suspend operator fun invoke(noteId: Int) {
        repository.deleteNote(noteId)
    }
}