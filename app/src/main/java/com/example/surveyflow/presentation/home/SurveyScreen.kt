import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.surveyflow.data.model.Record
import com.example.surveyflow.presentation.home.SurveyViewModel
import com.example.surveyflow.presentation.navigation.Screen
import com.example.surveyflow.utils.CustomTopBar
import com.example.surveyflow.utils.Response
import com.example.surveyflow.utils.shouldAlwaysShowNext
import com.example.surveyflow.utils.shouldShowSkip


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SurveyScreen(
    modifier: Modifier = Modifier,
    viewModel: SurveyViewModel = hiltViewModel(), navController: NavHostController
) {
    val surveyState = viewModel.surveyResponse.observeAsState()
    val answerState = viewModel.answer.forEach { it.value }
    val currentStep = remember { mutableStateOf<Record?>(null) }
    val answers = viewModel.answer.toMutableMap()

    LaunchedEffect(surveyState.value) {
        val list = surveyState.value?.data?.record
        if (!list.isNullOrEmpty()) {
            currentStep.value = list.first()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadSurvey()
        Log.d(TAG, "SurveyScreen: ${surveyState.value?.data}")
    }

    when (val result = surveyState.value) {
        is Response.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is Response.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No Internet ${surveyState.value?.message}")
            }
        }

        is Response.Success -> {
            currentStep.value?.let { step ->
                Scaffold(
                    topBar = {
                        CustomTopBar(
                            modifier = Modifier.statusBarsPadding(),
                            titleName = "Survey Screen"
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp)
                                .navigationBarsPadding()
                        ) {
                            SurveyStepContent(
                                step = step,
                                onNext = { answer, nextId ->
                                    viewModel.setAnswer(step.id, answer)
                                    if (nextId == "submit") {
                                        viewModel.saveAnswers(
                                            viewModel.answer,
                                            result.data?.record ?: emptyList()
                                        )
                                        navController.navigate(Screen.SurveyResult)
                                    } else {
                                        currentStep.value =
                                            result.data?.record?.find { it.id == nextId }
                                    }
                                }
                            )
                        }
                    }
                }

            }
        }

        else -> Unit
    }
}

