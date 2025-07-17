package com.example.surveyflow.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    modifier: Modifier = Modifier,
    titleName: String = "",
    isResult: Boolean = false,
    onBack: () -> Unit = {}
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(titleName, color = MaterialTheme.colorScheme.onBackground)
        },
        navigationIcon = {
            if (isResult) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "")
                }
            }

        },
    )

}