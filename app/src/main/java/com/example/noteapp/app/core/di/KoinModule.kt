package com.example.noteapp.app.core.di


import androidx.room.Room
import com.example.noteapp.app.core.data.source.local.AppDatabase
import com.example.noteapp.app.features.home.di.homeModule
import com.example.noteapp.app.features.note.di.noteModule
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val appModule = module {
    single {
        Room
            .databaseBuilder(
                androidContext(),
                AppDatabase::class.java,
                "notes_db",
            ).build()
    }
    single { get<AppDatabase>().noteDao() }
    includes(homeModule, noteModule)
}
