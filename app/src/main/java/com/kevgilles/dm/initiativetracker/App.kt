package com.kevgilles.dm.initiativetracker

import android.app.Application
import androidx.room.Room
import com.kevgilles.dm.initiativetracker.database.AppDatabase

class App: Application() {
    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, AppDatabase::class.java, "init_database")
            .build()
    }
}