package com.example.noteapp.app.features.note.domain.usecase

import NoteRepository
import com.example.noteapp.app.core.domain.model.Note

class GetNoteUseCase(private val repository: NoteRepository) {

    suspend operator fun invoke(noteId: Int): Note? {
        return repository.getNote(noteId)

    }
}