package cz.mapofnews.service

/**
 * News is on object containing the text of the events.
 * It is refactored to an individual class, because it is expected
 * that the news could be large and it is not neccessarry to load
 * news for all events.
 */
class News {
    lateinit var objectId: String
    var text: String = ""
}