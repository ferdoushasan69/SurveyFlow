import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.surveyflow.presentation.navigation.Screen
import com.example.surveyflow.presentation.result.ResultScreen

@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Survey, modifier = modifier) {
        composable<Screen.Survey> {
            SurveyScreen(navController = navController)
        }

        composable<Screen.SurveyResult> {
            ResultScreen(navController = navController)
        }
    }
}
