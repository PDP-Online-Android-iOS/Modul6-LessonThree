package dev.ogabek.kotlin.view

import android.annotation.SuppressLint
import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.ogabek.kotlin.R
import dev.ogabek.kotlin.database.model.Note
import java.text.ParseException
import java.text.SimpleDateFormat

class NotesAdapter(private val context: Context, notesList: List<Note>) :
    RecyclerView.Adapter<NotesAdapter.MyViewHolder>() {
    private val notesList: List<Note>

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var note: TextView
        var dot: TextView
        var timestamp: TextView

        init {
            note = view.findViewById(R.id.note)
            dot = view.findViewById(R.id.dot)
            timestamp = view.findViewById(R.id.timestamp)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.note_list_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val note: Note = notesList[position]
        holder.note.text = note.note

        //Displaying dot from HTML character code
        holder.dot.text = Html.fromHtml("&#8226;")

        // Formatting and displaying timestamp
        holder.timestamp.text = formatData(note.timestamp)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    /*
     *  Formatting timestamp to `MMM d` format
     *  Input: 2022-03-01 22:32:25
     *  Output: Mar 01
     */
    private fun formatData(dateStr: String): String {
        try {
            @SuppressLint("SimpleDateFormat") val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = fmt.parse(dateStr)
            @SuppressLint("SimpleDateFormat") val fmtOUt = SimpleDateFormat("MMM d")
            return fmtOUt.format(date)
        } catch (e: ParseException) {
        }
        return ""
    }

    init {
        this.notesList = notesList
    }
}