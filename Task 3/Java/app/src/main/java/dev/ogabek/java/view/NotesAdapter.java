package dev.ogabek.java.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import dev.ogabek.java.R;
import dev.ogabek.java.database.model.Note;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

    private Context context;
    private List<Note> notesList;
    
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView note, dot, timestamp;

        public MyViewHolder(@NonNull View view) {
            super(view);
            
            note = view.findViewById(R.id.note);
            dot = view.findViewById(R.id.dot);
            timestamp = view.findViewById(R.id.timestamp);
            
        }
    }

    public NotesAdapter(Context context, List<Note> notesList) {
        this.context = context;
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Note note = notesList.get(position);
        
        holder.note.setText(note.getNote());
        
        //Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"));
        
        // Formatting and displaying timestamp
        holder.timestamp.setText(formatData(note.getTimestamp()));
        
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    /*
     *  Formatting timestamp to `MMM d` format
     *  Input: 2022-03-01 22:32:25
     *  Output: Mar 01
     */
    
    private String formatData(String dateStr) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat fmtOUt = new SimpleDateFormat("MMM d");
            return fmtOUt.format(date);
        } catch (ParseException e) {
            
        }
        return "";
    }

}
