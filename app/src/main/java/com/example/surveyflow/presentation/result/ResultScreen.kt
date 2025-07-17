package com.example.surveyflow.presentation.result

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.surveyflow.utils.CustomTopBar
import com.example.surveyflow.utils.NetworkStatusToast
import com.example.surveyflow.utils.Response

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("FlowOperatorInvokedInComposition", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ResultScreen(
    navController: NavHostController,
    viewModel: ResultViewModel = hiltViewModel()
) {
    val uiState = viewModel.answerState.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchAnswers()
        Log.d("Survey screen", "ResultScreen: ${viewModel.answerState.value}")
    }


    Scaffold(topBar = {
        CustomTopBar(titleName = "Survey Result", isResult = true, onBack = {
            navController.navigateUp()
        }, modifier = Modifier)
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            NetworkStatusToast()
            when (uiState.value) {
                is Response.Error<*> -> {
                    Text("${uiState.value}")
                }

                is Response.Loading<*> -> {
                    CircularProgressIndicator()
                }

                is Response.Success<*> -> {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .navigationBarsPadding()
                    ) {
                        items(
                            uiState.value?.data ?: emptyList(),
                            key = { entity -> entity.id }) { ans ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("question Id : ${ans.questionId}")
                                Text(ans.answer)
                            }
                        }
                    }

                }

                null -> {}
            }

        }
    }

}