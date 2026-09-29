package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.MasteryTopic
import kotlinx.coroutines.flow.Flow

@Dao
interface MasteryDao {
    @Query("SELECT * FROM mastery_topics WHERE journeyId = :journeyId ORDER BY masteryPercentage ASC")
    fun getAllMasteryTopics(journeyId: Int = 1): Flow<List<MasteryTopic>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMasteryTopic(topic: MasteryTopic): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(topics: List<MasteryTopic>)

    @Update
    suspend fun updateMasteryTopic(topic: MasteryTopic)

    @Query("SELECT * FROM mastery_topics WHERE id = :id LIMIT 1")
    suspend fun getTopicById(id: Long): MasteryTopic?
}
