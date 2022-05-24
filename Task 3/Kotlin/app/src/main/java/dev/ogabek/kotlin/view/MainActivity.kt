package dev.ogabek.kotlin.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.ogabek.kotlin.R
import dev.ogabek.kotlin.database.DatabaseHelper
import dev.ogabek.kotlin.database.model.Note
import dev.ogabek.kotlin.utils.MyDividerItemDecoration
import dev.ogabek.kotlin.utils.RecyclerTouchListener

class MainActivity : AppCompatActivity() {
    private var mAdapter: NotesAdapter? = null
    private val notesList: MutableList<Note> = ArrayList<Note>()
    private var coordinatorLayout: CoordinatorLayout? = null
    private var recyclerView: RecyclerView? = null
    private var noNotesView: TextView? = null
    private var db: DatabaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        coordinatorLayout = findViewById(R.id.coordinator_layout)
        recyclerView = findViewById(R.id.recycler_view)
        noNotesView = findViewById(R.id.empty_notes_view)
        db = DatabaseHelper(this)
        notesList.addAll(db!!.allNotes())
        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { showNoteDialog(false, null, -1) }
        mAdapter = NotesAdapter(this, notesList)
        val mLayoutManager: LayoutManager = LinearLayoutManager(applicationContext)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.addItemDecoration(
            MyDividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL,
                16
            )
        )
        recyclerView!!.adapter = mAdapter
        toggleEmptyNotes()

        /*
         *  On Long press on RecyclerView item, open alert dialog
         * with option to choose
         * Edit and Delete
         */
        recyclerView!!.addOnItemTouchListener(
            RecyclerTouchListener(this,
                recyclerView!!, object : RecyclerTouchListener.ClickListener {
                    override fun onClick(view: View, position: Int) {

                    }

                    override fun onLongClick(view: View, position: Int) {
                        showActionsDialog(position)
                    }
                })
        )
    }

    /*
     *  Inserting new note in db
     * and refreshing list
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun createNote(note: String) {
        // Inserting note in db and getting
        // newly inserted note id
        val id: Long = db!!.insertNote(note)

        // Get the newly inserted note from db
        val n: Note = db!!.getNote(id)
        // Adding new note to array list at 0 position
        notesList.add(0, n)
        mAdapter!!.notifyDataSetChanged()
        toggleEmptyNotes()
    }

    /*
     *  Updating note in db and updating
     * item in the list by its position
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun updateNote(note: String, position: Int) {
        val n: Note = notesList[position]

        // Updating note text
        n.note = note

        // Updating note in db
        db!!.updateNote(n)

        // Refreshing the list
        notesList[position] = n
        mAdapter!!.notifyDataSetChanged()
        toggleEmptyNotes()
    }

    /*
     *  Deleting note from SQLite and removing the
     *  item from the list by its position
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun deleteNote(position: Int) {
        // Deleting the note from db
        db!!.deleteNote(notesList[position])

        // Removing the note from the list
        notesList.removeAt(position)
        mAdapter!!.notifyDataSetChanged()
        toggleEmptyNotes()
    }

    /*
     *  Opens dialog with Edit - Delete options
     *  Edit - 0
     *  Delete - 0
     */
    private fun showActionsDialog(position: Int) {
        val colors = arrayOf<CharSequence>("Edit", "Delete")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose option")
        builder.setItems(colors) { dialog, which ->
            if (which == 0) {
                showNoteDialog(true, notesList[position], position)
            } else {
                deleteNote(position)
            }
        }
        builder.show()
    }

    /*
     *  Shows alert dialog with EditText options to enter / edit
     *  a note.
     *  When shouldUpdate=true, it automatically displays old and changes the
     *  bottom text to UPDATE
     */
    private fun showNoteDialog(shouldUpdate: Boolean, note: Note?, position: Int) {
        val layoutInflaterAndroid = LayoutInflater.from(applicationContext)
        val view = layoutInflaterAndroid.inflate(R.layout.note_dialog, null)
        val alertDialogBuilderUserInput = AlertDialog.Builder(this@MainActivity)
        alertDialogBuilderUserInput.setView(view)
        val inputNote = view.findViewById<EditText>(R.id.note)
        val dialogTitle = view.findViewById<TextView>(R.id.dialog_title)
        dialogTitle.text =
            if (!shouldUpdate) getString(R.string.lbl_new_note_title) else getString(R.string.lbl_edit_note_title)
        if (shouldUpdate && note != null) {
            inputNote.setText(note.note)
        }
        alertDialogBuilderUserInput
            .setCancelable(false)
            .setPositiveButton(
                if (shouldUpdate) "update" else "save"
            ) { dialogBox, id -> }
            .setNegativeButton(
                "cancel"
            ) { dialogBox, id -> dialogBox.cancel() }
        val alertDialog = alertDialogBuilderUserInput.create()
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
            View.OnClickListener {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputNote.text.toString())) {
                    Toast.makeText(this@MainActivity, "Enter note!", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                } else {
                    alertDialog.dismiss()
                }

                // check if user updating note
                if (shouldUpdate && note != null) {
                    // update note by it's id
                    updateNote(inputNote.text.toString(), position)
                } else {
                    // create new note
                    createNote(inputNote.text.toString())
                }
            })
    }

    /**
     * Toggling list and empty notes view
     */
    private fun toggleEmptyNotes() {
        // you can check notesList.size() > 0
        if (db!!.notesCount() > 0) {
            noNotesView!!.visibility = View.GONE
        } else {
            noNotesView!!.visibility = View.VISIBLE
        }
    }
}