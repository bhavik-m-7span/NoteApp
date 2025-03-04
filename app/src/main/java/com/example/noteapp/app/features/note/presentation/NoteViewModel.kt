package com.example.noteapp.app.features.note.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.noteapp.app.core.presentation.validator.StringValidator
import com.example.noteapp.app.core.domain.model.Note
import com.example.noteapp.navigation.AddNoteRoute
import com.example.noteapp.app.core.presentation.validator.FormSubmissionStatus
import com.example.noteapp.app.core.presentation.validator.Formz
import com.example.noteapp.app.features.note.domain.usecase.NoteUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// EOD 27.02
// 1. Implemented Room db to store notes
// 2. Injected into di to access in repo and viewmodel
// 3. CRUD operation for Notes
// 4. Implemented Note Listing and Creating UI and functionality
// 5. Added Note Edit/Delete functionality

// EOD 28.02
// 1. Implemented one time ui events with SharedFlow
// 2. Added callbacks wherever needed
// 3. Handling BackStack entry data with SavedStateHandle
// 4. Added UIEvent wherever necessary and handled otherwises with status
// 5. Created Formz(dart library) like functionality in Jetpack kotlin

// EOD 03.03
// 1. Improved Formz functionality
// 2. Working on Clean architecture with MVI

// EOD 04.03
// 1. Implemented Clean Architecture with MVVM in NoteApp
// 2. Separated each functionality into usecases and repo
// 3. Separated di modules into each feature modules
// 4. Updated Delete Note functionality

class NoteViewModel(
    private val useCase: NoteUseCases,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _noteId: Int? = savedStateHandle.toRoute<AddNoteRoute>().noteId
    val noteId = _noteId

    private val _state = MutableStateFlow(NoteState())
    val state = _state.asStateFlow()

    init {
        fetchNote()
    }

    private fun fetchNote() {
        if (_noteId != null) {
            viewModelScope.launch {
                val noteToEdit = useCase.getNoteUseCase(_noteId)
                if (noteToEdit != null) {
                    val title = StringValidator.pure(noteToEdit.title)
                    val description = StringValidator.pure(noteToEdit.description)
                    _state.update {
                        it.copy(
                            noteId = noteToEdit.id,
                            title = title,
                            description = description,
                        )
                    }
                }
            }
        }
    }

    fun onNoteTitleChange(titleText: String) {
        val title = StringValidator.dirty(titleText)
        _state.update {
            it.copy(
                title = title,
                isValid = Formz.validate(listOf(title, it.description)),
            )
        }
    }

    fun onNoteDescChange(descriptionText: String) {
        val description = StringValidator.dirty(descriptionText)
        _state.update {
            it.copy(
                description = description,
                isValid = Formz.validate(listOf(it.title, description)),
            )
        }
    }

    fun onSubmit() {
        val title = StringValidator.dirty(state.value.title.value)
        val description = StringValidator.dirty(state.value.description.value)
        val isValid = Formz.validate(listOf(title, description))
        if (isValid) {
            viewModelScope.launch {
                _state.update { it.copy(status = FormSubmissionStatus.IN_PROGRESS) }
                if (_state.value.noteId == null) {
                    useCase.insertNoteUseCase(
                        Note(
                            title = title.value,
                            description = description.value,
                        ),
                    )
                } else {
                    useCase.updateNoteUseCase(
                        Note(
                            id = requireNotNull(_state.value.noteId),
                            title = title.value,
                            description = description.value,
                        ),
                    )
                }
                _state.update { it.copy(status = FormSubmissionStatus.SUCCESS) }
            }
        } else {
            _state.update { it.copy(status = FormSubmissionStatus.FAILURE) }
        }
    }
}
