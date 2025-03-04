package com.example.noteapp.app.features.note.domain.usecase

import NoteRepository
import com.example.noteapp.app.core.domain.model.Note

class InsertNoteUseCase(private val repository: NoteRepository) {

    suspend operator fun invoke(note: Note) {
        repository.insert(note)
    }
}