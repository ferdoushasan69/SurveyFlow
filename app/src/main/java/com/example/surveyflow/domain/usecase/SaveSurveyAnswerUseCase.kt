package com.example.surveyflow.domain.usecase

import com.example.surveyflow.data.model.Record
import com.example.surveyflow.domain.repository.SurveyRepository
import javax.inject.Inject

class SaveSurveyAnswerUseCase @Inject constructor(
    private val repository: SurveyRepository
) {
    suspend operator fun invoke(answer : Map<String, String>,record : List<Record>) = repository.saveAnswer(
        answers = answer,
        records = record
    )
}