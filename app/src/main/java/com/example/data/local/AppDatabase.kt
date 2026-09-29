package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.model.DailyLog
import com.example.data.model.ExamCalendarEvent
import com.example.data.model.JourneyProfile
import com.example.data.model.MasteryTopic
import com.example.data.model.QuizRecord
import com.example.data.model.StudentAccount
import com.example.data.model.StudyDebtRecord
import com.example.data.model.TaskItem

@Database(
    entities = [
        TaskItem::class,
        JourneyProfile::class,
        QuizRecord::class,
        DailyLog::class,
        StudyDebtRecord::class,
        MasteryTopic::class,
        StudentAccount::class,
        ExamCalendarEvent::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun journeyDao(): JourneyDao
    abstract fun quizDao(): QuizDao
    abstract fun dailyLogDao(): DailyLogDao
    abstract fun studyDebtDao(): StudyDebtDao
    abstract fun masteryDao(): MasteryDao
    abstract fun accountDao(): AccountDao
    abstract fun examCalendarDao(): ExamCalendarDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pathforge_os_master_db"
                ).fallbackToDestructiveMigration(true).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
