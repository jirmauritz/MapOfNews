package cz.mapofnews.data

class Event(val id: Long,
            val title: String,
            val type: EventType,
            val lat: Double,
            val lng: Double,
            val abstract: String,
            val source: String,
            val text: String?) {
}