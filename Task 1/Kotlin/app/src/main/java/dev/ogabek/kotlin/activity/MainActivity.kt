package dev.ogabek.kotlin.activity

import android.content.ContentValues.TAG
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.ogabek.kotlin.R
import dev.ogabek.kotlin.adapter.NoteAdapter
import dev.ogabek.kotlin.database.NoteRepository
import dev.ogabek.kotlin.model.Note
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    private lateinit var rv_notes: RecyclerView
    private lateinit var btn_add: FloatingActionButton

    private val notes: ArrayList<Note> = ArrayList()
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()


    }

    private fun initViews() {

        val repository = NoteRepository(application)

        notes.addAll(getAllNotes(repository))

        adapter = NoteAdapter(this, notes)
        rv_notes = findViewById(R.id.rv_notes)
        rv_notes.layoutManager = LinearLayoutManager(this)
        rv_notes.adapter = adapter

        btn_add = findViewById(R.id.btn_add)

        btn_add.setOnClickListener {
            val view = layoutInflater.inflate(R.layout.custom_dialog, null)
            val alertDialog = AlertDialog.Builder(this).create()
            alertDialog.setTitle("New Note")
            alertDialog.setCancelable(true)

            val text = view.findViewById<EditText>(R.id.etComments)

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Save") { _, _ ->
                val repository = NoteRepository(application)
                val id = notes.size
                val note = Note(id, text.text.toString())
                saveNote(repository, note)
                notes.add(note)
                adapter.notifyDataSetChanged()
            }

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel") { _, _ ->
                alertDialog.cancel()
            }

            alertDialog.setView(view)
            alertDialog.show()
        }

    }

    private fun saveNote(repository: NoteRepository, note: Note) {
        val executor = Executors.newSingleThreadExecutor()  // in Background
        executor.execute {
            repository.saveNote(note)
        }
    }

    private fun getAllNotes(repository: NoteRepository): List<Note> {
        Log.d(TAG, "getAllNotes: ${repository.getNotes()}")
        return repository.getNotes()
    }

    fun deleteData(repository: NoteRepository) {
        val executor = Executors.newSingleThreadExecutor()  // in Background
        executor.execute {
            repository.deleteData()
        }
    }

}