package com.example.noteapp.app.features.home.presentation

import com.example.noteapp.app.core.domain.model.Note

enum class HomeStatus {
    LOADING,
    EMPTY,
    LOADED,
}

data class HomeState(
    var status: HomeStatus = HomeStatus.LOADING,
    val notes: List<Note> = emptyList(),
)
