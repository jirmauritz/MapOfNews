package cz.mapofnews.api

/**
 * Exception throws when error in communication with backend occurs.
 */
class ApiException(override val message: String?,
                   status: String? = "No objects returned",
                   override val cause: Throwable? = null)
    : Exception()