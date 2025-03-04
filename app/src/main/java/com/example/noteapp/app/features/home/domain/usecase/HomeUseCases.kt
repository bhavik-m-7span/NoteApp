package com.example.noteapp.app.features.home.domain.usecase

data class HomeUseCases(
    val getNotesUseCase: GetNotesUseCase,
    val deleteNoteUseCase: DeleteNoteUseCase,
)