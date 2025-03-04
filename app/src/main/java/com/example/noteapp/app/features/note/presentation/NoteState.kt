package com.example.noteapp.app.features.note.presentation

import com.example.noteapp.app.core.presentation.validator.FormSubmissionStatus
import com.example.myapplication.validator.StringValidator

data class NoteState(
    var noteId: Int? = null,
    var title: StringValidator = StringValidator.pure(),
    var description: StringValidator = StringValidator.pure(),
    var isValid: Boolean = false,
    var status: FormSubmissionStatus = FormSubmissionStatus.INITIAL,
)
