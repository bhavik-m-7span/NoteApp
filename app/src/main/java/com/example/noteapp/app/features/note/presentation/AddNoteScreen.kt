package com.example.noteapp.app.features.note.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.noteapp.app.core.presentation.validator.FormSubmissionStatus
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddNoteScreen(
    viewModel: NoteViewModel = koinViewModel(),
    onBack: (Map<String, Any?>?) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.status) {
        if (state.status.isSuccess) {
            onBack(mapOf("message" to "Note saved successfully!!"))
        }
    }

    AddNoteComposable(
        state = state,
        onBack = onBack,
        onTitleChange = viewModel::onNoteTitleChange,
        onDescriptionChange = viewModel::onNoteDescChange,
        onSubmit = viewModel::onSubmit,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddNoteComposable(
    state: NoteState = NoteState(),
    onBack: (Map<String, Any?>?) -> Unit = {},
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onTitleChange: (String) -> Unit = {},
    onDescriptionChange: (String) -> Unit = {},
    onSubmit: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Add Note") },
                navigationIcon = {
                    IconButton(onClick = {
                        onBack.invoke(emptyMap())
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            "backIcon"
                        )
                    }
                },
            )
        },
    ) {
        Box(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(it)
                .background(
                    color =
                    Color.LightGray
                        .copy(alpha = 0.3f),
                ),
        ) {
            Column(modifier = Modifier.padding(15.dp)) {
                TextField(
                    value = state.title.value,
                    onValueChange = onTitleChange,
                    label = { Text("Note title") },
                    colors =
                    TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.DarkGray.copy(alpha = 0.5f),
                        focusedLabelColor = Color.DarkGray,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.title.displayError != null,
                    supportingText = {
                        if (state.title.displayError != null) {
                            Text("Please enter valid note title")
                        }
                    },
                )
                Spacer(modifier = Modifier.height(15.dp))
                TextField(
                    value = state.description.value,
                    onValueChange = onDescriptionChange,
                    label = { Text("Note description") },
                    maxLines = 20,
                    minLines = 2,
                    colors =
                    TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.DarkGray.copy(alpha = 0.5f),
                        focusedLabelColor = Color.DarkGray,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.description.displayError != null,
                    supportingText = {
                        if (state.description.displayError != null) {
                            Text("Please enter valid note description")
                        }
                    },
                )
                Spacer(modifier = Modifier.height(15.dp))
                Button(
                    onClick = onSubmit,
                    enabled = state.isValid,
                    shape = RectangleShape,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                ) {
                    Text("Save")
                }
            }
            if (state.status == FormSubmissionStatus.IN_PROGRESS) {
                Box(
                    modifier =
                    Modifier
                        .fillMaxSize()
                        .background(Color.Transparent) // Fully transparent overlay
                        .clickable(enabled = false) {}, // Blocks clicks but does nothing
                )
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Gray,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotePreview() {
    AddNoteComposable()
}
