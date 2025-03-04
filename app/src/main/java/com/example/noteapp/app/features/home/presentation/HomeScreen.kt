package com.example.noteapp.app.features.home.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.noteapp.R
import com.example.noteapp.app.core.domain.model.Note
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onAddNoteClicked: () -> Unit,
    onEdit: (Int) -> Unit,
    result: Map<String, Any?>?,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val uiEvent = viewModel.uiEvent
    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        uiEvent.collect { event ->
            when (event) {
                HomeUIEvent.DeleteSuccess -> {
                    snackBarHostState.showSnackbar("Note deleted successfully!")
                }

                HomeUIEvent.None          -> {}
            }
        }
    }
    LaunchedEffect(result) {
        result?.let {
            if (it["message"] != null) {
                snackBarHostState.showSnackbar(it["message"].toString())
            }
        }
    }
    HomeComposable(
        homeSate = state,
        onAddNoteClicked = onAddNoteClicked,
        onEdit = onEdit,
        snackBarHostState = snackBarHostState,
        onDelete = viewModel::deleteNote,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Body(
    modifier: Modifier = Modifier,
    homeSate: HomeState,
    onEdit: (Int) -> Unit,
    onDelete: (Note) -> Unit,
) {
    Box(
        modifier
            .fillMaxSize()
            .background(
                color = Color.LightGray.copy(alpha = 0.3f),
            ),
    ) {
        when (homeSate.status) {
            HomeStatus.LOADING -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(alignment = Alignment.Center),
                    color = Color.Gray,
                )
            }

            HomeStatus.EMPTY   -> Text(
                "No Notes found!!\n Please press ' + ' to create new",
                modifier = Modifier.align(alignment = Alignment.Center),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
            )

            HomeStatus.LOADED  -> CompositionLocalProvider(
                LocalOverscrollConfiguration provides null,
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(top = 6.dp, bottom = 100.dp),
                ) {
                    items(homeSate.notes, key = { it.id }) { note ->
                        NoteItem(
                            note = note,
                            onEdit = onEdit,
                            onDelete = onDelete,
                        )
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeComposable(
    homeSate: HomeState = HomeState(),
    onAddNoteClicked: () -> Unit = {},
    onEdit: (Int) -> Unit = {},
    onDelete: (Note) -> Unit = {},
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Notes") },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNoteClicked,
                containerColor = Color.DarkGray,
                contentColor = Color.White,
                modifier = Modifier.padding(15.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                )
            }
        },
    ) { innerPadding ->
        Body(
            modifier = Modifier.padding(innerPadding),
            homeSate = homeSate,
            onEdit = onEdit,
            onDelete = onDelete,
        )
    }
}

@Composable
private fun NoteItem(
    note: Note,
    onEdit: (Int) -> Unit,
    onDelete: (Note) -> Unit,
) {
    var offsetX by remember { mutableFloatStateOf(0f) }

    AnimatedVisibility(
        visible = true,
        exit =
        shrinkHorizontally(
            animationSpec = tween(durationMillis = 700),
            shrinkTowards = Alignment.Start,
        ) + fadeOut(),
    ) {
        Box(
            modifier =
            Modifier
                .fillMaxSize()
                .height(IntrinsicSize.Min)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (offsetX > -300f) {
                                offsetX = 0f // Reset position if gesture is incomplete
                            }
                        },
                    ) { _, dragAmount ->
                        offsetX = (offsetX + dragAmount).coerceIn(-400f, 0f)
                    }
                }
                .padding(vertical = 6.dp)
                .background(if (offsetX != 0f) Color.LightGray else Color.Transparent),
        ) {
            Row(
                modifier =
                Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier =
                    Modifier
                        .background(Color.Transparent),
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    IconButton(onClick = {
                        onDelete(note)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(26.dp),
                        )
                    }
                }
                Box(modifier = Modifier.width(20.dp))
                Box(
                    modifier =
                    Modifier
                        .background(Color.Transparent),
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    IconButton(onClick = {
                        onEdit.invoke(note.id)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(26.dp),
                        )
                    }
                }
            }
            Box(
                modifier =
                Modifier
                    .offset { IntOffset(offsetX.roundToInt(), 0) },
            ) {
                Card(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    colors =
                    CardDefaults.cardColors(
                        containerColor = Color.White,
                    ),
                    onClick = {
                        onEdit.invoke(note.id)
                    },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_note),
                            contentDescription = "profile",
                            modifier =
                            Modifier
                                .clip(CircleShape)
                                .padding(all = 15.dp)
                                .width(30.dp)
                                .height(30.dp),
                            colorFilter = ColorFilter.tint(color = Color.Gray.copy(alpha = 0.7f)),
                        )
                        Column(modifier = Modifier.padding(vertical = 6.dp)) {
                            Text(
                                text = note.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                            )
                            Text(
                                text = note.description,
                                fontSize = 14.sp,
                                maxLines = 7,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.DarkGray.copy(alpha = 0.7f),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun HomePreview(modifier: Modifier = Modifier) {
    HomeComposable()
}
