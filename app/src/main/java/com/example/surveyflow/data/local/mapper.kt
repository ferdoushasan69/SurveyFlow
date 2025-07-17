// File: data/source/local/RecordMapper.kt

package com.example.surveyflow.data.local

import com.example.surveyflow.data.model.Record

fun Record.toEntity(answer: String): SurveyEntity {
    return SurveyEntity(
        questionId = this.id,
        questionType = this.type,
        answer = answer
    )
}
