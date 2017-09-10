package cz.mapofnews.backendless

import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import cz.mapofnews.api.ApiException
import cz.mapofnews.api.AppCallback
import cz.mapofnews.api.RetrieveApi
import cz.mapofnews.service.Event
import cz.mapofnews.service.News
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of RetrieveApiComponent for Backendless service.
 */
@Singleton
class RetrieveApiBackendless @Inject constructor() : RetrieveApi {

    override fun retrieveAllEvents(callback: AppCallback<List<Event>>) {
        Backendless.Persistence.of(Event::class.java).find(createAsyncCallback(callback))
    }

    override fun retrieveNews(eventId: String, callback: AppCallback<News>) {
        Backendless.Persistence.of(News::class.java).findById(eventId, createAsyncCallback(callback))
    }

    private fun <T> createAsyncCallback(appCallback: AppCallback<T>): AsyncCallback<T> {
        return object : AsyncCallback<T> {
            override fun handleFault(fault: BackendlessFault?) {
                throw ApiException("Failed to retrieve objects from server: ${fault?.detail}", fault?.code)
            }

            override fun handleResponse(response: T?) {
                if (response == null) {
                    throw ApiException("The response is null.")
                }
                appCallback.handleResponse(response)
            }
        }
    }
}
