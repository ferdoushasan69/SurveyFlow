package com.example.surveyflow.data.repository

import com.example.surveyflow.data.model.Record
import com.example.surveyflow.data.model.SurveyResponse
import com.example.surveyflow.data.source.local.SurveyDao
import com.example.surveyflow.data.source.local.SurveyEntity
import com.example.surveyflow.data.source.local.toEntity
import com.example.surveyflow.data.source.remote.ApiService
import com.example.surveyflow.domain.repository.SurveyRepository
import com.example.surveyflow.utils.Response
import javax.inject.Inject


class SurveyRepositoryImpl @Inject constructor(private val apiService: ApiService,private val dao: SurveyDao) : SurveyRepository {
    override suspend fun fetchSurvey(): Response<SurveyResponse> {
        return try {
            val response = apiService.getServerList()
            Response.Success(response)
        } catch (e: Exception) {
            Response.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    override suspend fun saveAnswer(answers: Map<String, String>, records: List<Record>) {
        answers.forEach { (questionId, answer) ->
            val record = records.find { it.id == questionId}
            if (record != null) {
                dao.insertAnswer(record.toEntity(answer)) // ✅ Will now work
            }
        }
    }

    override suspend fun getAllAnswer(): Response<List<SurveyEntity>> {
        return try {
            val data = dao.getAllAnswers()
          Response.Success(data)
        }catch (e : Exception){
            e.printStackTrace()
            Response.Error(e.localizedMessage)
        }
    }


}