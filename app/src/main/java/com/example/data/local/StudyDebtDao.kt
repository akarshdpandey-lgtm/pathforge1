package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.StudyDebtRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyDebtDao {
    @Query("SELECT * FROM study_debt_records WHERE journeyId = :journeyId ORDER BY id DESC")
    fun getAllDebtRecords(journeyId: Int = 1): Flow<List<StudyDebtRecord>>

    @Query("SELECT SUM(debtMinutes - recoveredMinutes) FROM study_debt_records WHERE isResolved = 0 AND journeyId = :journeyId")
    fun getTotalPendingDebtMinutes(journeyId: Int = 1): Flow<Int?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDebtRecord(record: StudyDebtRecord): Long

    @Update
    suspend fun updateDebtRecord(record: StudyDebtRecord)

    @Query("UPDATE study_debt_records SET recoveredMinutes = recoveredMinutes + :recovered, isResolved = CASE WHEN recoveredMinutes + :recovered >= debtMinutes THEN 1 ELSE 0 END WHERE isResolved = 0 AND journeyId = :journeyId")
    suspend fun applyDebtRecovery(recovered: Int, journeyId: Int = 1)
}
