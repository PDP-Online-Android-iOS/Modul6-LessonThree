package dev.ogabek.kotlin.database.model

class Note{

    var id: Int = 0
    var note: String = ""
    var timestamp: String = ""

    companion object{
        val TABLE_NAME = "notes"

        val COLUMN_ID = "id"
        val COLUMN_NOTE = "note"
        val COLUMN_TIMESTAMP = "timestamp"

        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_NOTE TEXT, $COLUMN_TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP)"

    }

    constructor()

    constructor(id: Int, note: String, timestamp: String) {
        this.id = id
        this.note = note
        this.timestamp = timestamp
    }


}