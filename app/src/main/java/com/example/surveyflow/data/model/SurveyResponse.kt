package com.example.surveyflow.data.model

data class SurveyResponse(
    val metadata: Metadata,
    val record: List<Record>
)