package com.example.noteapp.app.features.home.di

import com.example.noteapp.app.features.home.domain.repository.HomeRepository
import com.example.noteapp.app.features.home.data.repository.HomeRepositoryImpl
import com.example.noteapp.app.features.home.domain.usecase.DeleteNoteUseCase
import com.example.noteapp.app.features.home.domain.usecase.GetNotesUseCase
import com.example.noteapp.app.features.home.domain.usecase.HomeUseCases
import com.example.noteapp.app.features.home.presentation.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    factory<HomeRepository> {
        HomeRepositoryImpl(get())
    }
    factory<HomeUseCases> {
        HomeUseCases(
            getNotesUseCase = GetNotesUseCase(get()),
            deleteNoteUseCase = DeleteNoteUseCase(get())
        )
    }
    viewModel {
        HomeViewModel(get())
    }
}