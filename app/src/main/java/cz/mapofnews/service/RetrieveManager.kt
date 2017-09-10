package cz.mapofnews.service

import cz.mapofnews.api.AppCallback
import cz.mapofnews.api.RetrieveApi
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The RetrieveManager contains all methods needed to retrieve
 * data and keeps the data as properties. All data are lost when
 * app is closed.
 */
@Singleton
class RetrieveManager @Inject constructor(private val retrieveApi: RetrieveApi) {

    // all events
    var events = hashMapOf<String, Event>()
    // map of the event ids mapped to the texts of the events (news)
    var news = hashMapOf<String, News>()


    fun fetchAllEvents(afterCallback: AppCallback<List<Event>>) {
        retrieveApi.retrieveAllEvents(object : AppCallback<List<Event>> {
            override fun handleResponse(response: List<Event>) {
                (response as ArrayList<Event>).forEach { event -> events.put(event.objectId, event) }
                afterCallback.handleResponse(response)
            }
        })
    }

    fun fetchNews(eventId: String) {
        retrieveApi.retrieveNews(eventId, object : AppCallback<News> {
            override fun handleResponse(response: News) {
                news.put(eventId, response)
            }
        })
    }
}