package dev.ogabek.java.manager;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import dev.ogabek.java.database.NoteDao;
import dev.ogabek.java.model.Note;

@Database(entities = {Note.class}, version = 1)
public abstract class RoomManager extends RoomDatabase {

    public abstract NoteDao noteDao();
    private static RoomManager INSTANCE;

    public static RoomManager getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomManager.class, "notes.db")
                            .fallbackToDestructiveMigration()
//                            .allowMainThreadQueries()           // Run in Main Thread
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
