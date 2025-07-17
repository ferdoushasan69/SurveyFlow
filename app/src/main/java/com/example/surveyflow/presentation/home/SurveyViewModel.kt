package com.example.surveyflow.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.surveyflow.data.model.Record
import com.example.surveyflow.data.model.SurveyResponse
import com.example.surveyflow.domain.repository.SurveyRepository
import com.example.surveyflow.domain.usecase.SaveSurveyAnswerUseCase
import com.example.surveyflow.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurveyViewModel @Inject constructor(
    private val repository: SurveyRepository,
    private val surveyAnswerUseCase: SaveSurveyAnswerUseCase,
) : ViewModel() {


    private val _surveyResponse = MutableLiveData<Response<SurveyResponse>>()
    val surveyResponse: LiveData<Response<SurveyResponse>> = _surveyResponse


    private val _answers = mutableMapOf<String, String>()
    val answer: Map<String, String> get() = _answers


    fun setAnswer(id: String, answer: String) {
        _answers[id] = answer
    }

    fun loadSurvey() {
        viewModelScope.launch {
            try {
                _surveyResponse.value = Response.Loading()
                val result = repository.fetchSurvey()
                _surveyResponse.value = result
            } catch (e: Exception) {
                Log.e("SurveyViewmodel", "loadSurvey: ${e.cause}")
            }

        }
    }

    fun saveAnswers(answers: Map<String, String>, records: List<Record>) {
        viewModelScope.launch {
            try {
                _answers.putAll(answers)
                surveyAnswerUseCase(answer = answers, record = records)
            } catch (e: Exception) {

            }
        }
    }

    fun clearPreviousAnswer() {
        viewModelScope.launch {
        }
    }

}