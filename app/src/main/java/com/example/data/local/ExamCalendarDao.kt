package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.ExamCalendarEvent
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamCalendarDao {
    @Query("SELECT * FROM exam_calendar_events ORDER BY daysRemaining ASC")
    fun getAllEvents(): Flow<List<ExamCalendarEvent>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<ExamCalendarEvent>)
}
