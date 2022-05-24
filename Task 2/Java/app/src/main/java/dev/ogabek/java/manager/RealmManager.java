package dev.ogabek.java.manager;

import java.util.ArrayList;

import dev.ogabek.java.model.Note;
import io.realm.Realm;
import io.realm.RealmResults;

public class RealmManager {

    private static final String TAG = RealmManager.class.toString();

    private static RealmManager realmManager;
    private static Realm realm;

    public RealmManager getInstance() {
        if (realmManager == null) {
            realmManager = new RealmManager();
        }
        return realmManager;
    }

    public RealmManager() {
        realm = Realm.getDefaultInstance();
    }

    public void saveNote(Note note) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(note);
        realm.commitTransaction();
    }

    public ArrayList<Note> getData() {
        RealmResults<Note> result = realm.where(Note.class).findAll();
        return new ArrayList<>(result);
    }

}
