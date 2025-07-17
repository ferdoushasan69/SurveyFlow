package com.example.surveyflow.presentation.navigation

import kotlinx.serialization.Serializable

object Screen {
    @Serializable
    data object Survey

    @Serializable
    data object SurveyResult
}