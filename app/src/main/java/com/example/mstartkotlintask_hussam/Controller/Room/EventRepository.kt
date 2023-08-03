package com.example.mstartkotlintask_hussam.Controller.Room

import com.example.mstartkotlintask_hussam.model.beans.events.Event
import com.example.mstartkotlintask_hussam.model.beans.events.EventDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventRepository(private val eventDao: EventDao) {
    suspend fun insert(event: Event) = withContext(Dispatchers.IO) {
        eventDao.insert(event)
    }

    suspend fun update(event: Event) = withContext(Dispatchers.IO) {
        eventDao.update(event)
    }

    suspend fun delete(event: Event) = withContext(Dispatchers.IO) {
        eventDao.delete(event)
    }

    suspend fun getAll(): List<Event> = withContext(Dispatchers.IO) {
        eventDao.getAll()
    }

    suspend fun getByPosition(position: Int): Event = withContext(Dispatchers.IO) {
        eventDao.getEventByPosition(position)!!
    }
}
