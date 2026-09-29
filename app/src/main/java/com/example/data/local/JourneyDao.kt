package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.JourneyProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface JourneyDao {
    @Query("SELECT * FROM journey_profile WHERE id = 1 LIMIT 1")
    fun getProfile(): Flow<JourneyProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: JourneyProfile)

    @Update
    suspend fun updateProfile(profile: JourneyProfile)
}
