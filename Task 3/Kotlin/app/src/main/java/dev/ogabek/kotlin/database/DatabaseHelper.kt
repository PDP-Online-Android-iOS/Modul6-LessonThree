package dev.ogabek.kotlin.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import dev.ogabek.kotlin.database.model.Note

class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    // Creating Table
    override fun onCreate(db: SQLiteDatabase) {

        // Create Notes Table
        db.execSQL(Note.CREATE_TABLE)
    }

    // Upgrading Database
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_NAME)

        // Create Tables Again
        onCreate(db)
    }

    fun insertNote(note: String?): Long {
        // Get writable database as we want to write data
        val db = this.writableDatabase
        val values = ContentValues()
        // `id` and `timestamp` will be inserted automatically
        // no need to add them
        values.put(Note.COLUMN_NOTE, note)

        // Insert Row
        val id = db.insert(Note.TABLE_NAME, null, values)

        // Close db connection
        db.close()

        // Return newly inserted row id
        return id
    }

    fun getNote(id: Long): Note {
        // Get Readable Database as we are not inserting anything
        val db = this.readableDatabase
        val cursor = db.query(Note.TABLE_NAME, arrayOf(Note.COLUMN_ID, Note.COLUMN_NOTE, Note.COLUMN_TIMESTAMP), Note.COLUMN_ID + "=?", arrayOf(id.toString()), null, null, null, null)
        cursor.moveToFirst()

        // Prepare note object
        @SuppressLint("Range") val note = Note(
            cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)),
            cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)),
            cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP))
        )

        // Close the db connection
        cursor.close()
        return note
    }

    @SuppressLint("Range")
    fun allNotes(): List<Note> {
        val notes: MutableList<Note> = ArrayList<Note>()

        // Select All Query
        val selectQuery = "SELECT * FROM " + Note.TABLE_NAME.toString() + " ORDER BY " +
                Note.COLUMN_TIMESTAMP.toString() + " DESC"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                val note = Note()
                note.id = (cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)))
                note.note = (cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)))
                note.timestamp = (cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP)))
                notes.add(note)
            } while (cursor.moveToNext())
        }

        // Close db connection
        db.close()

        // Return notes list
        return notes
    }


    // Count Query
    fun notesCount(): Int {
        // Count Query
        val countQuery = "SELECT * FROM " + Note.TABLE_NAME
        val db = this.readableDatabase
        val cursor = db.rawQuery(countQuery, null)

        // Get Count
        val count = cursor.count

        // Close cursor connection
        cursor.close()

        // Return Count
        return count
    }


    fun updateNote(note: Note): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(Note.COLUMN_NOTE, note.note)

        // Updating row
        return db.update(Note.TABLE_NAME, values, Note.COLUMN_ID + " = ?", arrayOf(java.lang.String.valueOf(note.id)))
    }

    fun deleteNote(note: Note) {
        val db = this.writableDatabase

        // Deleting Note
        db.delete(Note.TABLE_NAME, Note.COLUMN_ID + " = ?", arrayOf(java.lang.String.valueOf(note.id)))

        // Close Database Connection
        db.close()
    }

    companion object {
        // Database Version
        private const val DATABASE_VERSION = 1

        // Database Name
        private const val DATABASE_NAME = "notes_db"
    }
}