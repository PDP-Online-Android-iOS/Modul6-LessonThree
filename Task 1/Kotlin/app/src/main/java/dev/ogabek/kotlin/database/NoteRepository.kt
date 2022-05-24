package dev.ogabek.kotlin.database

import android.app.Application
import dev.ogabek.kotlin.manager.RoomManager
import dev.ogabek.kotlin.model.Note

class NoteRepository(application: Application) {

    private val db = RoomManager.getDatabase(application)
    private val noteDao: NoteDao = db.noteDao()

    fun getNotes(): List<Note> {
        return noteDao.getAllNotes()
    }

    fun saveNote(note: Note) {
        noteDao.saveNote(note)
    }

    fun deleteData() {
        noteDao.deleteData()
    }

}