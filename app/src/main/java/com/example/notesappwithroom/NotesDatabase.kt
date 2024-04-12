package com.example.notesappwithroom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class], version = 1)
abstract class NotesDatabase : RoomDatabase() {

    // Notes Database Defined
    abstract fun noteDao(): NoteDao

    companion object { // Singleton pattern implementation

        @Volatile // As soon as value change all threads get to know
        private var INSTANCE: NotesDatabase? = null

        fun getNotesDatabase(context: Context): NotesDatabase {
            if (INSTANCE == null) {
                // Thread locking so multiple threads do not create multiple instances
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext, NotesDatabase::class.java,
                        "notesDB"
                    ).build()
                }

            }
            return INSTANCE!!
        }
    }

}