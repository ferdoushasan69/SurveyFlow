package com.example.surveyflow.data.source.remote

import com.example.surveyflow.data.model.SurveyResponse
import com.example.surveyflow.utils.Response
import retrofit2.http.GET
interface ApiService {
    @GET("v3/b/687374506063391d31aca23a")
    suspend fun getServerList(): SurveyResponse
}
