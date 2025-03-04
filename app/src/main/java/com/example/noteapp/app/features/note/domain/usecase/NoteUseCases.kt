package com.example.noteapp.app.features.note.domain.usecase

data class NoteUseCases(
    val getNoteUseCase: GetNoteUseCase,
    val insertNoteUseCase: InsertNoteUseCase,
    val updateNoteUseCase: UpdateNoteUseCase,
)