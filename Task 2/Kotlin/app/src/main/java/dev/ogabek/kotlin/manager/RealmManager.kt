package dev.ogabek.kotlin.manager

import android.util.Log
import dev.ogabek.kotlin.model.Note
import io.realm.Realm
import io.realm.RealmResults

class RealmManager {

    private val TAG = RealmManager::class.java.simpleName

    companion object {
        var realmManager: RealmManager? = null
        private lateinit var realm: Realm
        val getInstance: RealmManager?
            get() {
                if (realmManager == null) {
                    realmManager = RealmManager()
                }
                return realmManager
            }
    }

    init {
        realm = Realm.getDefaultInstance()
    }

    fun savePost(note: Note) {
        realm.beginTransaction()
        realm.copyToRealmOrUpdate(note)
        realm.commitTransaction()
    }

    fun loadPosts(): ArrayList<Note> {
        val result: RealmResults<Note> = realm.where(Note::class.java).findAll()
        return ArrayList(result)
    }

}