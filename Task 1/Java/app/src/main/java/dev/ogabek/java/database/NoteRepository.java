package dev.ogabek.java.database;

import android.app.Application;

import java.util.List;

import dev.ogabek.java.manager.RoomManager;
import dev.ogabek.java.model.Note;

public class NoteRepository {

    private NoteDao noteDao;

    public NoteRepository(Application application) {
        RoomManager db = RoomManager.getDatabase(application);
        noteDao = db.noteDao();
    }

    public List<Note> getNotes() {
        return noteDao.getAllNotes();
    }

    public void saveNote(Note note) {
        noteDao.saveData(note);
    }

    public void clearData() {
        noteDao.clearData();
    }

}
