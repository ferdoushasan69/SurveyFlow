package com.example.surveyflow.utils

import com.example.surveyflow.data.model.Record


fun Record.shouldShowSkip() = this.skip.id != "-1"

fun Record.shouldAlwaysShowNext() = this.type in listOf("textInput", "numberInput", "checkbox","camera")
