package dev.ogabek.java.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dev.ogabek.java.R;
import dev.ogabek.java.model.Note;

public class NoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<Note> notes;

    public NoteAdapter(Context context, List<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Note note = notes.get(position);

        if (holder instanceof NoteViewHolder) {
            ((NoteViewHolder) holder).text.setText(note.getText());
        }

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    private static class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView text;

        public NoteViewHolder(View view) {
            super(view);

            text = view.findViewById(R.id.text);

        }
    }
}
