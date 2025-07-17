package com.example.surveyflow.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.surveyflow.data.model.Record

@Entity(tableName = "survey_answers")
data class SurveyEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val questionId: String,
    val questionType: String,
    val answer: String
)


