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
    fun retrieveAllEvents(callback: AppCallback<List<Event>?>)

    /**
     * Retrieves a news for the specified newsId from the backend in an asynchronous manner.
     *
     * @param newsId id of the news which will be fetched
     * @param callback defined what happens after the data are fetched
     */
    fun retrieveNews(newsId: String, callback: AppCallback<News?>)

}