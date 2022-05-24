package dev.ogabek.kotlin.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.ogabek.kotlin.model.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveNote(note: Note)

    @Query("SELECT * FROM note_table")
    fun getAllNotes(): List<Note>

    @Query("DELETE FROM note_table")
    fun deleteData()

}