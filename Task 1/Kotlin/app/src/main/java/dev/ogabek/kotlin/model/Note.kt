package dev.ogabek.kotlin.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
open class Note(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "text")
    val text: String



) {
    override fun toString(): String {
        return "ID : $id  | Text: $text"
    }
}
