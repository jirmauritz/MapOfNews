package cz.mapofnews.data

import cz.mapofnews.R

enum class EventType(val resNum: Int) {
    ACCIDENT(R.drawable.accident),
    CRIME(R.drawable.crime),
    DISASTER(R.drawable.disaster)
}