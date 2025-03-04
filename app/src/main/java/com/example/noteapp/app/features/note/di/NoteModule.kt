package com.example.noteapp.app.features.note.di

import NoteRepository
import androidx.lifecycle.SavedStateHandle
import com.example.noteapp.app.features.note.presentation.NoteViewModel
import com.example.noteapp.app.features.note.data.repository.NoteRepositoryImpl
import com.example.noteapp.app.features.note.domain.usecase.GetNoteUseCase
import com.example.noteapp.app.features.note.domain.usecase.InsertNoteUseCase
import com.example.noteapp.app.features.note.domain.usecase.NoteUseCases
import com.example.noteapp.app.features.note.domain.usecase.UpdateNoteUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val noteModule = module {
    factory<NoteRepository> {
        NoteRepositoryImpl(get())
    }
    factory<NoteUseCases> {
        NoteUseCases(
            getNoteUseCase = GetNoteUseCase(get()),
            insertNoteUseCase = InsertNoteUseCase(get()),
            updateNoteUseCase = UpdateNoteUseCase(get())
        )
    }
    viewModel { (handle: SavedStateHandle) ->
        NoteViewModel(get(), handle)
    }
}