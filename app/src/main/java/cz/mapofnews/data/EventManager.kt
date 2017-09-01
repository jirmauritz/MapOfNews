package cz.mapofnews.data

class EventManager {

    companion object {

        fun getAllEvents(): Array<Event> {
            // fake creation of some events
            val e1 = Event(1, "Bouracka", EventType.ACCIDENT, 50.0, 16.0, "Srážka dvou aut.")
            val e2 = Event(2, "Vrazda", EventType.CRIME, 49.5, 15.9, "Vražda.")
            val e3 = Event(7, "Strom spadl", EventType.DISASTER, 49.3, 15.8, "Spadl strom na auto.")

            return arrayOf(e1, e2, e3)
        }

        fun getById(id: Long): Event? {
            val e1 = Event(1, "Bouracka", EventType.ACCIDENT, 50.0, 16.0, "Srážka dvou aut.")
            val e2 = Event(2, "Vrazda", EventType.CRIME, 49.5, 15.9, "Vražda.")
            val e3 = Event(7, "Strom spadl", EventType.DISASTER, 49.3, 15.8, "Spadl strom na auto.")
            val hashMap = hashMapOf<Long, Event>(Pair(1,e1), Pair(2, e2), Pair(7, e3))
            return hashMap[id]
        }
    }
}