package com.example.surveyflow.domain.usecase

import com.example.surveyflow.domain.repository.SurveyRepository
import javax.inject.Inject

class GetSurveyAnswerUseCase @Inject constructor(
    private val repository: SurveyRepository
) {
    suspend operator fun invoke() = repository.getAllAnswer()
}