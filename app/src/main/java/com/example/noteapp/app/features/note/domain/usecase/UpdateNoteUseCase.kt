package com.example.noteapp.app.features.note.domain.usecase

import NoteRepository
import com.example.noteapp.app.core.domain.model.Note

class UpdateNoteUseCase(private val repository: NoteRepository) {

    suspend operator fun invoke(note: Note) {
        repository.update(note)
    }
}