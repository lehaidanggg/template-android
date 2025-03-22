package com.example.template

import androidx.room.Room
import com.example.template.common.resource.ResourcesProvider
import com.example.template.data.local.AppDatabase
import com.example.template.data.repositories.UseRepository
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module


interface NamedDI {
    companion object {
        const val DEFAULT_DISPATCHER = "defaultDispatcher"
        const val IO_DISPATCHER = "ioDispatcher"
        const val MAIN_DISPATCHER = "mainDispatcher"
        const val MAIN_IMMEDIATE_DISPATCHER = "mainImmediateDispatcher"
    }
}

val appModule = module {

    single { "Hello Koin" }
    //=========================================OTHER================================================
    single(named(NamedDI.DEFAULT_DISPATCHER)) { Dispatchers.Default }
    single(named(NamedDI.IO_DISPATCHER)) { Dispatchers.IO }
    single(named(NamedDI.MAIN_DISPATCHER)) { Dispatchers.Main }
    single(named(NamedDI.MAIN_IMMEDIATE_DISPATCHER)) { Dispatchers.Main.immediate }
    //
    single { ResourcesProvider(get()) }

    //=========================================ROOM=================================================
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            AppDatabase.NAME_DB
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    //=========================================DAO==================================================
    single { get<AppDatabase>().userDao() }

    //=========================================REPOSITORY===========================================
    single<UseRepository> {
        UseRepository(
            userDao = get(),
            dispatcher = get(named(NamedDI.IO_DISPATCHER))
        )
    }

    //=========================================VIEWMODEL============================================
//    viewModel { IntroVM(get()) }
//    viewModel { HomeVM(get()) }

}