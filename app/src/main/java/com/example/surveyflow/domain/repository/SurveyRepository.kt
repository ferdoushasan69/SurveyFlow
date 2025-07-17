package com.example.surveyflow.domain.repository

import com.example.surveyflow.data.model.Record
import com.example.surveyflow.data.model.SurveyResponse
import com.example.surveyflow.data.source.local.SurveyEntity
import com.example.surveyflow.utils.Response

interface SurveyRepository {

    suspend fun fetchSurvey() : Response<SurveyResponse>

    suspend fun saveAnswer(answers: Map<String, String>, records: List<Record>)

    suspend fun getAllAnswer() : Response<List<SurveyEntity>>

}