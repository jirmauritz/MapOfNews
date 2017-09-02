package cz.mapofnews.api

interface AppCallback<in T> {
    fun handleResponse(response: T)
}