@Composable
fun SurveyStepContent(
    step: Record,
    onNext: (answer: String, nextId: String) -> Unit
) {
    val isLastStep = step.referTo?.id == "submit"
    val context = LocalContext.current

    when (step.type) {
        "multipleChoice" -> MultipleChoiceView(step = step, onNext = onNext, isLastStep, context)
        "textInput", "numberInput" -> InputFieldView(
            step = step,
            onNext = onNext,
            isLastStep,
            context
        )

        "dropdown" -> DropdownView(step = step, onNext = onNext, isLastStep, context)
        "checkbox" -> CheckboxView(step = step, onNext = onNext, isLastStep)
        "camera" -> CameraView(
            step = step,
            onNext = onNext,
            isLastStep = isLastStep,
        )

        else -> Text("Unsupported question type", modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun InputFieldView(
    step: Record,
    onNext: (answer: String, nextId: String) -> Unit,
    isLastStep: Boolean,
    context: Context
) {
    var text by remember { mutableStateOf("") }
    val isValid = step.validations.regex.let {
        Regex(it).matches(text)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(step.question.slug)
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Enter value") },
            keyboardOptions = KeyboardOptions(
                keyboardType = if (step.type == "numberInput") KeyboardType.Number else KeyboardType.Text
            ),
            modifier = Modifier.fillMaxWidth()
        )

        SurveyNavigationButtons(
            isLastStep = isLastStep,
            showSkip = step.shouldShowSkip(),
            showNext = step.shouldAlwaysShowNext(),
            onSkip = {
                onNext("skipped", step.skip.id)
            },
            onNext = {
                val nextId = step.referTo?.id
                if (nextId != null && isValid) {
                    onNext(text, nextId)
                } else {
                    Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT).show()
                }
            },
            nextEnabled = isValid
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownView(
    step: Record,
    onNext: (answer: String, nextId: String) -> Unit,
    isLastStep: Boolean,
    context: Context
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }

    val showSkip = step.shouldShowSkip()
    val showNext = true // Dropdown is user input, so we always show 'Next'

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(step.question.slug)

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select option") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                step.options?.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.value) },
                        onClick = {
                            selectedOption = option.value
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        SurveyNavigationButtons(
            isLastStep = isLastStep,
            showSkip = showSkip,
            showNext = showNext,
            onSkip = {
                onNext("skipped", step.skip.id)
            },
            onNext = {
                val nextId = step.options?.find { it.value == selectedOption }?.referTo?.id
                if (nextId != null) {
                    onNext(selectedOption, nextId)
                } else {
                    Toast.makeText(context, "Invalid next step", Toast.LENGTH_SHORT).show()
                }
            },
            nextEnabled = selectedOption.isNotEmpty()
        )
    }
}

@Composable
fun CheckboxView(
    step: Record,
    onNext: (answer: String, nextId: String) -> Unit,
    isLastStep: Boolean,
) {
    val selected = remember(step.id) {
        mutableStateMapOf<String, Boolean>().apply {
            step.options.forEach { put(it.value, false) }
        }
    }

    val selectedValues = selected.filter { it.value }.keys.joinToString(", ")
    val showSkip = step.shouldShowSkip()
    val showNext = step.shouldAlwaysShowNext()
    val nextEnabled = selected.any { it.value }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(step.question.slug)

        step.options.forEach { option ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = selected[option.value] == true,
                    onCheckedChange = { selected[option.value] = it }
                )
                Text(option.value)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        SurveyNavigationButtons(
            isLastStep = isLastStep,
            showSkip = showSkip,
            showNext = showNext,
            onSkip = {
                onNext("skipped", step.skip.id)
            },
            onNext = {
                val nextId = step.referTo.id
                onNext(selectedValues, nextId)
            },
            nextEnabled = nextEnabled
        )
    }
}

@Composable
fun CameraView(
    step: Record,
    onNext: (answer: String, nextId: String) -> Unit,
    isLastStep: Boolean,
) {
    val showSkip = step.shouldShowSkip()
    val showNext = step.shouldAlwaysShowNext()

    val captured = remember { mutableStateOf(false) }
    val latestOnNext by rememberUpdatedState(onNext)

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            if (bitmap != null) {
                val nextId = step.referTo?.id
                Log.d("CameraView", "Captured image, going to: $nextId")
                if (!nextId.isNullOrEmpty()) {
                    captured.value = true
                    latestOnNext("captured_image_uri", nextId)
                }
            }
        }

    LaunchedEffect(captured.value) {
        if (captured.value) {
            captured.value = false
            // Optionally trigger recomposition or log
        }
    }



    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(step.question.slug)

        SurveyNavigationButtons(
            isLastStep = isLastStep,
            showSkip = showSkip,
            showNext = showNext,
            onSkip = {
                onNext("skipped", step.skip.id)
            },
            onNext = {
                launcher.launch(null)
            },
            nextEnabled = true // always true for camera trigger
        )
    }
}

@Composable
fun MultipleChoiceView(
    step: Record,
    onNext: (answer: String, nextId: String) -> Unit,
    isLastStep: Boolean,
    context: Context
) {
    var selected by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(step.question.slug)

        step.options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    selected = option.value
                }
            ) {
                RadioButton(selected = selected == option.value, onClick = {
                    selected = option.value
                })
                Text(option.value)
            }
        }

        SurveyNavigationButtons(
            isLastStep = isLastStep,
            showSkip = step.shouldShowSkip(),
            showNext = true,
            onSkip = {
                onNext("skipped", step.skip.id)
            },
            onNext = {
                val nextId = step.options.find { it.value == selected }?.referTo?.id
                if (nextId != null) {
                    onNext(selected, nextId)
                } else {
                    Toast.makeText(context, "Please select an option", Toast.LENGTH_SHORT).show()
                }
            },
            nextEnabled = selected.isNotEmpty()
        )
    }
}

@Composable
fun SurveyNavigationButtons(
    isLastStep: Boolean,
    showSkip: Boolean,
    showNext: Boolean,
    onSkip: () -> Unit,
    onNext: () -> Unit,
    nextEnabled: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (showSkip) {
            Button(onClick = onSkip) {
                Text("Skip")
            }
        }

        if (showNext) {
            Button(
                onClick = onNext,
                enabled = nextEnabled
            ) {
                Text(if (isLastStep) "Submit" else "Next")
            }
        }
    }
}
