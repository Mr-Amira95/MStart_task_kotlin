package com.example.mstartkotlintask_hussam.Controller.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mstartkotlintask_hussam.model.beans.events.Event
import com.example.mstartkotlintask_hussam.model.beans.events.EventDao

@Database(entities = [Event::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "events_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
