package dev.ogabek.kotlin.activity

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.ogabek.kotlin.R
import dev.ogabek.kotlin.adapter.NoteAdapter
import dev.ogabek.kotlin.manager.RealmManager
import dev.ogabek.kotlin.model.Note

class MainActivity : AppCompatActivity() {

    private lateinit var add: FloatingActionButton
    private lateinit var notes: RecyclerView

    private var noteList: ArrayList<Note> = ArrayList()
    val adapter = NoteAdapter(this, noteList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        noteList.addAll(RealmManager.getInstance!!.loadPosts())

        initViews()

    }

    private fun initViews() {
        add = findViewById(R.id.btn_add)
        notes = findViewById(R.id.rv_notes)
        notes.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        notes.adapter = adapter

        add.setOnClickListener {
            val view = layoutInflater.inflate(R.layout.custom_dialog, null)
            val alertDialog = AlertDialog.Builder(this).create()
            alertDialog.setTitle("New Note")
            alertDialog.setCancelable(true)


            val text: EditText = view.findViewById(R.id.etComments)

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Save") { _, _ ->
                val note = Note(RealmManager.getInstance!!.loadPosts().size, text.text.toString())
                noteList.add(note)
                adapter.notifyDataSetChanged()
                RealmManager.getInstance!!.savePost(note)
            }

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel") { _, _ ->
                alertDialog.cancel()
            }

            alertDialog.setView(view)
            alertDialog.show()
        }

    }
}