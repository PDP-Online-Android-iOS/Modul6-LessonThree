package dev.ogabek.kotlin.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Note : RealmObject {

    @PrimaryKey
    var id: Int = 0
    var text: String? = null

    constructor()

    constructor(id: Int, text: String) {
        this.id = id
        this.text = text
    }

}