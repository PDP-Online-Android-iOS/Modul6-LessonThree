package dev.ogabek.kotlin

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

open class Application: Application() {

    override fun onCreate() {
        initRealm()
        super.onCreate()
    }

    private fun initRealm() {
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .allowWritesOnUiThread(true)
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)
    }

}