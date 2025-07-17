package com.example.surveyflow.presentation.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.surveyflow.data.local.SurveyEntity
import com.example.surveyflow.domain.usecase.GetSurveyAnswerUseCase
import com.example.surveyflow.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val getSurveyAnswerUseCase: GetSurveyAnswerUseCase
) : ViewModel() {

    private val _answerState = MutableLiveData<Response<List<SurveyEntity>>>(Response.Loading())
    val answerState: LiveData<Response<List<SurveyEntity>>> = _answerState


    fun fetchAnswers() {
        viewModelScope.launch {
            _answerState.value = Response.Loading()
            try {
                val result = getSurveyAnswerUseCase()
                _answerState.value = Response.Success(result.data ?: emptyList())
            } catch (e: Exception) {
                _answerState.value = Response.Error(e.localizedMessage ?: "Unknown Error")
            }
        }
    }
}