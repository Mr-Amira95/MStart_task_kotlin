package com.example.mstartkotlintask_hussam.model.beans.events

import androidx.room.*

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var eventName: String,
    var eventDescription: String,
    var gregorianDate: String,
    var hijriDate: String,
    var serverDatetime: String
)

@Dao
interface EventDao {

    @Query("SELECT id, eventName, eventDescription, gregorianDate, hijriDate, serverDatetime FROM events")
    suspend fun getAll(): List<Event>

    @Query("SELECT id, eventName, eventDescription, gregorianDate, hijriDate, serverDatetime FROM events ORDER BY id ASC LIMIT 1 OFFSET :position")
    suspend fun getEventByPosition(position: Int?): Event?

    @Insert
    suspend fun insert(event: Event)

    @Update
    suspend fun update(event: Event)

    @Delete
    suspend fun delete(event: Event)

}
