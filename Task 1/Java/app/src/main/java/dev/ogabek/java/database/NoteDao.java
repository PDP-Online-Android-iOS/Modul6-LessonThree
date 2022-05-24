package dev.ogabek.java.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import dev.ogabek.java.model.Note;

@Dao
public interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long saveData(Note note);

    @Query("SELECT * FROM note_table")
    List<Note> getAllNotes();

    @Query("DELETE FROM note_table")
    void clearData();

}
