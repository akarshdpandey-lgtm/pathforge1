package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.StudentAccount
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Query("SELECT * FROM student_accounts WHERE isLoggedIn = 1 LIMIT 1")
    fun getActiveAccount(): Flow<StudentAccount?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAccount(account: StudentAccount)

    @Update
    suspend fun updateAccount(account: StudentAccount)

    @Query("UPDATE student_accounts SET isLoggedIn = 0")
    suspend fun logoutAll()
}
