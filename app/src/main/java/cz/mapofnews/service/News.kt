package cz.mapofnews.service

/**
 * News is on object containing the text of the events.
 * It is refactored to an individual class, because it is expected
 * that the news could be large and it is not neccessarry to load
 * news for all events.
 */
class News() {

    constructor(objectId: String, text: String) : this() {
        this.objectId = objectId
        this.text = text
    }

    lateinit var objectId: String
    var text: String = ""
}