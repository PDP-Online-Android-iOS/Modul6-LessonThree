package dev.ogabek.java.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import dev.ogabek.java.R;
import dev.ogabek.java.adapter.NoteAdapter;
import dev.ogabek.java.database.NoteRepository;
import dev.ogabek.java.model.Note;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv_notes;
    private FloatingActionButton btn_add;

    private List<Note> notes = new ArrayList<Note>();
    private NoteAdapter adapter = new NoteAdapter(this, notes);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NoteRepository repository = new NoteRepository(getApplication());

        try {
            notes.addAll(new GetNotes(repository).execute().get());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        initViews();

    }

    private void initViews() {

        btn_add = findViewById(R.id.btn_add);
        rv_notes = findViewById(R.id.rv_notes);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ui) {

                View view = getLayoutInflater().inflate(R.layout.custom_dialog, null);
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("New Note");
                alertDialog.setCancelable(true);

                EditText text = view.findViewById(R.id.etComments);

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NoteRepository repository = new NoteRepository(getApplication());
                        int id = 0;
                        try {
                            id = new GetNotes(repository).execute().get().size();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }

                        Note note = new Note(id, text.getText().toString());
                        new SaveNote(repository).execute(note);
                        notes.add(note);
                        adapter.notifyDataSetChanged();
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.cancel();
                    }
                });

                alertDialog.setView(view);
                alertDialog.show();

            }
        });

        rv_notes.setLayoutManager(new GridLayoutManager(this, 1));
        rv_notes.setAdapter(adapter);

    }


    // Create new Thread for saving note
    class SaveNote extends AsyncTask<Note, Void, Void> {
        NoteRepository repository;

        SaveNote(NoteRepository repository) {
            this.repository = repository;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            repository.saveNote(notes[0]);
            return null;
        }
    }

    // Create new Thread for getting Notes
    class GetNotes extends AsyncTask<Void, Void, List<Note>> {
        NoteRepository repository;

        GetNotes(NoteRepository repository) {
            this.repository = repository;
        }

        @Override
        protected List<Note> doInBackground(Void... unused) {
            return repository.getNotes();
        }

        @Override
        protected void onPostExecute(List<Note> users) {
            super.onPostExecute(users);
        }
    }

    // Create new Thread for clearing Notes
    class ClearNotes extends AsyncTask<Void, Void, Void> {
        NoteRepository repository;

        ClearNotes(NoteRepository repository) {
            this.repository = repository;
        }

        @Override
        protected Void doInBackground(Void... unused) {
            repository.clearData();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
        }
    }

}