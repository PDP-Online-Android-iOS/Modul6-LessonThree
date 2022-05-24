package dev.ogabek.kotlin.manager

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.ogabek.kotlin.database.NoteDao
import dev.ogabek.kotlin.model.Note

@Database(entities = [Note::class], version = 1)
abstract class RoomManager: RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {

        @Volatile
        private var INSTANCE: RoomManager? = null

        fun getDatabase(context: Context): RoomManager {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext,
                    RoomManager::class.java, "notes.db")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()       // Run in Main Thread
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }

}