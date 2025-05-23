package com.example.noteapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.noteapp.app.features.home.presentation.HomeScreen
import com.example.noteapp.app.features.note.presentation.AddNoteScreen
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

@Serializable
data class AddNoteRoute(
    val noteId: Int? = null,
)


@Composable
fun Navigation(
    navController: NavHostController,
) {
    NavHost(navController, startDestination = HomeRoute) {
        composable<HomeRoute> { entry ->
            HomeScreen(
                onAddNoteClicked = {
                    entry.savedStateHandle.remove<Map<String, Any?>?>("result")
                    navController.navigate(route = AddNoteRoute())
                },
                onEdit = {
                    entry.savedStateHandle.remove<Map<String, Any?>?>("result")
                    navController.navigate(route = AddNoteRoute(noteId = it))
                },
            )
        }
        composable<AddNoteRoute> {
            AddNoteScreen(
                onBack = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("result", it)
                    navController.popBackStack()
                },
            )
        }
    }
}