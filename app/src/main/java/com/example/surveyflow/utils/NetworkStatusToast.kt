package com.example.surveyflow.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.surveyflow.presentation.home.networkStatusFlow

@Composable
fun NetworkStatusToast(context: Context = LocalContext.current) {
    // ✅ Proper usage of produceState
    val isConnected by produceState(initialValue = true, context) {
        networkStatusFlow(context).collect { value = it }
    }

    // ✅ Show toast only when disconnected
    LaunchedEffect(isConnected) {
        if (!isConnected) {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show()
        }
    }
}
