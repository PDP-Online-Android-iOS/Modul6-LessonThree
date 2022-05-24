package dev.ogabek.java.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import dev.ogabek.java.R;
import dev.ogabek.java.adapter.NoteAdapter;
import dev.ogabek.java.manager.RealmManager;
import dev.ogabek.java.model.Note;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton add;
    private RecyclerView notes;

    private final ArrayList<Note> noteList = new ArrayList<>();
    NoteAdapter adapter = new NoteAdapter(this, noteList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteList.addAll(new RealmManager().getInstance().getData());

        initViews();

    }

    private void initViews() {
        add = findViewById(R.id.btn_add);
        notes = findViewById(R.id.rv_notes);
        notes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        notes.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View ui = getLayoutInflater().inflate(R.layout.custom_dialog, null);
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("New Note");
                alertDialog.setCancelable(true);

                EditText editText = ui.findViewById(R.id.et_text);

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!editText.getText().toString().isEmpty()) {
                            Note note = new Note(new RealmManager().getInstance().getData().size(), editText.getText().toString());
                            noteList.add(note);
                            adapter.notifyDataSetChanged();
                            new RealmManager().getInstance().saveNote(note);
                        } else {
                            editText.setError("Please Fill it");
                        }
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.cancel();
                    }
                });

                alertDialog.setView(ui);
                alertDialog.show();
            }
        });

    }
}