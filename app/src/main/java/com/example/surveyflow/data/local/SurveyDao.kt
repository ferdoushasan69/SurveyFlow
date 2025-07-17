package com.example.surveyflow.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface SurveyDao {

    @Insert
    suspend fun insertAnswer(answer: SurveyEntity)

    @Query("SELECT * FROM survey_answers")
    suspend fun getAllAnswers(): List<SurveyEntity>

    @Query("DELETE FROM survey_answers")
    suspend fun clearAnswers()

}