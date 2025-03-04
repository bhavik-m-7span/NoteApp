package com.example.noteapp.app.core.presentation.validator

enum class StringValidationError {
    INVALID,
}

class StringValidator private constructor(
    value: String,
    isPure: Boolean,
) : FormInput<String, StringValidationError>(value, isPure) {
    companion object {
        fun pure(value: String = "") = StringValidator(value, true)

        fun dirty(value: String = "") = StringValidator(value, false)
    }

    override fun validator(value: String): StringValidationError? =
        if (value.trim().isNotEmpty()) {
            null
        } else {
            StringValidationError.INVALID
        }
}
