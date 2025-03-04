package com.example.noteapp.app.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.app.features.home.domain.usecase.HomeUseCases
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val useCase: HomeUseCases,
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state =
        useCase
            .getNotesUseCase()
            .map {
                _state.update { state ->
                    state.copy(
                        notes = it,
                        status = if (it.isNotEmpty()) HomeStatus.LOADED else HomeStatus.EMPTY,
                    )
                }
                _state.value
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                HomeState(),
            )

    private val _uiEvent = MutableSharedFlow<HomeUIEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun deleteNote(noteId: Int) {
        viewModelScope.launch {
            useCase.deleteNoteUseCase(noteId)
            _uiEvent.emit(HomeUIEvent.DeleteSuccess)
        }
    }
}

sealed interface HomeUIEvent {
    data object None : HomeUIEvent

    data object DeleteSuccess : HomeUIEvent
}
