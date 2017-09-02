package cz.mapofnews.service

import cz.mapofnews.R

/**
 * Type of an event, each type has its own icon on the map.
 */
enum class EventType(val resNum: Int) {
    ACCIDENT(R.drawable.accident),
    CRIME(R.drawable.crime),
    DISASTER(R.drawable.disaster)
}