package cz.mapofnews.service

import java.util.*

/**
 * Class represents an event, which can be viewed byu user.
 * It is connected to some time, type and place. Does not contain
 * the text of the news, this is factored to a class News.
 */
class Event() {

    constructor(objectId: String,
                type: EventType,
                eventDate: Date,
                title: String,
                abstract: String,
                source: String,
                link: String?,
                lat: Double,
                lng: Double,
                newsId: String?) : this() {
        this.objectId = objectId
        this.type = type
        this.eventDate = eventDate
        this.title = title
        this.abstract = abstract
        this.source = source
        this.link = link
        this.lat = lat
        this.lng = lng
        this.newsId = newsId
    }

    lateinit var objectId: String
    lateinit var type: EventType
    lateinit var eventDate: Date
    var title = ""
    var abstract = ""
    var source = ""
    var link: String? = null
    var lat: Double  = 0.0
    var lng: Double  = 0.0
    var newsId: String? = null

    override fun toString(): String {
        return "Event(objectId='$objectId', type=$type, eventDate=$eventDate, title='$title', abstract='$abstract', source='$source', link=$link, lat=$lat, lng=$lng, newsId=$newsId)"
    }

}