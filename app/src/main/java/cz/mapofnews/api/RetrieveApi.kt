package cz.mapofnews.api

import cz.mapofnews.service.Event
import cz.mapofnews.service.News

/**
 * Interface creates contract between service layer of the application and
 * the backend api class for retrieving objects.
 */
interface RetrieveApi {

    /**
     * Retrieves all events from the backend in an asynchronous manner.
     *
     * @param callback defined what happens after the data are fetched
     */
    fun retrieveAllEvents(callback: AppCallback<List<Event>>)

    /**
     * Retrieves a news for the specified event from the backend in an asynchronous manner.
     *
     * @param eventId id of the event, for which news will be fetched
     * @param callback defined what happens after the data are fetched
     */
    fun retrieveNews(eventId: String, callback: AppCallback<News>)

}