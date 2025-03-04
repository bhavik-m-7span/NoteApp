package com.example.noteapp.app.core.presentation.validator

enum class FormSubmissionStatus {
    INITIAL,
    IN_PROGRESS,
    SUCCESS,
    FAILURE,
    CANCELED,
    ;

    val isInitial get() = this == INITIAL
    val isInProgress get() = this == IN_PROGRESS
    val isSuccess get() = this == SUCCESS
    val isFailure get() = this == FAILURE
    val isCanceled get() = this == CANCELED
    val isInProgressOrSuccess get() = isInProgress || isSuccess
}

abstract class FormInput<T, E>(
    val value: T,
    val isPure: Boolean = true,
) {
    abstract fun validator(value: T): E?

    open val isValid: Boolean get() = validator(value) == null
    val isNotValid: Boolean get() = !isValid
    open val error: E? get() = validator(value)
    val displayError: E? get() = if (isPure) null else error

    /*override fun equals(other: Any?): Boolean =
        other is FormInput<*, *> && other.value == value && other.isPure == isPure

    override fun hashCode(): Int = value.hashCode() + isPure.hashCode()*/
}

object Formz {
    fun validate(inputs: List<FormInput<*, *>>): Boolean = inputs.all { it.isValid }

    fun isPure(inputs: List<FormInput<*, *>>): Boolean = inputs.all { it.isPure }
}
