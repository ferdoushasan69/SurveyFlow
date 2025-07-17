package com.example.surveyflow.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SurveyEntity::class], version = 1, exportSchema = false)
abstract class SurveyDatabase : RoomDatabase() {
    abstract fun surveyDao(): SurveyDao
}
